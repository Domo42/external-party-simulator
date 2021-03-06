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
package com.codebullets.external.party.simulator.pipeline;

/**
 * Handles a message based on content and type.
 */
public interface ScriptPipeline {
    /**
     * Resets internal state back to defaults.
     */
    void reset();

    /**
     * Handles a message by calling a list of matching handler scripts.
     */
    void handle(MessageReceivedEvent message);

    /**
     * Handles the established connection indication by calling a list of matching handler scripts.
     */
    void handle(ConnectionEstablishedEvent establishedEvent);
}