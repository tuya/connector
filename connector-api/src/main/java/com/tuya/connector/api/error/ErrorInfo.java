package com.tuya.connector.api.error;

import java.io.Serializable;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 11:37 上午
 */
public class ErrorInfo implements Serializable {

    private String errorCode;
    private String errorMsg;

    public ErrorInfo(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ErrorInfo(String errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorInfo() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return String.format("[errorCode: %s, errorMsg: %s]", errorCode, errorMsg);
    }
}
