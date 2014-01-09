import com.codebullets.external.party.simulator.pipeline.AbstractConnectionEstablishedHandler
import com.codebullets.external.party.simulator.pipeline.ConnectionEstablishedWorkItem

class GreetingsStranger extends AbstractConnectionEstablishedHandler {

    @Override
    void handle(final ConnectionEstablishedWorkItem establishedItem) {
        // as soon as a connection is established send a greeting back to caller
        sendTo(establishedItem.connectionContext, "Greetings Stranger!")
    }
}
