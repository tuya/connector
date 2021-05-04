package com.tuya.connector.api.token;

import com.tuya.connector.api.config.Configuration;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 10:21 上午
 */
public interface TokenManager<T extends Token> {

    /**
     * get token
     * @return
     */
    T getToken();

    /**
     * refresh token
     * @return
     */
    T refreshToken();

    /**
     * Overrid this method is recommended strongly.
     * @return
     */
    default T getCachedToken() {
        return getToken();
    }

    /**
     * connector configuration
     * @return
     */
    Configuration getConfiguration();

}
