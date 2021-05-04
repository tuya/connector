package com.tuya.connector.api.annotations;

import com.alibaba.fastjson.JSON;
import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.config.Logging;
import com.tuya.connector.api.core.ConnectorFactory;
import com.tuya.connector.api.core.DefaultConnectorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 6:56 下午
 */
public class AnnotationTest {

    static AnnotationAbility ability;

    @BeforeAll
    static void init() {
        Configuration config = new Configuration();
        ApiDataSource apiDataSource = ApiDataSource.DEFAULT_BUILDER.build();
        apiDataSource.setBaseUrl("http://localhost:8080");
        apiDataSource.setLoggingStrategy(Logging.Strategy.BODY);
        config.setApiDataSource(apiDataSource);

        ConnectorFactory connectorFactory = new DefaultConnectorFactory(config);
        ability = connectorFactory.loadConnector(AnnotationConnector.class);
    }

    @Test
    void getTest() {
        Boolean get = ability.get();
        assertEquals(get, true);
    }

    @Test
    void postTest() {
        Boolean post = ability.post();
        assertEquals(post, true);
    }

    @Test
    void putTest() {
        Boolean put = ability.put();
        assertEquals(put, true);
    }

    @Test
    void deleteTest() {
        Boolean delete = ability.delete();
        assertEquals(delete, true);
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaa","bbb"})
    void pathTest(String param) {
        String path = ability.path(param);
        assertEquals(path, param);
    }

    @Test
    void queryTest() {
        String queryParam = ability.query("QueryParam");
        assertEquals(queryParam, "QueryParam");
    }

    @Test
    void queryMapTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("param1", "value1");
        map.put("param2", "value2");
        String queryMap = ability.queryMap(map);
        assertEquals(JSON.toJSONString(map), queryMap);
    }

    @Test
    void urlTest() {
        String url = ability.url("http://localhost:8080/test/annotations/url");
        assertEquals("/url", url);
    }

    @Test
    void headersTest() {
        String headers = ability.headers();
        assertEquals(headers, "headerValue");
    }

    @Test
    void headerTest() {
        String headerValue = ability.header("headerValue");
        assertEquals(headerValue, "headerValue");
    }

    @Test
    void headerMapTest() {
        Map<String, String> map = new HashMap<>();
        map.put("headerKey", "headerValue");
        String headerValue = ability.headerMap(map);
        assertEquals("headerValue", headerValue);
    }


    @Test
    void bodyTest() {
        Map<String, String> map = new HashMap<>();
        map.put("param1", "value1");
        map.put("param2", "value2");
        String body = ability.body(map);
        assertEquals(body, JSON.toJSONString(map));
    }



}
