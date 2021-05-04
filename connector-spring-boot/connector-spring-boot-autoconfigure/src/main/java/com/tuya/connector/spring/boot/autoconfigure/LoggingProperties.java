package com.tuya.connector.spring.boot.autoconfigure;

import lombok.Data;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/27 2:48 下午
 */
@Data
public class LoggingProperties {
    /**
     * Connector logging level: [ERROR / WARN / INFO(Default) / DEBUG / TRACE]
     */
    private String level;

    /**
     * Connector logging strategy: [NONE(Default) / BASIC / HEADERS / BODY]
     */
    private String strategy;

    public static final String DEFAULT_LEVEL = "INFO";
    public static final String DEFAULT_STRATEGY = "NONE";
}
