package com.tuya.connector.spring.annotations.interceptor;

import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.spring.annotations.ConnectorScan;
import com.tuya.connector.spring.core.ConnectorFactoryBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

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
        Boolean result = ability.deleteNoBody(1);
        Assertions.assertEquals(result, true);
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
