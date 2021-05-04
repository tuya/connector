package com.tuya.connector.spring.annotations.errorprocessor;

import com.tuya.connector.api.annotations.GET;

/**
 * <p> TODO
 *
 * @author @author qiufeng.yu@tuya.com
 * @since 2021/3/29 9:50 下午
 */
public interface ErrorProcessorConnector {
    @GET("/test/spring/errorprocessor/get")
    Boolean get();
}
