import com.codebullets.external.party.simulator.pipeline.AbstractMessageHandler
import com.codebullets.external.party.simulator.pipeline.ContentType
import com.codebullets.external.party.simulator.pipeline.MessageWorkItem

import java.util.concurrent.TimeUnit

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
    void handle(final MessageWorkItem messageItem) {
        def text = messageItem.textContent

        // if incoming text contains only digits
        if (text ==~ /\d+/) {
            def delayInSeconds = Integer.parseInt(text);
            def newWorkItem = MessageWorkItem.create(messageItem.connectionContext, delayInSeconds, "delayedMessage")
            workerQueue.addDelayed(newWorkItem, delayInSeconds, TimeUnit.SECONDS)
        }
    }
}