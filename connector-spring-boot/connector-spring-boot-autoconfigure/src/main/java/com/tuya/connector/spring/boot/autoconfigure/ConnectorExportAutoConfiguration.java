package com.tuya.connector.spring.boot.autoconfigure;

import com.tuya.connector.spring.export.ExportConfiguration;
import com.tuya.connector.spring.export.ExportProxyInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/7 2:39 下午
 */
@Slf4j
@org.springframework.context.annotation.Configuration
@AutoConfigureAfter(ConnectorAutoConfiguration.class)
@ConditionalOnClass({ConnectorAutoConfiguration.class, ExportProxyInjector.class})
@EnableConfigurationProperties(ConnectorProperties.class)
public class ConnectorExportAutoConfiguration {

    private final ConnectorProperties connectorProperties;

    public ConnectorExportAutoConfiguration(ConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ExportProxyInjector exportProxyInjector(ExportConfiguration exportConfiguration) {
        return new ExportProxyInjector(exportConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExportConfiguration exportConfiguration() {
        return ExportConfiguration.builder().autoExport(connectorProperties.isAutoExport()).build();
    }

}
