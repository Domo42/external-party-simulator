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

import com.codebullets.external.party.simulator.connections.ConnectionConfig;
import com.codebullets.external.party.simulator.connections.ConnectionMonitor;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Initializer for the outbound websocket client.
 */
public class WebSocketClientInitializer extends ChannelInitializer<SocketChannel> {
    private final ConnectionMonitor connectionMonitor;
    private final ConnectionConfig connectionConfig;
    private final OutboundWebSocketConnection outboundWebSocketConnection;

    /**
     * Generates a new instance of WebSocketServerInitializer.
     */
    public WebSocketClientInitializer(
            final ConnectionMonitor connectionMonitor,
            final ConnectionConfig connectionConfig,
            final OutboundWebSocketConnection outboundWebSocketConnection) {
        this.connectionMonitor = connectionMonitor;
        this.connectionConfig = connectionConfig;
        this.outboundWebSocketConnection = outboundWebSocketConnection;
    }

    @Override
    public void initChannel(final SocketChannel ch) throws Exception {
        HttpHeaders headers = new DefaultHttpHeaders();

        // Use half of the timeout for ping if there is no other traffic.
        // if timeoutSec is very small (<1s) then pingTimeout will be 0 which
        // disables it in the IdleStateHandler
        long timeoutSec = TimeUnit.MILLISECONDS.toSeconds(connectionConfig.getTimeout());
        long pingTimeout = timeoutSec / 2;

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("http-codec", new HttpClientCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
        pipeline.addLast("idleStateHandler", new IdleStateHandler((int) timeoutSec, (int) pingTimeout, 0));
        pipeline.addLast("keepAliveHandler", new KeepAliveHandler(outboundWebSocketConnection));
        pipeline.addLast("handler",
                new NettyWebSocketClientHandler(
                        WebSocketClientHandshakerFactory.newHandshaker(
                            URI.create(connectionConfig.getEndpoint()), WebSocketVersion.V13, null, false, headers),
                        connectionMonitor,
                        connectionConfig.getName()));
    }
}