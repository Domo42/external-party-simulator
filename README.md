external-party-simulator
========================

The simulator can be used to fake other components that connect to your
API; or to simulate an external system your services are connecting to.

Simulation is done by executing one ore more Groovy scripts whenever data is
received or a new connection has been established.

In addition the simulator makes it easy to performed timed events. For example
if a regular keep alive is expected.

Have a look at the projects github [wiki](https://github.com/Domo42/external-party-simulator/wiki)
for additional information.


## Writing a Handler

The next snippet shows a simple "Hello World" handler. This handler is executed
every time any kind of text message is received. Inside the handler method
the incoming text is checked for "Hello World!". If true an answer is returned
converting the original text to all uppercase letters.

```groovy
/**
 * This handler returns an all upper case version of hello world
 * in case the phrase "Hello, World!" is received.
 */
class HelloWorldHandler extends AbstractMessageHandler {

    /**
     * Overriding this method indicates this handler is only interested
     * in receiving plain text messages.
     */
    @Override
    ContentType getContentType() {
        return ContentType.TEXT
    }

    /**
     * Overriding this method indicates this handler is only interested
     * in message which do not (yet) have a specific message type.
     */
    @Override
    String getMessageType() {
        return null
    }

    /**
     * This method is called by the simulator framework when a message is
     * received matching all criteria from above (text + no type).
     */
    @Override
    void handle(final MessageReceivedEvent receivedEvent) {
        // in case message is the test hello world text, send back the
        // message in upper case letters
        if (receivedEvent.textContent == "Hello, World!") {
            sendTo(receivedEvent.connectionContext, receivedEvent.textContent.toUpperCase())
        }
    }
}
```

## Chaining Handlers

More complex workflows can be created by chaining handlers together. This is
done by adding custom events into simulator worker queue. For example a handler
can analyze the incoming text and check whether it is in JSON format. Once
detected this handle can puts a new event item into the queue with a message
of type "JSON". This will result in only the handler being called that have
returned "JSON" as content of the _messageType_ property.

The next snippets show a more simple workflow. Every incoming message is
checked whether the content consists purely of digit characters. If yes
a new purely internal event item is created and put into the worker queue.
In addition a delay is specified. This results event item not being executed
right away.

```groovy
class DelayResponseHandler extends AbstractMessageHandler {
    @Override
    ContentType getContentType() {
        return ContentType.TEXT
    }

    @Override
    String getMessageType() {
        return null
    }

    @Override
    void handle(final MessageReceivedEvent receivedEvent) {
        def text = receivedEvent.textContent

        // if incoming text contains only digits
        if (text ==~ /\d+/) {
            def delayInSeconds = Integer.parseInt(text);
            def newWorkItem = MessageReceivedEvent.create(
                    receivedEvent.connectionContext, delayInSeconds, "delayedMessage")
            workerQueue.addDelayed(newWorkItem, delayInSeconds, TimeUnit.SECONDS)
        }
    }
}
```

Note the the ```MessageReceivedEvent.create``` call specified the string
"delayedMessage" as parameter. This string is used again in the next snippet
to create a handler that is only triggered for this specific (internal)
message type. This filtering is caused by returning "delayedMessage" as message
type.

```groovy
class SendDelayedMessage extends AbstractMessageHandler {
    @Override
    ContentType getContentType() {
        return ContentType.OBJECT
    }

    @Override
    String getMessageType() {
        return "delayedMessage"
    }

    /**
     * The original execution delay has been caused by the DelayResponseHandler.
     */
    @Override
    void handle(final MessageReceivedEvent receivedEvent) {
        def context = receivedEvent.connectionContext;

        // this text will be received by whoever has sent the original digits message
        sendTo(context, "This message has been delayed by " + receivedEvent.objectContent + " seconds.")
    }
}
```
