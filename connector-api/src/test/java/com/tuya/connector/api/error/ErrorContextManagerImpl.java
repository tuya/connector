package com.tuya.connector.api.error;

import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.context.ContextManager;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 11:08 上午
 */
public class ErrorContextManagerImpl implements ContextManager<ErrorContextImpl> {

    private static final ThreadLocal<ErrorContextImpl> ctx = new ThreadLocal<>();

    private Configuration configuration;

    public ErrorContextManagerImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ErrorContextImpl start() {
        ErrorContextImpl errorContext = new ErrorContextImpl(configuration);
        ctx.set(errorContext);
        return errorContext;
    }

    @Override
    public void clear() {
        ctx.remove();
    }

    @Override
    public ErrorContextImpl get() {
        return ctx.get();
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
