/* --------------------------------------------------
 * Contains a list of default connection definitions
 * to open as the party simulator is started.
 * --------------------------------------------------
 */

import com.codebullets.external.party.simulator.connections.websocket.inbound.InboundWebSocketConnection

connection("control", InboundWebSocketConnection) {
    endpoint = "ws://localhost:5353/control"
    timeout = 30_000
}