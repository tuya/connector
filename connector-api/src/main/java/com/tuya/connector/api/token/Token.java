package com.tuya.connector.api.token;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 10:01 上午
 */
public interface Token {

    /**
     * access token
     * @return
     */
    String getAccessToken();

    /**
     * refresh token
     * @return
     */
    String getRefreshToken();

}
