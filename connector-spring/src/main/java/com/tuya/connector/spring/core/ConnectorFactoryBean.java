package com.tuya.connector.spring.core;

import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.core.ConnectorFactory;
import com.tuya.connector.api.core.DefaultConnectorFactory;
import com.tuya.connector.api.error.ErrorProcessor;
import com.tuya.connector.api.error.ErrorProcessorRegister;
import com.tuya.connector.api.exceptions.ConnectorException;
import com.tuya.connector.api.plugin.ConnectorInterceptor;
import com.tuya.connector.spring.annotations.Interceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/26 11:38 上午
 */
@Slf4j
public class ConnectorFactoryBean implements FactoryBean<ConnectorFactory>, InitializingBean, BeanNameAware, ApplicationContextAware {

    private static ApplicationContext ctx;

    private Configuration configuration;
    private ConnectorFactory connectorFactory;

    public ConnectorFactoryBean() {
    }

    public ConnectorFactoryBean(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.isNull(configuration)) {
            log.warn("No connector configuration found, the default configuration will take effect");
        }
        if (Objects.isNull(configuration.getApiDataSource())) {
            throw new ConnectorException("Connector api datasource must be required!");
        }

        // register error processor
        registerErrorProcessor();

        configuration.init();

        // set interceptors sort by priority
        setInterceptors();
    }

    private void registerErrorProcessor() {
        Map<String, ErrorProcessor> errorProcessors = ctx.getBeansOfType(ErrorProcessor.class);
        if (CollectionUtils.isEmpty(errorProcessors)) {
            log.info("no error processor found");
            return;
        }
        ErrorProcessorRegister register = configuration.getApiDataSource().getErrorProcessorRegister();
        if (Objects.isNull(register)) {
            register = new ErrorProcessorRegister();
            configuration.getApiDataSource().setErrorProcessorRegister(register);
        }
        for (ErrorProcessor processor : errorProcessors.values()) {
            log.info("register processor for error code: {}", processor.getErrorCode());
            register.register(processor);
        }
    }

    private void setInterceptors() {
        Map<String, Object> interceptors = ctx.getBeansWithAnnotation(Interceptor.class);
        if (!CollectionUtils.isEmpty(interceptors)) {
            interceptors.values().stream().sorted(
                (o1, o2) -> {
                    if (o1 instanceof ConnectorInterceptor && o2 instanceof ConnectorInterceptor) {
                        ConnectorInterceptor ci1 = (ConnectorInterceptor) o1;
                        Class<?> original1 = ci1.getClass();
                        if (AopUtils.isCglibProxy(ci1)) {
                            original1 = ClassUtils.getUserClass(original1);
                        }
                        ConnectorInterceptor ci2 = (ConnectorInterceptor) o2;
                        Class<?> original2 = ci2.getClass();
                        if (AopUtils.isCglibProxy(ci2)) {
                            original2 = ClassUtils.getUserClass(original2);
                        }
                        Interceptor annotation1 = original1.getAnnotation(Interceptor.class);
                        Interceptor annotation2 = original2.getAnnotation(Interceptor.class);
                        return annotation1.priority() - annotation2.priority();
                    }
                    throw new ConnectorException("Connector interceptor must implements ConnectorInterceptor.class");
                }
            ).forEach(o -> configuration.addInterceptor((ConnectorInterceptor) o));
        }
    }

    @Override
    public ConnectorFactory getObject() throws Exception {
        if (Objects.isNull(this.configuration) || Objects.isNull(this.configuration.getApiDataSource())) {
            afterPropertiesSet();
        }

        return connectorFactory = new DefaultConnectorFactory(configuration);
    }

    @Override
    public Class<?> getObjectType() {
        return Objects.isNull(connectorFactory) ? ConnectorFactory.class : this.connectorFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setBeanName(String s) {
        ConnectorProxyBean.connectorFactoryName = s;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}
