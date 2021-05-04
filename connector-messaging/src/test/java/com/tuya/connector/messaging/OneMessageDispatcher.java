package com.tuya.connector.messaging;

import com.tuya.connector.messaging.event.OfflineEvent;
import com.tuya.connector.messaging.event.OnlineEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/3/24 2:40 下午
 */
public class OneMessageDispatcher implements MessageDispatcher, ApplicationContextAware {

    static ApplicationContext ctx;

    @Override
    public void dispatch() {
        ctx.publishEvent(new OnlineEvent());
        ctx.publishEvent(new OfflineEvent());
    }

    @Override
    public MessageDataSource getMsgDataSource() {
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }
}
