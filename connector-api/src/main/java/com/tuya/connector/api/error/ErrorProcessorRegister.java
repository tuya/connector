package com.tuya.connector.api.error;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 10:52 上午
 */
public class ErrorProcessorRegister {

    private Map<String, ErrorProcessor> processorMap = new HashMap<>();

    public void register(ErrorProcessor processor) {
        processorMap.put(processor.getErrorCode(), processor);
    }

    public ErrorProcessor get(String errorCode) {
        return processorMap.get(errorCode);
    }


}
