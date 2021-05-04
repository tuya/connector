package com.tuya.connector.spring.annotations.errorprocessor;

import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.context.Context;
import com.tuya.connector.api.error.ErrorInfo;
import com.tuya.connector.api.error.ErrorProcessor;
import com.tuya.connector.api.plugin.Invocation;
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
 * @since 2021/3/29 9:48 下午
 */
public class ErrorProcessorTest {
    static AnnotationConfigApplicationContext ctx;
    static ErrorProcessorConnector connector;

    @BeforeAll
    static void init() {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(ErrorProcessorConfig.class);
        ctx.refresh();
        ctx.start();
        connector = ctx.getBean(ErrorProcessorConnector.class);
    }

    @Test
    void errorTest() {
        Boolean result = connector.get();
        Assertions.assertEquals(result, true);
    }

    @org.springframework.context.annotation.Configuration
    @ConnectorScan(basePackages = "com.tuya.connector.spring.annotations.errorprocessor")
    static class ErrorProcessorConfig {
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

        @Bean
        public ErrorProcessor aaaErrorProcessor() {
            return new ErrorProcessor() {
                @Override
                public Object process(ErrorInfo errorInfo, Invocation invocation, Context context) {
                    return null;
                }

                @Override
                public String getErrorCode() {
                    return "AAA";
                }
            };
        }

        @Bean
        public ErrorProcessor bbbErrorProcessor() {
            return new ErrorProcessor() {
                @Override
                public Object process(ErrorInfo errorInfo, Invocation invocation, Context context) {
                    return null;
                }

                @Override
                public String getErrorCode() {
                    return "BBB";
                }
            };
        }
    }
}
