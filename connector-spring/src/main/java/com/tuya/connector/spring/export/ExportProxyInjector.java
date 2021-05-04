package com.tuya.connector.spring.export;

import com.tuya.connector.spring.annotations.Export;
import com.tuya.connector.spring.core.ConnectorStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/7 12:46 下午
 */
@Slf4j
public class ExportProxyInjector implements ApplicationContextAware, InitializingBean {

    private static ApplicationContext ctx;
    private static RequestMappingHandlerMapping requestMappingHandler;

    private final ExportConfiguration exportConfiguration;

    public ExportProxyInjector(ExportConfiguration exportConfiguration) {
        this.exportConfiguration = exportConfiguration;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Predicate<Class<?>> exportPredicate = connector -> connector.isAnnotationPresent(Export.class);
        if (exportConfiguration.autoExport) {
            exportPredicate = connector -> true;
        }

        Set<Class<?>> exportConnectors = ConnectorStatistics.getConnectorSet();
        exportConnectors.stream().filter(exportPredicate).forEach(
            connector -> {
                log.info("start export connector: {}", connector.getSimpleName());
                Class<?> exportClass = ExportProxyFactory.build(connector);
                AbstractBeanDefinition controllerBdf = BeanDefinitionBuilder.genericBeanDefinition(exportClass).getBeanDefinition();
                GenericBeanDefinition beanDef = new GenericBeanDefinition(controllerBdf);
                String controllerName = connector.getSimpleName() + "_export_controller";
                ((GenericApplicationContext) ctx).registerBeanDefinition(controllerName, beanDef);
                registerRequestMapping(controllerName);
                log.info("finish export connector: {}", connector.getSimpleName());
            }
        );
    }

    private void registerRequestMapping(String controllerName) {
        requestMappingHandler = Objects.nonNull(requestMappingHandler) ? requestMappingHandler : ctx.getBean(RequestMappingHandlerMapping.class);
        try {
            Method method = requestMappingHandler.getClass().getSuperclass().getSuperclass()
                .getDeclaredMethod("detectHandlerMethods",Object.class);
            method.setAccessible(true);
            method.invoke(requestMappingHandler,controllerName);
        } catch (Exception e) {
            log.error("Export connector request mapping error", e);
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}
