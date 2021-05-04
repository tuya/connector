package com.tuya.connector.api.error;

import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.plugin.ConnectorInterceptor;
import com.tuya.connector.api.plugin.Invocation;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 10:39 上午
 */
@Slf4j
public class ErrorProcessorInterceptor implements ConnectorInterceptor {

    private final Configuration configuration;

    public ErrorProcessorInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable t) {
            ErrorInfo errorInfo = ErrorContext.get();
            if (Objects.nonNull(errorInfo)) {

                ErrorProcessorRegister errorProcessorRegister = configuration.getApiDataSource().getErrorProcessorRegister();
                ErrorProcessor errorProcessor = errorProcessorRegister.get(errorInfo.getErrorCode());
                if (Objects.nonNull(errorProcessor)) {
                    log.warn("processor for error code[{}]  ", errorInfo.getErrorCode());
                    return errorProcessor.process(errorInfo, invocation, configuration.getApiDataSource().getContextManager().get());
                } else {
                    log.info("No error processor found for errorCode:{}", errorInfo.getErrorCode());
                    throw t;
                }
            }
            throw t;
        } finally {
            ErrorContext.remove();
        }
        return result;
    }

}
