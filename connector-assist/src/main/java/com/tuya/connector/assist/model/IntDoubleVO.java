package com.tuya.connector.assist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntDoubleVO implements Serializable {
    private int a1;
    private Integer a2;
    private long a3;
    private Long a4;
    private double a5;
}
