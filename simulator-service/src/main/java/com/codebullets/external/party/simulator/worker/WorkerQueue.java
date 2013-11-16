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

/**
 * FIFO Worker queue where items can be added or taken to be processed.
 */
public interface WorkerQueue {
    /**
     * Add an item into the worker queue.
     */
    void add(WorkItem item);

    /**
     * Takes an item from the worker queue. Blocks if no item present.
     * @throws InterruptedException in case waiting is interrupted.
     */
    WorkItem take() throws InterruptedException;
}