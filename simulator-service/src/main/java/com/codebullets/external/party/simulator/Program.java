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
package com.codebullets.external.party.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main program entry point.
 */
public final class Program {
    private static final Logger LOG = LoggerFactory.getLogger(Program.class);
    private static final long SLEEP_TIME = 500;

    /**
     * Prevent instance of this class.
     */
    private Program() {
    }

    /**
     * Main program entry point.
     * @throws InterruptedException In case of thread stop.
     */
    public static void main(final String [] args) throws InterruptedException {
        LOG.info("Starting simulator service");

        while (true) {
            Thread.sleep(SLEEP_TIME);
        }
    }
}