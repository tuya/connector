package com.tuya.connector.spring.boot.autoconfigure;

import com.tuya.connector.api.config.ConnectionPool;
import com.tuya.connector.api.config.Timeout;
import com.tuya.connector.api.context.ContextManager;
import com.tuya.connector.api.error.ErrorProcessorRegister;
import com.tuya.connector.api.header.HeaderProcessor;
import com.tuya.connector.api.token.TokenManager;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/29 11:08 上午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiProperties {
    /**
     * Connector datasource base url
     */
    String baseUrl;

    String ak;

    String sk;

    String application;

    @NestedConfigurationProperty
    Timeout timeout;

    @NestedConfigurationProperty
    ConnectionPool pool;

    @NestedConfigurationProperty
    LoggingProperties logging;

    boolean autoSetHeader;
    Class<HeaderProcessor> headerProcessor;

    boolean autoRefreshToken;
    Class<TokenManager> tokenManager;

    Class<ContextManager> contextManager;

    ErrorProcessorRegister errorProcessorRegister;
}
