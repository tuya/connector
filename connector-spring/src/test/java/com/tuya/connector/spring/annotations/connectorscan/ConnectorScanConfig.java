package com.tuya.connector.spring.annotations.connectorscan;

import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.core.ConnectorFactory;
import com.tuya.connector.api.core.DefaultConnectorFactory;
import com.tuya.connector.spring.annotations.ConnectorScan;
import org.springframework.context.annotation.Bean;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 3:51 下午
 */
@org.springframework.context.annotation.Configuration
@ConnectorScan(basePackages = "com.tuya.connector.spring.annotations.connectorscan")
public class ConnectorScanConfig {

    @Bean
    public ConnectorFactory connectorFactory(Configuration configuration) {
        configuration.init();
        return new DefaultConnectorFactory(configuration);
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
