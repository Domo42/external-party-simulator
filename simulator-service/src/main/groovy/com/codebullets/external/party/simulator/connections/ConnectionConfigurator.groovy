package com.codebullets.external.party.simulator.connections

import org.codehaus.groovy.control.CompilerConfiguration

import java.nio.file.Path

public class ConnectionConfigurator {
    /**
     * Reads third party connection configuration files.
     */
    public Iterable<ConnectionConfig> readConnections(final Iterable<Path> files) {
        def connectionSettings = [];

        for (file in files) {
            ConnectionConfig[] configs = readConnectionFile(file)
            connectionSettings.addAll(configs);
        }

        return connectionSettings
    }

    /**
     * Reads the connection elements from single file.
     */
    private ConnectionConfig[] readConnectionFile(Path file) {
        Binding binding = new Binding()
        def configuration = new CompilerConfiguration();

        def scriptText = file.toFile().text
        def connectionScript = new GroovyShell(binding, configuration).parse(scriptText)
        connectionScript.metaClass.mixin(ConnectionRoot)
        connectionScript.run()

        def connectionSettings = connectionScript.configs
        return connectionSettings
    }
}
