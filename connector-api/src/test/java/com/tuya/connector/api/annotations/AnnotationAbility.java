package com.tuya.connector.api.annotations;

import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 6:45 下午
 */
public interface AnnotationAbility {

    Boolean get();

    Boolean post();

    Boolean put();

    Boolean delete();

    String path(String pathParam);

    String query(String param);

    String queryMap(Map<String, Object> map);

    String url(String url);

    String headers();

    String header(String header);

    String headerMap(Map<String, String> headerMap);

    String body(Object body);

}
