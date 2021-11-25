package com.tuya.connector.spring.annotations.interceptor;

import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.model.Result;
import com.tuya.connector.spring.annotations.ConnectorScan;
import com.tuya.connector.spring.core.ConnectorFactoryBean;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 4:36 下午
 */
public class InterceptorTest {
    static AnnotationConfigApplicationContext ctx;
    static InterceptorAbility ability;

    @BeforeAll
    static void init() {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(InterceptorA1.class);
        ctx.register(InterceptorA2.class);
        ctx.register(InterceptorConfig.class);

        ctx.refresh();
        ctx.start();
        ability = ctx.getBean(InterceptorConnector.class);
    }

    @Test
    void interceptorTest() {
        HashMap map = new HashMap();
        map.put("msg", "12314125");
//        Object result = ability.getWithBody();
//        System.out.println(result);
//        HashMap map1 = new HashMap();
//        map1.put("1", "1");
        Result result1 = new Result();
        result1.setCode(11111);
        Object result2 = ability.deleteWithBody(result1);
        System.out.println(result2);
//        Assertions.assertEquals(result, true);
    }

    @org.springframework.context.annotation.Configuration
    @ConnectorScan(basePackages = "com.tuya.connector.spring.annotations.interceptor")
    static class InterceptorConfig {
        @Bean
        public ConnectorFactoryBean connectorFactoryBean(Configuration configuration) {
            return new ConnectorFactoryBean(configuration);
        }

        @Bean
        public Configuration configuration(ApiDataSource apiDataSource) {
            return new Configuration(apiDataSource);
        }

        @Bean
        public ApiDataSource dataSource() {
            ApiDataSource.ApiDataSourceBuilder builer = ApiDataSource.DEFAULT_BUILDER;
            builer.baseUrl("http://localhost:8080");
            return builer.build();
        }
    }
}
