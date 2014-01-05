package com.codebullets.external.party.simulator.pipeline;

/**
 * Type of a specific message.
 */
public enum ContentType {
    /**
     * Message content is a binary byte buffer.
     */
    BINARY,

    /**
     * Message content is a text string.
     */
    TEXT,

    /**
     * Message content is a Java object.
     */
    OBJECT
}