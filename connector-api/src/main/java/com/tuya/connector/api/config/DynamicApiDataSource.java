package com.tuya.connector.api.config;

import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 3:21 下午
 */
public class DynamicApiDataSource extends ApiDataSource {

    private Map<String, ApiDataSource> dataSourceMap;

    public ApiDataSource determineTargetDataSource() {
        return null;
    }

    public String determineCurrentLookupKey() {
        return null;
    }

    public ApiDataSource defaultDataSource() {
        return null;
    }

}
