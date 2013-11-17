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
 * Configuration data for outbound connections.
 */
public class OutboundConnectionConfig {
    private String name;
    private String endpoint;
    private int timeoutSec;
    private int reconnectIntervalSec;

    /**
     * Gets the reference name of the connection.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the reference name of the connection.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets a String representing the target connection endpoint.
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Sets the connection endpoint address.
     */
    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Gets the connection timeout in seconds. (default 30s)
     */
    public int getTimeoutSec() {
        return timeoutSec;
    }

    /**
     * Sets the connection timeout in seconds.
     */
    public void setTimeoutSec(final int timeoutSec) {
        this.timeoutSec = timeoutSec;
    }

    /**
     * Gets the reconnect interval in seconds.
     */
    public int getReconnectIntervalSec() {
        return reconnectIntervalSec;
    }

    /**
     * Sets the reconnect interval in seconds.
     */
    public void setReconnectIntervalSec(final int reconnectIntervalSec) {
        this.reconnectIntervalSec = reconnectIntervalSec;
    }
}