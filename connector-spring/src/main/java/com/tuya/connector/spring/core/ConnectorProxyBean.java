package com.tuya.connector.spring.core;

import com.tuya.connector.api.core.ConnectorFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/26 2:10 下午
 */
public class ConnectorProxyBean<T> implements FactoryBean<T>, ApplicationContextAware, InitializingBean,
    ApplicationListener<ApplicationEvent> {

    public static String connectorFactoryName;
    private static ApplicationContext ctx;

    private Class<T> connector;
    private ConnectorFactory connectorFactory;
    private T connectorProxy;

    public ConnectorProxyBean(Class<T> connector) {
        this.connector = connector;
    }

    public void setConnectorFactory(ConnectorFactory connectorFactory) {
        this.connectorFactory = connectorFactory;
    }

    public void setConnector(Class<T> connector) {
        this.connector = connector;
    }

    @Override
    public T getObject() throws Exception {
        if (Objects.isNull(connectorFactory)) {
            afterPropertiesSet();
        }
        if (Objects.isNull(connectorProxy)) {
            connectorProxy = connectorFactory.loadConnector(connector);
        }
        return connectorProxy;
    }

    @Override
    public Class<T> getObjectType() {
        return this.connector;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.isNull(connectorFactory) && Objects.nonNull(ctx)) {
            connectorFactory = ctx.getBean(ConnectorFactory.class);
        }

        ConnectorStatistics.mark(connector);
    }


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            connectorProxy = connectorFactory.loadConnector(connector);
        }
    }
}
