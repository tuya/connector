package com.tuya.connector.api.exceptions;

import com.tuya.connector.api.error.ErrorInfo;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/3 7:50 下午
 */
public class ConnectorResultException extends ConnectorException{

    private static final long serialVersionUID = -8399778569393382947L;

    private ErrorInfo errorInfo;

    public ConnectorResultException(String message, ErrorInfo errorInfo) {
        super(message);
        this.errorInfo = errorInfo;
    }

    public ConnectorResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectorResultException(String message) {
        super(message);
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }
}
