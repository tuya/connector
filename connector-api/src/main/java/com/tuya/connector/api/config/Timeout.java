package com.tuya.connector.api.config;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/18 4:27 下午
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Timeout {
    protected static TimeoutBuilder DEFAULT_BUILDER = Timeout.builder().callTimeout(5).connectTimeout(5).readTimeout(5).writeTimeout(5);

    int callTimeout;
    int connectTimeout;
    int readTimeout;
    int writeTimeout;
}
