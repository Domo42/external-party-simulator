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
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static com.google.common.base.Preconditions.checkState;

/**
 * WebSocket connection connecting to another web socket endpoint.
 */
public class OutboundWebSocketConnection implements Connection {
    private static final Logger LOG = LoggerFactory.getLogger(OutboundWebSocketConnection.class);

    private ConnectionMonitor monitor;
    private Channel channel;

    @Override
    public void start(final ConnectionConfig config) {
        URI endpoint = URI.create(config.getEndpoint());

        EventLoopGroup eventGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventGroup)
                .channel(NioSocketChannel.class)
                .handler(new WebSocketClientInitializer(monitor, config));

        try {
            LOG.info("Connecting to web socket server at {}", endpoint);
            channel = bootstrap.connect(endpoint.getHost(), endpoint.getPort()).sync().channel();
        } catch (InterruptedException e) {
            eventGroup.shutdownGracefully();
            throw new IllegalStateException("Error starting web socket endpoint.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMonitor(final ConnectionMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final String text) {
        checkState(channel != null, "Not connected to service.");
        channel.writeAndFlush(new TextWebSocketFrame(text));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final ConnectionContext context, final String text) {
        send(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final byte[] buffer) {
        checkState(channel != null, "Not connected to service.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final ConnectionContext context, final byte[] buffer) {
    }

    /**
     * Sending of Java objects not supported on websocket connection.
     */
    @Override
    public void send(final Object object) {
        throw new UnsupportedOperationException("Send of plain Java objects not supported on websocket connection.");
    }

    /**
     * Sending of Java objects not supported on websocket connection.
     */
    @Override
    public void send(final ConnectionContext context, final Object object) {
        throw new UnsupportedOperationException("Send of plain Java objects not supported on websocket connection.");
    }
}