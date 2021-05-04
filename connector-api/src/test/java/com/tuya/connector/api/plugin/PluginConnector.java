package com.tuya.connector.api.plugin;

import com.tuya.connector.api.annotations.GET;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 10:23 上午
 */
public interface PluginConnector extends PluginAbility {

    @Override
    @GET("test/plugin/hello")
    Boolean plugin();

}
