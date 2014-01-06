/*
 * Copyright 2014 Stefan Domnanovits
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

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

/**
 * Responsible to keep the connection alive using ping messages and starts reconnect
 * in case the connection goes down.
 */
public class KeepAliveHandler extends ChannelHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(KeepAliveHandler.class);
    private static final int RECONNECT_DELAY_SEC = 5;
    private final OutboundWebSocketConnection outboundWebSocketConnection;
    private boolean isAlive;

    /**
     * Generates a new instance of KeepAliveHandler.
     */
    public KeepAliveHandler(final OutboundWebSocketConnection outboundWebSocketConnection) {
        this.outboundWebSocketConnection = outboundWebSocketConnection;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        isAlive = true;
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        isAlive = false;

        // start reconnect
        final EventLoop loop = ctx.channel().eventLoop();
        loop.schedule(
            new Runnable() {
                @Override
                public void run() {
                    outboundWebSocketConnection.openConnection();
                }
            },
            RECONNECT_DELAY_SEC, TimeUnit.SECONDS);
    }

    /**
     * Triggered by the IdleStateHandler in the channel pipeline. Indicates that either
     * no message has been either sent or received for a longer period of time.
     */
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE && isAlive) {
                // no message has been sent for longer period of time -> send ping to keep connection alive
                ctx.writeAndFlush(new PingWebSocketFrame());
            } else if (e.state() == IdleState.READER_IDLE) {
                // no message has been received for a longer period of time
                // this means even no pong events.
                LOG.warn("No message received in expected time.");
                ctx.close();
            }
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (cause instanceof ConnectException) {
            LOG.warn("Failed to connect: " + cause.getMessage());
        }

        ctx.close();
    }
}