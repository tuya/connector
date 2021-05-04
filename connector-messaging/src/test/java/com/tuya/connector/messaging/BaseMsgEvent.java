package com.tuya.connector.messaging;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/3/24 2:25 下午
 */
public abstract class BaseMsgEvent implements MessageEvent{


    protected enum EventType {
        Online("online", "设备上线"),
        Offline("offline", "设备离线")
        ;

        String type;
        String description;

        EventType(String type) {
            this.type = type;
        }

        EventType(String type, String description) {
            this(type);
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }
    }
}
