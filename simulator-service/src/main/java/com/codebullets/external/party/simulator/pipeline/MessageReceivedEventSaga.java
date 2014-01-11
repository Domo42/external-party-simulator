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

import com.codebullets.sagalib.AbstractSingleEventSaga;
import com.codebullets.sagalib.StartsSaga;

import javax.inject.Inject;

/**
 * Message has been received. Handle it by calling the matching scripts.
 */
public class MessageReceivedEventSaga extends AbstractSingleEventSaga {
    private final ScriptPipeline scriptPipeline;

    /**
     * Generates a new instance of MessageReceivedEventSaga.
     */
    @Inject
    public MessageReceivedEventSaga(final ScriptPipeline scriptPipeline) {
        this.scriptPipeline = scriptPipeline;
    }

    /**
     * Called as a message has been received or partly processed.
     */
    @StartsSaga
    public void messageReceived(final MessageReceivedEvent message) {
        scriptPipeline.handle(message);
    }
}