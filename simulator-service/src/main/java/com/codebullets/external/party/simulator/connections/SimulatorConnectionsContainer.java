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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    @Override
    public int size() {
        return connections.size();
    }

    @Override
    public boolean isEmpty() {
        return connections.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return connections.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return connections.containsValue(value);
    }

    @Override
    public Connection get(final Object key) {
        return connections.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection put(final String refName, final Connection connection) {
        return connections.put(refName, connection);
    }

    @Override
    public Connection remove(final Object key) {
        return connections.remove(key);
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Connection> m) {
        connections.putAll(m);
    }

    @Override
    public void clear() {
        connections.clear();
    }

    @Override
    public Set<String> keySet() {
        return connections.keySet();
    }

    @Override
    public Collection<Connection> values() {
        return connections.values();
    }

    @Override
    public Set<Entry<String, Connection>> entrySet() {
        return connections.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(final String refName) {
        connections.remove(refName);
    }
}