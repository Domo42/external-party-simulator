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
package com.codebullets.external.party.simulator.startup;

import com.codebullets.external.party.simulator.worker.EventItem;
import com.codebullets.external.party.simulator.worker.WorkerQueue;
import com.codebullets.sagalib.MessageStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * The actual simulator service class.
 */
public class Service implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(Service.class);

    private final WorkerQueue workerQueue;
    private final MessageStream sagaLib;

    /**
     * Generates a new instance of Service.
     */
    @Inject
    public Service(final WorkerQueue workerQueue, final MessageStream sagaLib) {
        this.workerQueue = workerQueue;
        this.sagaLib = sagaLib;

        // indicate startup to self
        this.workerQueue.add(new StartUpEventItem());
    }

    /**
     * Service worker thread.
     */
    @Override
    public void run() {
        LOG.info("Service worker thread started.");

        boolean keepRunning = true;

        while (keepRunning) {
            EventItem item = null;

            try {
                item = workerQueue.take();
                sagaLib.handle(item);
            } catch (InterruptedException ex) {
                keepRunning = false;
                LOG.info("Worker thread has been stopped.");
            } catch (Exception ex) {
                LOG.warn("Error processing item {}.", item, ex);
            }
        }
    }
}