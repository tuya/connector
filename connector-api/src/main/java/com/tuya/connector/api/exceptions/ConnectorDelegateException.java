package com.tuya.connector.api.exceptions;

/**
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/21 8:32 下午
 */
public class ConnectorDelegateException extends ConnectorException {

    private static final long serialVersionUID = 9170758281863145358L;

    public ConnectorDelegateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectorDelegateException(String message) {
        super(message);
    }
}
