package com.tuya.connector.api.context;

import com.tuya.connector.api.config.Configuration;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 5:14 下午
 */
public interface ContextManager<T extends Context> {
    /**
     * start build current context
     * @return
     */
    T start();

    /**
     * clear context
     */
    void clear();

    /**
     * get current context
     * @return
     */
    T get();

    /**
     * get connector configuration
     * @return
     */
    Configuration getConfiguration();
}
