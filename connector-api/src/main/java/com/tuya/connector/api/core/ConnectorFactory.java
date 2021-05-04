package com.tuya.connector.api.core;

import com.tuya.connector.api.config.Configuration;

/**
 * <p> TODO 考虑线程安全
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/18 4:41 下午
 */
public interface ConnectorFactory {

    /**
     * load connector proxy from connector
     * @param connectorInterface
     * @param <T>
     * @return
     */
    <T> T loadConnector(Class<T> connectorInterface);

    /**
     * connector configuration
     * @return
     */
    Configuration getConfiguration();

}
