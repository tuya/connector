package com.tuya.connector.api.context;

import com.tuya.connector.api.config.ApiDataSource;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/29 1:38 下午
 */

public interface Context {
    /**
     * API Datasource
     * @return
     */
    ApiDataSource getApiDataSource();

    /**
     * support multi-language
     * @return
     */
    String getLang();
}
