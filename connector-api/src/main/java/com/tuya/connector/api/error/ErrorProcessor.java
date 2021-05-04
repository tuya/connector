package com.tuya.connector.api.error;

import com.tuya.connector.api.context.Context;
import com.tuya.connector.api.plugin.Invocation;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 10:45 上午
 */
public interface ErrorProcessor {
    /**
     * process error when target code of error happened
     * @param errorInfo
     * @param invocation
     * @param context
     * @return
     */
    Object process(ErrorInfo errorInfo, Invocation invocation, Context context);

    /**
     * current error code
     * @return
     */
    String getErrorCode();

}
