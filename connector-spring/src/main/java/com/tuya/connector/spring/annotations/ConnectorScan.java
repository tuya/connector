package com.tuya.connector.spring.annotations;

import com.tuya.connector.spring.core.ConnectorScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qiufeng.yu@tuya.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ConnectorScannerRegistrar.class)
public @interface ConnectorScan {
    String[] basePackages() default {};
}
