/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.codebullets.external.party.simulator.connections.websocket.inbound;

import com.codebullets.external.party.simulator.connections.ConnectionMonitor;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.URI;

/**
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    private final URI endpoint;
    private final ConnectionMonitor connectionMonitor;
    private final String connectionName;

    /**
     * Generates a new instance of WebSocketServerInitializer.
     */
    public WebSocketServerInitializer(final URI endpoint, final ConnectionMonitor connectionMonitor, final String connectionName) {
        this.endpoint = endpoint;
        this.connectionMonitor = connectionMonitor;
        this.connectionName = connectionName;
    }

    @Override
    public void initChannel(final SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("codec-http", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
        pipeline.addLast("handler", new NettyWebSocketHandler(endpoint, connectionMonitor, connectionName));
    }
}
