external-party-simulator
========================

Allows simulation of external components that either connect to your API; or
the simulation of an external one own services are connecting to.

Simulation is done by executing one ore more Groovy scripts whenever data is
received or a new connection has been established.

In addition the simulator makes it easy to performed timed events. For example
if a regular keep alive is expected.


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
