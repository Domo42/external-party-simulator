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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores a list of service connections.
 */
public class SimulatorConnectionsContainer implements ConnectionsContainer {
    private Map<String, Connection> connections = Collections.synchronizedMap(new HashMap<String, Connection>());

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection get(final String refName) {
        return connections.get(refName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String refName, final Connection connection) {
        connections.put(refName, connection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final String refName) {
        connections.remove(refName);
    }
}