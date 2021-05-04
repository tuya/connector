package com.tuya.connector.spring.core;

import com.tuya.connector.spring.annotations.ConnectorScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/26 2:19 下午
 */
public class ConnectorScannerRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String ATTRIBUTE_NAME = "basePackages";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        List<String> basePackages = new ArrayList<>();
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ConnectorScan.class.getName()));
        for (String pkg : annoAttrs.getStringArray(ATTRIBUTE_NAME)) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        ClassPathConnectorScanner scanner = new ClassPathConnectorScanner(registry);
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }


}
