package com.tuya.connector.api.error;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 11:47 上午
 */
public class ErrorContext {
    private static final ThreadLocal<ErrorInfo> ERROR_CTX = new ThreadLocal<>();

    public static void set(ErrorInfo errorInfo) {
        ERROR_CTX.set(errorInfo);
    }

    public static ErrorInfo get() {
        return ERROR_CTX.get();
    }

    public static void remove() {
        ERROR_CTX.remove();
    }

}
