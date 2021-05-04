package com.tuya.connector.api.exceptions;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/21 9:23 下午
 */
public class ConnectorAnnotationException extends ConnectorException {
    public ConnectorAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectorAnnotationException(String message) {
        super(message);
    }
}
