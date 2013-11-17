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

import static com.google.common.base.Preconditions.checkState;

/**
 * Information about a specific message that has been received.
 */
public final class MessageWorkItem implements WorkItem {
    private final ContentType contentType;
    private final String messageType;
    private final Object content;

    /**
     * Use static builder method to create instance.
     */
    private MessageWorkItem(final String  text, final String messageType) {
        contentType = ContentType.STRING;
        this.messageType = messageType;
        content = text;
    }

    /**
     * Gets the type of message that has been received.
     */
    public ContentType getContentType() {
        return contentType;
    }

    /**
     * Gets the text content in case message is of type text.
     */
    public String textContent() {
        checkState(contentType.equals(ContentType.STRING), "Message content needs to be of type STRING.");
        return (String) content;
    }

    /**
     * Gets the message instance type.
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Creates a message work item of type String.
     */
    public static MessageWorkItem create(final String text, final String msgType) {
        return new MessageWorkItem(text, msgType);
    }
}