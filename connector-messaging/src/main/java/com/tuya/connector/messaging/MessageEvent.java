package com.tuya.connector.messaging;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/2 2:14 下午
 */
public interface MessageEvent {
    /**
     * message event type, unique identification of one message envent
     * @return
     */
    String type();
}
