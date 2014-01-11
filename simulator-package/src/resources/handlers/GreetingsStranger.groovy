import com.codebullets.external.party.simulator.pipeline.AbstractConnectionEstablishedHandler
import com.codebullets.external.party.simulator.pipeline.ConnectionEstablishedEvent

/**
 * This class is an example handler that is triggered in case either an
 * inbound or outbound connection has been established.<br/>
 * The first thing it does is sending a greeting text back.
 */
class GreetingsStranger extends AbstractConnectionEstablishedHandler {

    /**
     * This method is called by the simulator framework every time a
     * new connection has been established.
     */
    @Override
    void handle(final ConnectionEstablishedEvent connectedEvent) {
        // as soon as a connection is established send a greeting back to caller
        sendTo(connectedEvent.connectionContext, "Greetings Stranger!")
    }
}
