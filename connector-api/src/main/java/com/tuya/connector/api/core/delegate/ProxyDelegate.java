package com.tuya.connector.api.core.delegate;

import java.lang.reflect.Method;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/21 10:23 上午
 */
public interface ProxyDelegate {

    /**
     * the true executor for proxy connector
     * @param method
     * @param args
     * @return
     */
    Object execute(Method method, Object[] args);
}
