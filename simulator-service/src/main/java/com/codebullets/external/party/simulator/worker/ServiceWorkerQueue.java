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
package com.codebullets.external.party.simulator.worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Holds the work items for the simulator service.
 */
public class ServiceWorkerQueue implements WorkerQueue {
    private final BlockingQueue<EventItem> eventItems = new LinkedBlockingQueue<>();
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final EventItem item) {
        eventItems.add(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDelayed(final EventItem item, final long delay, final TimeUnit unit) {
        executor.schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        eventItems.add(item);
                    }
                },
                delay,
                unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventItem take() throws InterruptedException {
        return eventItems.take();
    }
}