package com.tuya.connector.api.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/25 4:25 下午
 */
public class Plugin implements InvocationHandler {

    private final Object target;
    private final ConnectorInterceptor interceptor;

    public Plugin(Object target, ConnectorInterceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return interceptor.intercept(new Invocation(target, method, args));
        } catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getCause();
        }
    }
}
