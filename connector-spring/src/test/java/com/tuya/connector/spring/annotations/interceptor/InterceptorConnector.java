package com.tuya.connector.spring.annotations.interceptor;

import com.tuya.connector.api.annotations.Body;
import com.tuya.connector.api.annotations.DELETE;
import com.tuya.connector.api.annotations.GET;
import com.tuya.connector.api.annotations.Query;

import java.util.HashMap;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 4:41 下午
 */
public interface InterceptorConnector extends InterceptorAbility {
    @GET("/test/spring/interceptor/get")
    Boolean get();

    @DELETE("/test/springboot/deleteNoBody")
    Boolean deleteNoBody(@Query("num") Integer num);

    @DELETE("/test/springboot/deleteWithBody")
    Boolean deleteWithBody(@Body HashMap map);
}
