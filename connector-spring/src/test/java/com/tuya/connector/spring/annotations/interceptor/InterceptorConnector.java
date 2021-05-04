package com.tuya.connector.spring.annotations.interceptor;

import com.tuya.connector.api.annotations.GET;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 4:41 下午
 */
public interface InterceptorConnector extends InterceptorAbility{
    @GET("/test/spring/interceptor/get")
    Boolean get();
}
