package com.tuya.connector.api.header;

import com.tuya.connector.api.context.Context;
import com.tuya.connector.api.context.ContextManager;
import com.tuya.connector.api.exceptions.ConnectorException;
import com.tuya.connector.api.model.HttpRequest;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;

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
        Request request = chain.request();

        BufferedSink sink = new Buffer();
        RequestBody body = request.body();
        if (body != null) {
            body.writeTo(sink);
        }
        HttpRequest httpRequest = HttpRequest.builder()
                .httpMethod(request.method())
                .headers(request.headers().toMultimap())
                .url(request.url().url())
                .body(sink.buffer().readByteArray())
                .build();
        Map<String, String> headerMap = headerProcessor.value(httpRequest);

        headerMap.put("Dev_channel", "SaaSFramework");
        headerMap.put("Dev_lang", "Java");
        Request.Builder requestBuilder = chain.request().newBuilder();
        //okhttp cannot add null value header
        nullToEmptyForMapValue(headerMap);
        headerMap.forEach(requestBuilder::addHeader);
        return chain.proceed(requestBuilder.build());
    }

    private void nullToEmptyForMapValue(Map<String,String> map){
        map.forEach((k,v)->{
            if(v==null){
                map.put(k,"");
            }
        });
    }

    private void checkCtx(Context ctx) {
        if (Objects.isNull(ctx) || Objects.isNull(ctx.getApiDataSource()) || Objects.isNull(ctx.getApiDataSource().getTokenManager())) {
            throw new ConnectorException("ConnectorContext required not null when adding default headers");
        }
    }

}
