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
//The MIT License
//
//Copyright (c) 2009 Carl Bystršm
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.

package com.codebullets.external.party.simulator.connections.websocket.outbound;

import com.codebullets.external.party.simulator.connections.ConnectionMonitor;
import com.codebullets.external.party.simulator.connections.websocket.NettyConnectionContext;
import com.codebullets.external.party.simulator.pipeline.MessageReceivedEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler performing web socket handshake.
 */
public class NettyWebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(NettyWebSocketClientHandler.class);

    private final WebSocketClientHandshaker handshaker;
    private final ConnectionMonitor connectionMonitor;
    private final String connectionName;
    private ChannelPromise handshakeFuture;
    private NettyConnectionContext context;

    /**
     * Generates a new instance of NettyWebSocketClientHandler.
     */
    public NettyWebSocketClientHandler(final WebSocketClientHandshaker handshaker, final ConnectionMonitor connectionMonitor, final String connectionName) {
        this.handshaker = handshaker;
        this.connectionMonitor = connectionMonitor;
        this.connectionName = connectionName;
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        LOG.info("WebSocket Client {} disconnected!", connectionName);
    }

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(ch, (FullHttpResponse) msg);
            LOG.info("WebSocket client {} connected.", connectionName);
            connectionMonitor.connectionEstablished(getContext(ctx));
            handshakeFuture.setSuccess();
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new Exception("Unexpected FullHttpResponse (getStatus=" + response.getStatus() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            LOG.debug("WebSocket client {} received message: " + textFrame.text(), connectionName);
            connectionMonitor.messageReceived(MessageReceivedEvent.create(getContext(ctx), textFrame.text()));
        } else if (frame instanceof BinaryWebSocketFrame) {
            byte[] buffer = frame.content().array();
            LOG.debug("WebSocket client {} received buffer with length " + buffer.length, connectionName);
        } else if (frame instanceof PingWebSocketFrame) {
            LOG.trace("WebSocket client {} received ping.", connectionName);
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
        } else if (frame instanceof CloseWebSocketFrame) {
            LOG.debug("WebSocket client {} received closing frame.", connectionName);
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        LOG.warn("Exception web socket client connection.", cause);

        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }

        ctx.close();
    }

    private NettyConnectionContext getContext(final ChannelHandlerContext ctx) {
        if (context == null) {
            context = new NettyConnectionContext(ctx.channel(), connectionName);
        }

        return context;
    }
}
