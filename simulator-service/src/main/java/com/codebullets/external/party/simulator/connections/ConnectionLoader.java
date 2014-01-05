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

import com.codebullets.external.party.simulator.config.Config;
import com.google.common.collect.Iterables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads connections configured from groovy script configuration.
 */
public class ConnectionLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionLoader.class);
    private final Config config;
    private final ConnectionMonitor connectionMonitor;
    private final ConnectionsContainer connectionsContainer;

    /**
     * Generates a new instance of ConnectionLoader.
     */
    @Inject
    public ConnectionLoader(final Config config, final ConnectionMonitor connectionMonitor, final ConnectionsContainer connectionsContainer) {
        this.config = config;
        this.connectionMonitor = connectionMonitor;
        this.connectionsContainer = connectionsContainer;
    }

    /**
     * Loads all connections from configuration and starts them.
     */
    public void startAllConnections() {
        Iterable<Path> connectionSettings = findAllConnectionSettings();

        ConnectionConfigurator connectionConfigurator = new ConnectionConfigurator();
        Iterable<ConnectionConfig> connectionConfigs = connectionConfigurator.readConnections(connectionSettings);
        if (Iterables.isEmpty(connectionConfigs)) {
            LOG.warn("No connection setting found. Please make sure the configuration is in either the connections sub folder or "
                + "the 'simulator.connections' system property points to the target location.");
        }

        printConfig(connectionConfigs);

        for (ConnectionConfig connectionConfig : connectionConfigs) {
            startConnection(connectionConfig);
        }
    }

    private void startConnection(final ConnectionConfig connectionConfig) {
        try {
            connectionConfig.getConnection().setMonitor(connectionMonitor);
            connectionConfig.getConnection().start(connectionConfig);

            connectionsContainer.put(connectionConfig.getName(), connectionConfig.getConnection());
        } catch (Exception ex) {
            LOG.error("Failed to start connection {}", connectionConfig.getName(), ex);
        }
    }

    private void printConfig(final Iterable<ConnectionConfig> connectionSettings) {
        for (ConnectionConfig settings : connectionSettings) {
            LOG.debug("Found connection entry named {}", settings.getName());
        }
    }

    /**
     * Returns the list of connection definition files.
     */
    private Iterable<Path> findAllConnectionSettings() {
        List<Path> connectionFiles = new ArrayList<>();
        Path connectionsDir = config.connectionsPath();
        LOG.info("Search for files in directory " + connectionsDir);

        try (DirectoryStream<Path> pathEntries = Files.newDirectoryStream(connectionsDir, "*.groovy")) {
            for (Path entry : pathEntries) {
                connectionFiles.add(entry);
            }
        } catch (IOException e) {
            LOG.warn("Error reading files from connections directory: " + e.getMessage());
        }

        return connectionFiles;
    }
}