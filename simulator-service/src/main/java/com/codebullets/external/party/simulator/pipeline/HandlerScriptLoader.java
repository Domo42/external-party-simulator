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
package com.codebullets.external.party.simulator.pipeline;

import com.codebullets.external.party.simulator.config.Config;
import groovy.lang.GroovyClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads all groovy handler scripts.
 */
public class HandlerScriptLoader {
    private static final Logger LOG = LoggerFactory.getLogger(HandlerScriptLoader.class);

    private final Config config;

    /**
     * Generates a new instance of HandlerScriptLoader.
     */
    @Inject
    public HandlerScriptLoader(final Config config) {
        this.config = config;
    }

    /**
     * Loads all scripts and returns a list of matching handlers to be executed.
     */
    public Iterable<AbstractMessageHandler> loadMatchingSciptHandlers(final MessageWorkItem messageItem, final GroovyClassLoader classLoader) {
        List<AbstractMessageHandler> handlers = new ArrayList<>();

        Iterable<AbstractMessageHandler> allHandlers = allScriptHandlers(classLoader);
        for (AbstractMessageHandler handler : allHandlers) {
            if (isMatchingHandler(handler, messageItem.getContentType(), messageItem.getMessageType())) {
                handlers.add(handler);
            }
        }

        return handlers;
    }

    private boolean isMatchingHandler(final AbstractMessageHandler handler, final ContentType messageContentType, final String messageType) {
        boolean isMatch = false;

        if (messageContentType.equals(handler.getContentType())) {
            GlobMatcher matcher = new GlobMatcher(handler.messageType());
            if (matcher.isMatch(messageType)) {
                isMatch = true;
            }
        }

        return isMatch;
    }

    /**
     * Loads all possible script handlers.
     */
    private Iterable<AbstractMessageHandler> allScriptHandlers(final GroovyClassLoader classLoader) {
        List<AbstractMessageHandler> handlers = new ArrayList<>();
        List<Class> allClasses = new ArrayList<>();

        // add all groovy class into class loader
        Iterable<Path> groovyFiles = findAllGroovyFiles();
        for (Path file : groovyFiles) {
            try {
                Class scriptClass = classLoader.parseClass(file.toFile());
                allClasses.add(scriptClass);
            } catch (IOException e) {
                LOG.error("Error parsing groove file " + file.toString());
            }
        }

        // after all classes are available instantiate handlers
        for (Class clazz : allClasses) {
            if (AbstractMessageHandler.class.isAssignableFrom(clazz)) {
                try {
                    AbstractMessageHandler handler = (AbstractMessageHandler) clazz.newInstance();
                    handlers.add(handler);
                } catch (InstantiationException | IllegalAccessException e) {
                    LOG.error("Error creating handler instance {}.", clazz.getSimpleName(), e);
                }
            }
        }

        return handlers;
    }

    /**
     * Returns the list of connection definition files.
     */
    private Iterable<Path> findAllGroovyFiles() {
        final List<Path> handlerFiles = new ArrayList<>();
        Path startPath = config.handlersPath();
        LOG.debug("Search for handler files in directory " + startPath);

        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
                    if (file.toString().endsWith(".groovy")) {
                        handlerFiles.add(file);
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(final Path file, final IOException e) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            LOG.error("Error finding script files.", e);
        }

        return handlerFiles;
    }
}