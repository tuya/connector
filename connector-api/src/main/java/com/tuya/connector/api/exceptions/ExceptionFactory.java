package com.tuya.connector.api.exceptions;

import com.tuya.connector.api.error.ErrorInfo;

import java.lang.reflect.InvocationTargetException;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/21 4:30 下午
 */
public class ExceptionFactory {
    private ExceptionFactory() {
    }

    public static RuntimeException wrapException(String message, Throwable t) {
        if (t instanceof InvocationTargetException) {
            t = t.getCause();
        }
        return new ConnectorException(message, t);
    }

    public static ConnectorResultException ofCode(int code) {
        return ofCode(code, "");
    }

    public static ConnectorResultException ofCode(int code, String msg) {
        return new ConnectorResultException("error code : " + code + ", error msg : " + msg, new ErrorInfo(code + "", msg));
    }

    public static ConnectorResultException ofCode(int code, String msg, Long t) {
        return new ConnectorResultException("error code : " + code + ", error msg : " + msg + " t : " + t, new ErrorInfo(code + "", msg));
    }
}
