package com.tuya.connector.api.core;

import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.plugin.ConnectorInterceptor;
import com.tuya.connector.api.plugin.Plugin;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/18 4:42 下午
 */
@Slf4j
public class DefaultConnectorFactory implements ConnectorFactory {

    private final Configuration configuration;

    public DefaultConnectorFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T loadConnector(Class<T> connectorInterface) {
        Instant start = Instant.now();
        log.debug("Start loading connector[{}] at: {}", connectorInterface.getName(), start);
        ConnectorProxy<T> proxy = new ConnectorProxy<>(configuration, connectorInterface);
        T connectorProxy = (T) Proxy.newProxyInstance(connectorInterface.getClassLoader(), new Class[] {connectorInterface}, proxy);
        connectorProxy = wrapInterceptor(connectorInterface, connectorProxy);
        log.debug("Finish loading connector[{}] costs : {} ms", connectorInterface.getName(), Duration.between(start, Instant.now()).toMillis());
        return connectorProxy;
    }

    /**
     * Connector API interceptor wrapper
     */
    private <T> T wrapInterceptor(Class<T> connectorInterface, T connectorProxy) {
        List<T>  connectorProxys = new ArrayList<>(1);
        connectorProxys.add(connectorProxy);
        Collection<ConnectorInterceptor> interceptors = configuration.getInterceptors();
        if (interceptors != null && interceptors.size() > 0) {
            for (ConnectorInterceptor interceptor : interceptors) {
                log.debug("Start wrapping interceptor: {}", interceptor.getClass().getName());
                //noinspection unchecked
                connectorProxys.set (0, (T) Proxy.newProxyInstance(
                    connectorInterface.getClassLoader(),
                    new Class[]{connectorInterface},
                    new Plugin(connectorProxys.get(0), interceptor)
                ));
                log.debug("Finish wrapping interceptor: {}", interceptor.getClass().getName());
            }
        }
        return connectorProxys.get(0);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
