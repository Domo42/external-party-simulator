/* --------------------------------------------------
 * Contains a list of default connection definitions
 * to open as the party simulator is started.
 * --------------------------------------------------
 */

import com.codebullets.external.party.simulator.connections.websocket.inbound.InboundWebSocketConnection
import com.codebullets.external.party.simulator.connections.websocket.outbound.OutboundWebSocketConnection

connection("control", InboundWebSocketConnection) {
    endpoint = "ws://localhost:5353/control"
    timeout = 300_000
}

connection("echo", InboundWebSocketConnection) {
    endpoint = "ws://localhost:5354/echo"
    timeout = 300_000
}

// loopback connection to own simulator control port for testing
connection("loopback", OutboundWebSocketConnection) {
    endpoint = "ws://localhost:5353/control"
    timeout = 30_000
}