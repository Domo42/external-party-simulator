import com.codebullets.external.party.simulator.pipeline.AbstractConnectionEstablishedHandler
import com.codebullets.external.party.simulator.pipeline.ConnectionEstablishedEvent

/**
 * This class is an example handler that is triggered in case either an
 * inbound or outbound connection has been established.<br/>
 * The first thing it does is sending a greeting text back.
 */
class GreetingsStranger extends AbstractConnectionEstablishedHandler {
    /**
     * Overriding this method is optional. It ensures this handler is
     * only executed in case the the connection being established is
     * named 'control'. By default the handler would be executed on all
     * connections.
     */
    @Override
    protected String getConnectionFilter() {
        return "*"
    }

    /**
     * This method is called by the simulator framework every time a
     * new connection has been established.
     */
    @Override
    void handle(final ConnectionEstablishedEvent connectedEvent) {
        // as soon as a connection is established send a greeting back to caller
        sendTo(connectedEvent.connectionContext, "Greetings Stranger!")

        // this text will not be received by caller. Instead the echo is
        // broadcast to clients connected to the 'echo' port
        echo("Greeting sent back back to %s id = %s", connectedEvent.connectionContext.connectionName, connectedEvent.connectionContext.id)
    }
}
