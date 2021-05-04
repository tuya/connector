package com.tuya.connector.messaging;

import com.tuya.connector.messaging.event.OfflineEvent;
import com.tuya.connector.messaging.event.OnlineEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/3/24 2:41 下午
 */
@Slf4j
public class MessageTest {
    static AnnotationConfigApplicationContext ctx;

    @BeforeAll
    static void init() {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(MessageConfig.class);
        ctx.register(OneMessageDispatcher.class);
        ctx.refresh();
        ctx.start();
    }

    @Test
    void msgTest() {
        OneMessageDispatcher dispatcher = ctx.getBean(OneMessageDispatcher.class);
        dispatcher.dispatch();
    }


    @Configuration
    static class MessageConfig {
        @EventListener
        public void online(OnlineEvent onlineEvent) {
            log.info("### online event happened ###");
            Assertions.assertTrue(true);
        }

        @EventListener
        public void offline(OfflineEvent offlineEvent) {
            log.info("### offline event happened ###");
            Assertions.assertTrue(true);
        }
    }

}
