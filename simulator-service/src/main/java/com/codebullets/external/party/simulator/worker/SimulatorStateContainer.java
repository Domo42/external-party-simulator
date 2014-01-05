/*
 * Copyright 2014 Stefan Domnanovits
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

import java.util.HashMap;
import java.util.Map;

/**
 * Contains data available to all simulation handler. The data is stored
 * in the memory of the server. Every handler can put and retrieve data here.<br/>
 * This is necessary because internal handler state will not be persisted anywhere
 * once the message handling is finished.
 */
public class SimulatorStateContainer {
    private Map<String, Object> state = new HashMap<>();

    /**
     * Gets the value of the specified key. If no value is set it returns null.
     */
    public Object get(final String key) {
        return state.get(key);
    }

    /**
     * Saves a value with a specific key.
     */
    public void put(final String key, final Object value) {
        state.put(key, value);
    }

    /**
     * Returns true if a value for a specific key is present; otherwise false.
     */
    public boolean containsKey(final String key) {
        return state.containsKey(key);
    }
}