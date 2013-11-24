package com.codebullets.external.party.simulator.connections

/***
 * Base class for ruby connection setting scripts
 */
class ConnectionRoot {
    List<ConnectionConfig> configs = []

    def connection(String connectionName, Class connectionClass, Closure closure = null) {
        ConnectionConfig config = new ConnectionConfig()
        config.name = connectionName
        config.connection = connectionClass.newInstance()

        ConnectionSettingDelegate setting = new ConnectionSettingDelegate()
        closure.delegate = setting
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()

        config.endpoint = setting.endpoint;
        config.timeout = setting.timeout;

        configs.add(config)
    }
}
