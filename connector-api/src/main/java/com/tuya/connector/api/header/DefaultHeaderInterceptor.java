package com.tuya.connector.api.header;

import com.tuya.connector.api.context.Context;
import com.tuya.connector.api.context.ContextManager;
import com.tuya.connector.api.exceptions.ConnectorException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/29 12:03 下午
 */
@SuppressWarnings("rawtypes")
public class DefaultHeaderInterceptor implements Interceptor {

    private final HeaderProcessor headerProcessor;

    private final ContextManager contextManager;

    public DefaultHeaderInterceptor(HeaderProcessor headerProcessor, ContextManager contextManager) {
        this.headerProcessor = headerProcessor;
        this.contextManager = contextManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Context ctx = contextManager.get();
        checkCtx(ctx);

        Map<String, String> headerMap = headerProcessor.value(chain.request().url().url());
        headerMap.put("__source", "Java");
        Request.Builder requestBuilder = chain.request().newBuilder();
        headerMap.forEach(requestBuilder::addHeader);
        return chain.proceed(requestBuilder.build());
    }

    private void checkCtx(Context ctx) {
        if (Objects.isNull(ctx) || Objects.isNull(ctx.getApiDataSource()) || Objects.isNull(ctx.getApiDataSource().getTokenManager())) {
            throw new ConnectorException("ConnectorContext required not null when adding default headers");
        }
    }



}
