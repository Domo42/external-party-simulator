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
package com.codebullets.external.party.simulator.connections.websocket;

import com.codebullets.external.party.simulator.connections.ConnectionContext;
import io.netty.channel.Channel;

/**
 * Context used to identify the specific netty channel.
 */
public class NettyConnectionContext implements ConnectionContext {
    private final Channel channel;
    private final String connectionName;

    /**
     * Generates a new instance of NettyConnectionContext.
     */
    public NettyConnectionContext(final Channel channel, final String connectionName) {
        this.channel = channel;
        this.connectionName = connectionName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return channel.id().asLongText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConnectionName() {
        return connectionName;
    }
}