package com.tuya.connector.api.error;

import com.tuya.connector.api.annotations.GET;
import com.tuya.connector.api.annotations.Path;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 12:18 下午
 */
public interface ErrorProcessorConnector extends ErrorProcessorAbility {

    @Override
    @GET("/test/errorprocessor/success")
    Boolean success();

    @Override
    @GET("/test/errorprocessor/error/{code}")
    Boolean error(@Path("code") int code);

}
