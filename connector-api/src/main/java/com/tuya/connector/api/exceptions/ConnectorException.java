package com.tuya.connector.api.exceptions;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/21 4:12 下午
 */
public class ConnectorException extends RuntimeException {

    private static final long serialVersionUID = 3890598182855130507L;


    public ConnectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectorException(String message) {
        super(message);
    }
}
