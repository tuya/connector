package com.tuya.connector.api.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/19 11:04 上午
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1076888268359702742L;

    Integer code;
    String msg;
    Boolean success;
    Long t;
    T result;
    String tid;
}
