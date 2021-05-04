package com.tuya.connector.spring.annotations.connectorscan;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 3:44 下午
 */
public class AnnotationsTest {

    static AnnotationConfigApplicationContext ctx;

    @BeforeAll
    static void init() {
        ctx = new AnnotationConfigApplicationContext(ConnectorScanConfig.class);
        ctx.start();
    }

    @Test
    void getTest() {
        AnnotationConfigAbility ability = ctx.getBean(AnnotationConfigConnector.class);
        Boolean result = ability.get();
        Assertions.assertEquals(result, true);
    }

}
