package com.tuya.connector.api.autoheader;

import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.context.ContextManager;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/6 11:08 上午
 */
public class HeaderContextManagerImpl implements ContextManager<HeaderContextImpl> {

    private static final ThreadLocal<HeaderContextImpl> ctx = new ThreadLocal<>();

    private final Configuration configuration;

    public HeaderContextManagerImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public HeaderContextImpl start() {
        HeaderContextImpl headerCtx = new HeaderContextImpl(configuration);
        ctx.set(headerCtx);
        return headerCtx;
    }

    @Override
    public void clear() {
        ctx.remove();
    }

    @Override
    public HeaderContextImpl get() {
        return ctx.get();
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
