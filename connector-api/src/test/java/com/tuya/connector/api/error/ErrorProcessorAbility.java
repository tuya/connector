package com.tuya.connector.api.error;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 12:18 下午
 */
public interface ErrorProcessorAbility {

    Boolean success();

    Boolean error(int code);

}
