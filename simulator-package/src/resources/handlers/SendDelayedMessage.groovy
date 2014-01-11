import com.codebullets.external.party.simulator.pipeline.AbstractMessageHandler
import com.codebullets.external.party.simulator.pipeline.ContentType
import com.codebullets.external.party.simulator.pipeline.MessageReceivedEvent

/**
 * Sends a message back to the caller indicating the initial delay. The
 * message item originates from DelayResponseHandler.groovy
 */
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
     * This handler executes the message work item right away.
     * The original execution delay has been caused by the DelayResponseHandler.
     */
    @Override
    void handle(final MessageReceivedEvent receivedEvent) {
        def context = receivedEvent.connectionContext;

        // this text will be received by clients connected to the echo connection
        echo("Sending initial greetings to %s, id = %s", context.connectionName, context.id);

        // this text will be received by whoever has connected
        sendTo(context, "This message has been delayed by " + receivedEvent.objectContent + " seconds.")
    }
}
