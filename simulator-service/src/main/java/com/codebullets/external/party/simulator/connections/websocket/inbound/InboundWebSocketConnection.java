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
import com.codebullets.external.party.simulator.connections.ConnectionContext;
import com.codebullets.external.party.simulator.connections.ConnectionMonitor;
import com.codebullets.external.party.simulator.connections.websocket.NettyConnectionContext;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
                .childHandler(new WebSocketServerInitializer(endpoint, connectionMonitor, config));

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

    /**
     * Operation not supported on inbound connection. Use {@link #send(com.codebullets.external.party.simulator.connections.ConnectionContext, String)}
     * instead.
     */
    @Override
    public void send(final String text) {
        throw new UnsupportedOperationException("Sending is only possible for an inbound connection when the inbound connection context is specified.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final ConnectionContext context, final String text) {
        if (context instanceof NettyConnectionContext) {
            NettyConnectionContext nettyContext = (NettyConnectionContext) context;
            nettyContext.getChannel().writeAndFlush(new TextWebSocketFrame(text));
        } else {
            LOG.warn("Expected context of type NettyConnectionContext, but was " + context.getClass().getSimpleName());
        }
    }

    /**
     * Operation not supported without connection context. Use {@link #send(com.codebullets.external.party.simulator.connections.ConnectionContext, byte[])}
     * instead.
     */
    @Override
    public void send(final byte[] buffer) {
        throw new UnsupportedOperationException("Sending is only possible for an inbound connection when the inbound connection context is specified.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final ConnectionContext context, final byte[] buffer) {
        if (context instanceof NettyConnectionContext) {
            NettyConnectionContext nettyContext = (NettyConnectionContext) context;
            ByteBuf binaryData = Unpooled.wrappedBuffer(buffer);
            nettyContext.getChannel().writeAndFlush(new BinaryWebSocketFrame(binaryData));
        } else {
            LOG.warn("Expected context of type NettyConnectionContext, but was " + context.getClass().getSimpleName());
        }
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