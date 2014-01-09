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
package com.codebullets.external.party.simulator.connections;

import com.codebullets.external.party.simulator.pipeline.ConnectionEstablishedWorkItem;
import com.codebullets.external.party.simulator.pipeline.MessageWorkItem;
import com.codebullets.external.party.simulator.worker.WorkerQueue;

import javax.inject.Inject;

/**
 * Callback monitor for service connections.
 */
public class ServiceConnectionMonitor implements ConnectionMonitor {
    private final WorkerQueue workerQueue;

    /**
     * Generates a new instance of ServiceConnectionMonitor.
     */
    @Inject
    public ServiceConnectionMonitor(final WorkerQueue workerQueue) {
        this.workerQueue = workerQueue;
    }

    @Override
    public void connectionEstablished(final ConnectionContext context) {
        workerQueue.add(new ConnectionEstablishedWorkItem(context));
    }

    @Override
    public void messageReceived(final MessageWorkItem message) {
        workerQueue.add(message);
    }
}