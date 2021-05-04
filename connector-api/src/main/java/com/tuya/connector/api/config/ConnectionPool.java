package com.tuya.connector.api.config;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/18 4:20 下午
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConnectionPool {
    protected static ConnectionPoolBuilder DEFAULT_BUILDER = ConnectionPool.builder().maxIdleConnections(10).keepAliveSecond(60);

    int maxIdleConnections;
    int keepAliveSecond;
}
