package com.tuya.connector.spring.annotations.interceptor;

import com.tuya.connector.api.plugin.ConnectorInterceptor;
import com.tuya.connector.api.plugin.Invocation;
import com.tuya.connector.spring.annotations.Interceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 4:37 下午
 */
@Slf4j
@Interceptor(priority = 1)
public class InterceptorA1 implements ConnectorInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("###### before {}", this.getClass().getName());
        Object result;
        result = invocation.proceed();
        log.info("###### after {}", this.getClass().getName());
        return result;
    }
}
