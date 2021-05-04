package com.tuya.connector.api.context;

import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.plugin.ConnectorInterceptor;
import com.tuya.connector.api.plugin.Invocation;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * <p> TODO
 *
 * @author 哲也（张梓濠 zheye.zhang@tuya.com）
 * @since 2021/4/24
 */
@Slf4j
public class ContextInterceptor implements ConnectorInterceptor {

    private final Configuration configuration;

    public ContextInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result;
        try {
            setContext();
            result = invocation.proceed();
        } catch (Throwable t) {
            log.warn("content intercept error: {}", t.getMessage());
            throw t;
        } finally {
            clearContext();
        }
        return result;
    }

    private void setContext() {
        ContextManager contextManager = configuration.getApiDataSource().getContextManager();
        if (Objects.nonNull(contextManager)) {
            contextManager.start();
        }
    }

    private void clearContext() {
        ContextManager contextManager = configuration.getApiDataSource().getContextManager();
        if (Objects.nonNull(contextManager)) {
            contextManager.clear();
        }
    }

}
