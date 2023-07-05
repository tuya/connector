package com.tuya.connector.messaging;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/2 3:29 下午
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDataSource {
    String url;
    String ak;
    String sk;
}
