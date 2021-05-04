package com.tuya.connector.api.logging;

import com.tuya.connector.api.config.Logging;
import lombok.extern.slf4j.Slf4j;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/28 12:25 下午
 */
@Slf4j
public class HttpLoggingFactory {

    public static HttpLoggingInterceptor createHttpLoggingInterceptor(Logging.Level logLevel, Logging.Strategy logStrategy) {
        HttpLoggingInterceptor.Logger logger;
        switch (logLevel) {
            case TRACE:
                logger = log::trace;
                break;
            case DEBUG:
                logger = log::debug;
                break;
            case WARN:
                logger = log::warn;
                break;
            case ERROR:
                logger = log::error;
                break;
            default: logger = log::info;
        }
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(logger);
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.valueOf(logStrategy.name()));
        return httpLoggingInterceptor;
    }
}
