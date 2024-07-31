package com.tuya.connector.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.HttpUrl;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author benguan.zhou@tuya.com
 * @description
 * @date 2021/06/24
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpRequest {
    String httpMethod;
    URL url;
    Map<String, List<String>> headers;
    byte[] body;
    HttpUrl httpUrl;

    private String orginalPath() {
        String p = httpUrl.scheme() + "://" + httpUrl.host();
        if (httpUrl.port() != 80 && httpUrl.port() != 443) {
            p += ":" + httpUrl.port();
        }
        List<String> pathSegments = httpUrl.pathSegments();
        for (String pathSegment : pathSegments) {
            p += "/" + pathSegment;
        }
        return p;
    }

}
