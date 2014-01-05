/*
 * Copyright 2013 Stefan Domnanovits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codebullets.external.party.simulator.connections.websocket.outbound;

import com.codebullets.external.party.simulator.connections.Connection;
import com.codebullets.external.party.simulator.connections.ConnectionConfig;
import com.codebullets.external.party.simulator.connections.ConnectionContext;
import com.codebullets.external.party.simulator.connections.ConnectionMonitor;

/**
 * WebSocket connection connecting to another web socket endpoint.
 */
public class OutboundWebSocketConnection implements Connection {
    @Override
    public void start(final ConnectionConfig config) {
    }

    @Override
    public void setMonitor(final ConnectionMonitor monitor) {
    }

    @Override
    public void send(final String text) {
    }

    @Override
    public void send(final ConnectionContext context, final String text) {
    }

    @Override
    public void send(final byte[] buffer) {
    }

    @Override
    public void send(final Object object) {
    }
}