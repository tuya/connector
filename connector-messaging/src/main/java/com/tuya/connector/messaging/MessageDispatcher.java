package com.tuya.connector.messaging;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/2 2:52 下午
 */
public interface MessageDispatcher {
    /**
     * dispatch message
     */
    void dispatch();

    /**
     * message Datasource
     * @return
     */
    MessageDataSource getMsgDataSource();
}
