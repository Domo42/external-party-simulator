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
package com.codebullets.external.party.simulator.handlers;

import com.codebullets.external.party.simulator.connections.Connection;
import com.codebullets.external.party.simulator.connections.ConnectionMonitor;
import com.codebullets.external.party.simulator.startup.StartUpWorkItem;
import com.codebullets.sagalib.AbstractSingleEventSaga;
import com.codebullets.sagalib.StartsSaga;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Perform service startup.
 */
public class ServiceStartupSaga extends AbstractSingleEventSaga {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceStartupSaga.class);
    private final ConnectionMonitor monitor;

    /**
     * Generates a new instance of ServiceStartupSaga.
     */
    @Inject
    public ServiceStartupSaga(final ConnectionMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * Called when the service starts up.
     */
    @StartsSaga
    public void start(final StartUpWorkItem message) {
        LOG.info("starting connections");

        ServiceLoader<Connection> connectionsLoader = ServiceLoader.load(Connection.class);
        List<Connection> connections = Lists.newArrayList(connectionsLoader.iterator());

        if (!connections.isEmpty()) {
            for (Connection connection : connectionsLoader) {
                LOG.debug("Starting connection {}", connection);
                connection.setMonitor(monitor);
                connection.start();
            }
        } else {
            LOG.warn("No connections found.");
        }
    }
}