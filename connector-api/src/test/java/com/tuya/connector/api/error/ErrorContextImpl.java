package com.tuya.connector.api.error;

import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.context.Context;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 11:04 上午
 */
public class ErrorContextImpl implements Context {

    private final Configuration configuration;

    public ErrorContextImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ApiDataSource getApiDataSource() {
        return configuration.getApiDataSource();
    }

    @Override
    public String getLang() {
        return "zh";
    }
}
