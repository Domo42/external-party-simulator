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
package com.codebullets.external.party.simulator.pipeline;

import com.codebullets.external.party.simulator.connections.ConnectionsContainer;
import com.codebullets.external.party.simulator.worker.SimulatorStateContainer;
import com.codebullets.external.party.simulator.worker.WorkerQueue;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import groovy.lang.GroovyClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Calls a list of groovy scripts.
 */
public class GroovyScriptPipeline implements ScriptPipeline {
    private static final Logger LOG = LoggerFactory.getLogger(GroovyScriptPipeline.class);

    private HandlerScriptLoader scriptLoader;
    private final ConnectionsContainer connectionsContainer;
    private final WorkerQueue queue;
    private final SimulatorStateContainer state;
    private final Cache<CacheKey, Iterable<AbstractHandler>> handlerCache = CacheBuilder.newBuilder().build();
    private GroovyClassLoader classLoader;

    /**
     * Generates a new instance of GroovyScriptPipeline.
     */
    @Inject
    public GroovyScriptPipeline(
            final HandlerScriptLoader scriptLoader,
            final ConnectionsContainer connectionsContainer,
            final WorkerQueue queue,
            final SimulatorStateContainer state) {
        this.scriptLoader = scriptLoader;
        this.connectionsContainer = connectionsContainer;
        this.queue = queue;
        this.state = state;
    }

    @Override
    public void reset() {
        try {
            handlerCache.invalidateAll();
            handlerCache.cleanUp();

            if (classLoader != null) {
                classLoader.clearCache();
                classLoader.close();
                classLoader = null;
            }
        } catch (IOException e) {
            LOG.warn("Error closing groovy class loader.", e);
        }

        classLoader = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final MessageReceivedEvent message) {
        Iterable<AbstractMessageHandler> handlers = getHandlers(message);
        for (AbstractMessageHandler handler : handlers) {
            tryHandleMessage(handler, message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final ConnectionEstablishedEvent establishedEvent) {
        Iterable<AbstractConnectionEstablishedHandler> establishedHandlers = getHandlers(establishedEvent);
        for (AbstractConnectionEstablishedHandler handler : establishedHandlers) {
            tryHandleMessage(handler, establishedEvent);
        }
    }

    private void tryHandleMessage(final AbstractMessageHandler handler, final MessageReceivedEvent message) {
        try {
            handler.setWorkerQueue(queue);
            handler.setConnections(connectionsContainer);
            handler.setState(state);
            handler.handle(message);
        } catch (Exception e) {
            LOG.error("Error handling message.", e);
        }
    }

    private void tryHandleMessage(final AbstractConnectionEstablishedHandler handler, final ConnectionEstablishedEvent message) {
        try {
            handler.setWorkerQueue(queue);
            handler.setConnections(connectionsContainer);
            handler.setState(state);
            handler.handle(message);
        } catch (Exception e) {
            LOG.error("Error handling message.", e);
        }
    }

    private Iterable<AbstractMessageHandler> getHandlers(final MessageReceivedEvent msgReceivedEvent) {
        Iterable<AbstractHandler> handlers = null;
        try {
            handlers = handlerCache.get(CacheKey.create(msgReceivedEvent),
                    new Callable<Iterable<AbstractHandler>>() {
                        @Override
                        public Iterable<AbstractHandler> call() {
                            return cast(scriptLoader.loadMatchingScriptHandlers(msgReceivedEvent, getClassLoader()));
                        }
                    });
        } catch (ExecutionException e) {
            LOG.warn("Error loading script handlers.", e);
        }

        return cast(handlers);
    }

    private Iterable<AbstractConnectionEstablishedHandler> getHandlers(final ConnectionEstablishedEvent establishedEvent) {
        Iterable<AbstractHandler> handlers = null;
        try {
            handlers = handlerCache.get(CacheKey.create(establishedEvent),
                    new Callable<Iterable<AbstractHandler>>() {
                        @Override
                        public Iterable<AbstractHandler> call() {
                            return cast(scriptLoader.loadMatchingScriptHandlers(establishedEvent, getClassLoader()));
                        }
                    });
        } catch (ExecutionException e) {
            LOG.warn("Error loading script handlers.", e);
        }

        return cast(handlers);
    }

    private GroovyClassLoader getClassLoader() {
        if (classLoader == null) {
            classLoader = new GroovyClassLoader(this.getClass().getClassLoader());
        }

        return classLoader;
    }

    private <T> Iterable<T> cast(final Iterable<?> source) {
        return (Iterable<T>) source;
    }

    /**
     * Key used to identify the list of handlers for a simulator event.
     */
    private final static class CacheKey {
        private final EventType eventType;
        private final String msgType;
        private final String connection;
        private final ContentType contentType;

        private CacheKey(final EventType eventType, final String msgType, final String connection, final ContentType contentType) {
            this.eventType = eventType;
            this.msgType = msgType;
            this.connection = connection;
            this.contentType = contentType;
        }

        @Override
        public boolean equals(final Object obj) {
            boolean isEqual = false;

            if (this == obj) {
                isEqual = true;
            } else if (obj instanceof CacheKey) {
                CacheKey other = (CacheKey) obj;

                isEqual = Objects.equals(eventType, other.eventType)
                        && Objects.equals(msgType, other.msgType)
                        && Objects.equals(connection, other.connection)
                        && Objects.equals(contentType, other.contentType);
            }

            return isEqual;
        }

        @Override
        public int hashCode() {
            return Objects.hash(eventType, msgType, connection, contentType);
        }

        /**
         * Create a new instance.
         */
        public static CacheKey create(final ConnectionEstablishedEvent event) {
            return new CacheKey(EventType.CONNECTION_ESTABLISHED, null, event.getConnectionContext().getConnectionName(), null);
        }

        /**
         * Create a new instance.
         */
        public static CacheKey create(final MessageReceivedEvent event) {
            return new CacheKey(
                    EventType.MESSAGE_RECEIVED,
                    event.getMessageType(),
                    event.getConnectionContext().getConnectionName(),
                    event.getContentType());
        }
    }

    /**
     * List of possible simulator event types.
     */
    private enum EventType {
        MESSAGE_RECEIVED, CONNECTION_ESTABLISHED
    }
}