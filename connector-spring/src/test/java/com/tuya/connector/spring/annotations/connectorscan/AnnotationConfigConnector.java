package com.tuya.connector.spring.annotations.connectorscan;

import com.tuya.connector.api.annotations.GET;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 3:55 下午
 */
public interface AnnotationConfigConnector extends AnnotationConfigAbility{

    @GET("/test/spring/annotations/get")
    Boolean get();
}
