import com.codebullets.external.party.simulator.connections.Connection
import com.codebullets.external.party.simulator.pipeline.AbstractMessageHandler
import com.codebullets.external.party.simulator.pipeline.ContentType
import com.codebullets.external.party.simulator.pipeline.MessageWorkItem

class HelloWorldHandler extends AbstractMessageHandler {

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
        // in case message is the test hello world text, send back the
        // message in upper case letters
        if (messageItem.textContent().equals("Hello, World!")) {
            Connection callingConnection = getConnection(messageItem.connectionContext)
            callingConnection.send(messageItem.connectionContext, messageItem.textContent().toUpperCase())
        }
    }
}
