package com.tuya.connector.spring.boot.autoconfigure;


import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.config.Logging;
import com.tuya.connector.api.context.ContextManager;
import com.tuya.connector.api.core.ConnectorFactory;
import com.tuya.connector.api.exceptions.ConnectorException;
import com.tuya.connector.api.header.HeaderProcessor;
import com.tuya.connector.api.token.TokenManager;
import com.tuya.connector.spring.core.ConnectorFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/25 5:51 下午
 */

@Slf4j
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ConnectorFactoryBean.class, ConnectorFactory.class})
@EnableConfigurationProperties(ConnectorProperties.class)
public class ConnectorAutoConfiguration implements InitializingBean, ApplicationContextAware {

    private static ApplicationContext ctx;

    private final ConnectorProperties connectorProperties;

    public ConnectorAutoConfiguration(ConnectorProperties connectorProperties) {
        this.connectorProperties = connectorProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    public ConnectorFactoryBean connectorFactoryBean(Configuration configuration) {
        ConnectorFactoryBean factory = new ConnectorFactoryBean();
        factory.setConfiguration(configuration);
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean
    public Configuration configuration() {
        Configuration configuration = new Configuration();
        ApiDataSource apiDataSource = ApiDataSource.DEFAULT_BUILDER.build();
        configuration.setApiDataSource(dataSource(apiDataSource, configuration));
        log.info("Connector configuration : {}", configuration);
        return configuration;
    }

    @Override
    public void afterPropertiesSet() {
        if (Objects.isNull(connectorProperties) || Objects.isNull(connectorProperties.getApi())) {
            throw new ConnectorException("Connector datasource must be required!");
        }
    }

    private ApiDataSource dataSource(ApiDataSource apiDataSource, Configuration configuration) {
        ApiProperties apiProperties = connectorProperties.getApi();
        apiDataSource.setBaseUrl(apiProperties.getBaseUrl());
        if (Objects.nonNull(apiProperties.getAk())) {
            apiDataSource.setAk(apiProperties.getAk());
        }
        if (Objects.nonNull(apiProperties.getSk())) {
            apiDataSource.setSk(apiProperties.getSk());
        }
        if (Objects.nonNull(apiProperties.getTimeout())) {
            apiDataSource.setTimeout(apiProperties.getTimeout());
        }
        if (Objects.nonNull(apiProperties.getPool())) {
            apiDataSource.setConnectionPool(apiProperties.getPool());
        }
        apiDataSource.setLoggingLevel(loggingLevel());
        apiDataSource.setLoggingStrategy(loggingStrategy());
        apiDataSource.setAutoSetHeader(apiProperties.isAutoSetHeader());
        if (Objects.nonNull(apiProperties.getHeaderProcessor())) {
            registerBean(apiProperties.getHeaderProcessor(), configuration);
            HeaderProcessor headerProcessor = ctx.getBean(apiProperties.getHeaderProcessor());
            apiDataSource.setHeaderProcessor(headerProcessor);
        }
        if (Objects.nonNull(apiProperties.getTokenManager())) {
            registerBean(apiProperties.getTokenManager(), configuration);
            TokenManager tokenManager = ctx.getBean(apiProperties.getTokenManager());
            apiDataSource.setTokenManager(tokenManager);
        }
        if (Objects.nonNull(apiProperties.getContextManager())) {
            registerBean(apiProperties.getContextManager(), configuration);
            ContextManager contextManager = ctx.getBean(apiProperties.getContextManager());
            apiDataSource.setContextManager(contextManager);
        }
        apiDataSource.setAutoRefreshToken(apiProperties.isAutoRefreshToken());

        return apiDataSource;
    }

    private void registerBean(Class<?> clz, Configuration configuration) {
        GenericBeanDefinition beanDef = new GenericBeanDefinition(BeanDefinitionBuilder.genericBeanDefinition(clz).getBeanDefinition());
        beanDef.getConstructorArgumentValues().addGenericArgumentValue(configuration);
        ((GenericApplicationContext)ctx).registerBeanDefinition(clz.getName(), beanDef);
    }

    private Logging.Level loggingLevel() {
        String logLevel;
        LoggingProperties loggingProperties = connectorProperties.getApi().getLogging();
        if (Objects.isNull(loggingProperties) || StringUtils.isEmpty(loggingProperties.getLevel())) {
            logLevel = LoggingProperties.DEFAULT_LEVEL;
        } else {
            logLevel = loggingProperties.getLevel();
        }
        return Logging.Level.valueOf(logLevel.toUpperCase());
    }
    private Logging.Strategy loggingStrategy() {
        String logStrategy;
        LoggingProperties loggingProperties = connectorProperties.getApi().getLogging();
        if (Objects.isNull(loggingProperties) || StringUtils.isEmpty(loggingProperties.getStrategy())) {
            logStrategy = LoggingProperties.DEFAULT_STRATEGY;
        } else {
            logStrategy = loggingProperties.getStrategy();
        }
        return Logging.Strategy.valueOf(logStrategy.toUpperCase());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}
