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

import com.codebullets.external.party.simulator.config.Config;
import com.codebullets.external.party.simulator.config.ServiceConfig;
import com.codebullets.external.party.simulator.connections.ConnectionLoader;
import com.codebullets.external.party.simulator.connections.ConnectionMonitor;
import com.codebullets.external.party.simulator.connections.ConnectionsContainer;
import com.codebullets.external.party.simulator.connections.ServiceConnectionMonitor;
import com.codebullets.external.party.simulator.connections.SimulatorConnectionsContainer;
import com.codebullets.external.party.simulator.pipeline.GroovyScriptPipeline;
import com.codebullets.external.party.simulator.pipeline.HandlerScriptLoader;
import com.codebullets.external.party.simulator.pipeline.ScriptPipeline;
import com.codebullets.external.party.simulator.worker.ServiceWorkerQueue;
import com.codebullets.external.party.simulator.worker.WorkerQueue;
import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Object bindings for the simulator service.
 */
public class ServiceModule extends AbstractModule {
    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        bind(Service.class).in(Singleton.class);
        bind(WorkerQueue.class).to(ServiceWorkerQueue.class).in(Singleton.class);
        bind(ConnectionsContainer.class).to(SimulatorConnectionsContainer.class).in(Singleton.class);
        bind(ConnectionMonitor.class).to(ServiceConnectionMonitor.class).in(Singleton.class);
        bind(ConnectionLoader.class).in(Singleton.class);
        bind(HandlerScriptLoader.class).in(Singleton.class);
        bind(ScriptPipeline.class).to(GroovyScriptPipeline.class).in(Singleton.class);
        bind(Config.class).to(ServiceConfig.class);
    }
}