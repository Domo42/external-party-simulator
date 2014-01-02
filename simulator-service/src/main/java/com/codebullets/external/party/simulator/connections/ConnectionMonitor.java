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
package com.codebullets.external.party.simulator.connections;

import com.codebullets.external.party.simulator.pipeline.MessageWorkItem;

/**
 * Callback interface that is injected into a connection. The connection
 * needs to call methods on the monitor which will then trigger
 * possible available handlers.
 */
public interface ConnectionMonitor {
    /**
     * Called when an outbound or inbound connection has been established.
     */
    void connectionEstablished(ConnectionContext context);

    /**
     * Called when a message has been received.
     */
    void messageReceived(MessageWorkItem message);
}