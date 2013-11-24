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
package com.codebullets.external.party.simulator.config;

import com.google.common.base.StandardSystemProperty;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Returns service configuration based on system properties and defaults.
 */
public class ServiceConfig implements Config {
    /**
     * {@inheritDoc}
     */
    @Override
    public Path connectionsPath() {
        Path connectionsPath;

        String configVal = System.getProperty("simulator.connections");
        if (configVal != null) {
            connectionsPath = Paths.get(configVal);
        } else {
            connectionsPath = Paths.get(StandardSystemProperty.USER_DIR.value());
        }

        return connectionsPath;
    }
}