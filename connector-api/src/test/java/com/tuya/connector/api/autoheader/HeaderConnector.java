package com.tuya.connector.api.autoheader;

import com.tuya.connector.api.annotations.GET;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 8:35 下午
 */
public interface HeaderConnector extends HeaderAbility{

    @Override
    @GET("/test/autosetheader/enable")
    String enable();

}
