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
package com.codebullets.external.party.simulator.pipeline;

import com.codebullets.external.party.simulator.connections.Connection;
import com.codebullets.external.party.simulator.connections.ConnectionContext;
import com.codebullets.external.party.simulator.connections.ConnectionsContainer;
import com.codebullets.external.party.simulator.worker.SimulatorStateContainer;
import com.codebullets.external.party.simulator.worker.WorkerQueue;

import javax.annotation.Nullable;

/**
 * Base class for all message handlers.
 */
public abstract class AbstractMessageHandler {
    private ConnectionsContainer connections;
    private WorkerQueue workerQueue;
    private SimulatorStateContainer state;

    /**
     * Gets the type of content the handler is able to process.
     */
    public abstract ContentType getContentType();

    /**
     * Gets the message specific type, describing the actual message. This value
     * can be null in case the handler is registering for untyped messages. It can
     * also contain glob wildcards ('*', '?') to register for a range of matching types.
     */
    @Nullable
    public abstract String messageType();

    /**
     * This method is called for the handler to perform its logic.
     */
    public abstract void handle(final MessageWorkItem messageItem);

    /**
     * Called by the simulator framework to set list of available connections.
     */
    public void setConnectionContainer(final ConnectionsContainer connectionContainer) {
        this.connections = connectionContainer;
    }

    /**
     * Gets a container holding all available connections.
     */
    public ConnectionsContainer getConnectionContainer() {
        return connections;
    }

    /**
     * Gets the specific connection based on the context.
     */
    protected Connection getConnection(final ConnectionContext context) {
        String connectionName = context.getConnectionName();
        return connections.get(connectionName);
    }

    /**
     * Called by the simulator framework to set the queue.
     */
    protected void setWorkerQueue(final WorkerQueue queue) {
        workerQueue = queue;
    }

    /**
     * Gets the simulator worker queue. This queue can be used to enqueue additional
     * {@link MessageWorkItem} for further processing.
     */
    public WorkerQueue getWorkerQueue() {
        return workerQueue;
    }

    /**
     * Called by the simulator framework to set the state.
     */
    protected void setState(final SimulatorStateContainer simulatorState) {
        this.state = simulatorState;
    }

    /**
     * Gets a container where message handlers can store and retrieve any kind of data.
     */
    public SimulatorStateContainer getState() {
        return state;
    }
}