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

import com.codebullets.external.party.simulator.connections.ConnectionLoader;
import com.codebullets.external.party.simulator.connections.ConnectionMonitor;
import com.codebullets.sagalib.AbstractSingleEventSaga;
import com.codebullets.sagalib.StartsSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Perform service startup.
 */
public class ServiceStartupSaga extends AbstractSingleEventSaga {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceStartupSaga.class);
    private final ConnectionMonitor monitor;
    private final ConnectionLoader loader;

    /**
     * Generates a new instance of ServiceStartupSaga.
     */
    @Inject
    public ServiceStartupSaga(final ConnectionMonitor monitor, final ConnectionLoader loader) {
        this.monitor = monitor;
        this.loader = loader;
    }

    /**
     * Called when the service starts up.
     */
    @StartsSaga
    public void start(final StartUpEventItem message) {
        LOG.info("starting connections");

        loader.startAllConnections();
    }
}