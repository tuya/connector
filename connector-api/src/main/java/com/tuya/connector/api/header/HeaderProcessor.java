package com.tuya.connector.api.header;

import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.model.HttpRequest;

import java.net.URL;
import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/3 4:53 下午
 */
public interface HeaderProcessor {

    /**
     * build header map from different url
     * @param request current request
     * @return
     */
    Map<String, String> value(HttpRequest request);

    /**
     * header sign
     * @param content
     * @return
     */
    String sign(String content);

    /**
     * connector configuration
     * @return
     */
    Configuration getConfiguration();
}
