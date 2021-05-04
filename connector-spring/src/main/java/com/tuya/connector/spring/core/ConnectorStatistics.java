package com.tuya.connector.spring.core;

import java.util.HashSet;
import java.util.Set;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/20 5:28 下午
 */
public class ConnectorStatistics {

    private static final Set<Class<?>> CONNECTOR_SET = new HashSet<>();

    public static void mark(Class<?> connector) {
        CONNECTOR_SET.add(connector);
    }

    public static Set<Class<?>> getConnectorSet() {
        return CONNECTOR_SET;
    }
}
