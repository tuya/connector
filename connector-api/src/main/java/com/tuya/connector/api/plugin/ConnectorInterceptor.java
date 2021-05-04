package com.tuya.connector.api.plugin;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/25 3:12 下午
 */
@FunctionalInterface
public interface ConnectorInterceptor {

    /**
     * intercept connector method
     * @param invocation
     * @return
     * @throws Throwable
     */
    Object intercept(Invocation invocation) throws Throwable;

}
