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
import groovy.lang.GroovyClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Calls a list of groovy scripts.
 */
public class GroovyScriptPipeline implements ScriptPipeline {
    private static final Logger LOG = LoggerFactory.getLogger(GroovyScriptPipeline.class);

    private HandlerScriptLoader scriptLoader;
    private final ConnectionsContainer connectionsContainer;
    private final WorkerQueue queue;
    private final SimulatorStateContainer state;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final MessageWorkItem message) {
        GroovyClassLoader classLoader = new GroovyClassLoader(this.getClass().getClassLoader());
        Iterable<AbstractMessageHandler> handlers = scriptLoader.loadMatchingSciptHandlers(message, classLoader);

        for (AbstractMessageHandler handler : handlers) {
            tryHandleMessage(handler, message);
        }
    }

    private void tryHandleMessage(final AbstractMessageHandler handler, final MessageWorkItem message) {
        try {
            handler.setWorkerQueue(queue);
            handler.setConnectionContainer(connectionsContainer);
            handler.setState(state);
            handler.handle(message);
        } catch (Exception e) {
            LOG.error("Error handling message.", e);
        }
    }
}