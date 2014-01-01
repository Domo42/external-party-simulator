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
package com.codebullets.external.party.simulator.connections.websocket.inbound;

import com.codebullets.external.party.simulator.connections.Connection;
import com.codebullets.external.party.simulator.connections.ConnectionConfig;
import com.codebullets.external.party.simulator.connections.ConnectionMonitor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * WebSocket connection that waits for clients to connect and send messages.
 */
public class InboundWebSocketConnection implements Connection {
    private static final Logger LOG = LoggerFactory.getLogger(InboundWebSocketConnection.class);

    private ConnectionMonitor connectionMonitor;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final ConnectionConfig config) {
        URI endpoint = URI.create(config.getEndpoint());

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketServerInitializer(endpoint, connectionMonitor));

        try {
            serverBootstrap.bind(endpoint.getPort()).sync().channel();
            LOG.info("Web socket server started at port {}.", endpoint.getPort());
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

            throw new IllegalStateException("Error starting web socket endpoint.", e);
        }
    }

    @Override
    public void setMonitor(final ConnectionMonitor monitor) {
        connectionMonitor = monitor;
    }

    @Override
    public void send(final String text) {
    }

    @Override
    public void send(final byte[] buffer) {
    }

    @Override
    public void send(final Object object) {
    }
}