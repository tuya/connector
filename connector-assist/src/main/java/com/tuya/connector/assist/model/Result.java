package com.tuya.connector.assist.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * @author qiufeng.yu@tuya.com
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
}