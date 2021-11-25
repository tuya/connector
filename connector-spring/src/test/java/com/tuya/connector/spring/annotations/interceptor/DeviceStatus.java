package com.tuya.connector.spring.annotations.interceptor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: connector
 * @description:
 * @author: milong
 * @create: 2021-11-17 11:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatus {

    private String code;

    private Object value;

}
