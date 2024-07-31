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

    public String originalPath() {
        List<String> pathSegments = httpUrl.pathSegments();
        if (pathSegments.isEmpty()) {
            return "";
        }
        StringBuilder p = new StringBuilder();
        for (String pathSegment : pathSegments) {
            p.append("/").append(pathSegment);
        }
        return p.toString();
    }

}
