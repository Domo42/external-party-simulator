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

/**
 * Contains settings of a single connections.
 */
public class ConnectionConfig {
    private String name;
    private String endpoint;
    private int timeout;
    private Connection connection;

    /**
     * Gets the name of the connection.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the connection.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the endpoint to open or connect to.
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the endpoint connection to open or connect to.
     */
    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Gets a value in ms until the connection times out.<p/>
     * For incoming connections this means until a client is considered no
     * longer connected.<br/>
     * For outgoing connection it means a time of inactivity until a server might no longer
     * consider the connection active.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets a value in ms until the connection times out.
     */
    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets the concrete connection class to start.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Sets the concrete connection implementation to start.
     */
    public void setConnection(final Connection connection) {
        this.connection = connection;
    }
}