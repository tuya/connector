package com.tuya.connector.messaging.event;

import com.tuya.connector.messaging.BaseMsgEvent;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/3/24 2:28 下午
 */
public class OfflineEvent extends BaseMsgEvent {

    @Override
    public String type() {
        return EventType.Offline.getType();
    }
}
