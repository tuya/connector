package com.tuya.connector.api.error;

import com.tuya.connector.api.context.Context;
import com.tuya.connector.api.plugin.Invocation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 12:55 下午
 */
@Slf4j
public class ErrorProcessorForCode8888 implements ErrorProcessor{
    @Override
    public Object process(ErrorInfo errorInfo, Invocation invocation, Context context) {
        log.error("8888 error processoer");
        return true;
    }

    @Override
    public String getErrorCode() {
        return "8888";
    }
}
