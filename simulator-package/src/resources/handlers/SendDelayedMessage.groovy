import com.codebullets.external.party.simulator.pipeline.AbstractMessageHandler
import com.codebullets.external.party.simulator.pipeline.ContentType
import com.codebullets.external.party.simulator.pipeline.MessageWorkItem

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
    void handle(final MessageWorkItem messageItem) {
        sendTo(messageItem.connectionContext, "This message has been delayed by " + messageItem.objectContent + " seconds.")
    }
}
