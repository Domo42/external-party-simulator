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
import com.codebullets.external.party.simulator.worker.WorkerQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * Watches the script folder for changes. If something has changed
 * triggers an internal event to reset the script pipeline.
 */
public class ScriptReloadWatcher implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(ScriptReloadWatcher.class);

    private final WorkerQueue workerQueue;
    private final Map<WatchKey, Path> watchedDirectories = new HashMap<>();
    private WatchService watchService;
    private Thread watcherEventThread;

    /**
     * Generates a new instance of ScriptReloadWatcher.
     * @throws IOException Thrown if watcher in unable to watch the local
     *                     filesystem for changes.
     */
    @Inject
    public ScriptReloadWatcher(final Config config, final WorkerQueue workerQueue) throws IOException {
        this.workerQueue = workerQueue;
        watchService = FileSystems.getDefault().newWatchService();
        registerAll(config.handlersPath());

        watcherEventThread = new Thread(new WatcherThread(), "HandlersDirectoryWatcher");
        watcherEventThread.start();
    }

    /**
     * Register watcher for given path and all sub directories.
     * @throws IOException Thrown in case watcher can not be registered.
     */
    private void registerAll(final Path root) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
                    throws IOException {
                registerDirectory(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Register a watcher for a single file path.
     * @throws IOException Thrown in case watcher can not be registered.
     */
    private void registerDirectory(final Path dir) throws IOException {
        WatchKey watchKey = dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        watchedDirectories.put(watchKey, dir);
    }

    /**
     * Is called whenever something changed in the the scripting directory.
     */
    private void onFileWatchEventTriggered(final WatchKey key) {
        Path sourcePath = watchedDirectories.get(key);
        if (sourcePath != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind eventKind = event.kind();

                // events from watcher are always path events
                WatchEvent<Path> pathEvent = cast(event);
                Path changedElement = sourcePath.resolve(pathEvent.context());

                LOG.trace("Event of kind {} happened for {}", eventKind.name(), changedElement);
                if (eventKind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(changedElement)) {
                            registerAll(changedElement);
                        }
                    } catch (Exception ex) {
                        LOG.warn("Error registering watcher for {}", changedElement);
                    }
                }
            }

            // always trigger changed event
            workerQueue.add(new ScriptsChangedEvent());
        }

        boolean isValid = key.reset();
        if (!isValid) {
            watchedDirectories.remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(final WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Close internal resources.
     */
    @Override
    public void close() {
        if (watcherEventThread != null) {
            watcherEventThread.interrupt();
            watcherEventThread = null;
        }

        watchedDirectories.clear();

        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                LOG.warn("Error closing file watch service", e);
            }

            watchService = null;
        }
    }

    /**
     * Waits for events in the watch service and forwards them
     * to watcher class.
     */
    private class WatcherThread implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    WatchKey key = watchService.take();
                    if (key != null) {
                        onFileWatchEventTriggered(key);
                    }
                }
            } catch (InterruptedException e) {
                LOG.debug("Thread has been stopped by parent");
            }
        }
    }
}