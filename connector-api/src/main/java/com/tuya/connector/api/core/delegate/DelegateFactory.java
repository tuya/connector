package com.tuya.connector.api.core.delegate;

import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.exceptions.ConnectorDelegateException;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/21 10:28 上午
 */
@Slf4j
public class DelegateFactory {

    public static ProxyDelegate forRetrofit(Configuration configuration, Class<?> connector) {
        Instant start = Instant.now();
        log.debug("Start create retrofit delegate for connector[{}] at : {}", connector.getName(), start);
        try {
            return new RetrofitDelegate(configuration, connector);
        } catch (Exception e) {
            throw new ConnectorDelegateException(
                    String.format("Error create retrofit delegate for connector : %s.\n Cause: %s", connector.getName(), e), e);
        } finally {
            log.debug("Finish create retrofit delegate for connector[{}] costs : {} ms", connector.getName(),
                Duration.between(start, Instant.now()).toMillis());
        }
    }

}
