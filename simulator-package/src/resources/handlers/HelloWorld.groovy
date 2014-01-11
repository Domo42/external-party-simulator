import com.codebullets.external.party.simulator.pipeline.AbstractMessageHandler
import com.codebullets.external.party.simulator.pipeline.ContentType
import com.codebullets.external.party.simulator.pipeline.MessageReceivedEvent

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
