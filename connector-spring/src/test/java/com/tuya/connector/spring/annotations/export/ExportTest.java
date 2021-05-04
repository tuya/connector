package com.tuya.connector.spring.annotations.export;

import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.spring.annotations.ConnectorScan;
import com.tuya.connector.spring.core.ConnectorFactoryBean;
import com.tuya.connector.spring.export.ExportConfiguration;
import com.tuya.connector.spring.export.ExportProxyInjector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/7 11:30 上午
 */
public class ExportTest {

    static AnnotationConfigApplicationContext ctx;

    @BeforeAll
    static void init() {
        ctx = new AnnotationConfigApplicationContext(ExportConfig.class);
        ctx.start();
        ctx.register(ExportProxyInjector.class);
        ctx.getBean(ExportProxyInjector.class);
    }

    @Test
    void exportTest() {
        ExportConnector connector = ctx.getBean(ExportConnector.class);
        Boolean result = connector.get();
        Assertions.assertEquals(result, true);
    }

    @org.springframework.context.annotation.Configuration
    @ConnectorScan(basePackages = "com.tuya.connector.spring.annotations.export")
    static class ExportConfig{
        @Bean
        public ConnectorFactoryBean connectorFactoryBean(Configuration configuration) {
            return new ConnectorFactoryBean(configuration);
        }

        @Bean
        public Configuration configuration(ApiDataSource apiDataSource) {
            return new Configuration(apiDataSource);
        }

        @Bean
        public ExportConfiguration exportConfiguration() {
            return ExportConfiguration.builder().autoExport(true).build();
        }

        @Bean
        public ApiDataSource dataSource() {
            ApiDataSource.ApiDataSourceBuilder builer = ApiDataSource.DEFAULT_BUILDER;
            builer.baseUrl("http://localhost:8080");
            return builer.build();
        }
    }
}
