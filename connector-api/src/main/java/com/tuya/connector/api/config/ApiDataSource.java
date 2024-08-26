package com.tuya.connector.api.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.gson.GsonBuilder;
import com.tuya.connector.api.context.ContextManager;
import com.tuya.connector.api.error.ErrorProcessorRegister;
import com.tuya.connector.api.header.HeaderProcessor;
import com.tuya.connector.api.token.TokenManager;
import lombok.*;
import lombok.experimental.FieldDefaults;
import okhttp3.OkHttpClient;

import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/18 8:51 下午
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiDataSource {
    public static ApiDataSourceBuilder DEFAULT_BUILDER;

    static {
        DEFAULT_BUILDER  = ApiDataSource.builder()
            .timeout(Timeout.DEFAULT_BUILDER.build())
            .connectionPool(ConnectionPool.DEFAULT_BUILDER.build())
            .loggingLevel(Logging.Level.INFO)
            .loggingStrategy(Logging.Strategy.NONE);
        ErrorProcessorRegister errorProcessorRegister = new ErrorProcessorRegister();
        DEFAULT_BUILDER.errorProcessorRegister(errorProcessorRegister);
    }

    String baseUrl;
    String ak;
    String sk;
    String entry;
    String lang;

    static TransmittableThreadLocal<String> currentAk = new TransmittableThreadLocal<>();
    static TransmittableThreadLocal<String> currentSk = new TransmittableThreadLocal<>();
    static TransmittableThreadLocal<String> currentLang = new TransmittableThreadLocal<>();

    public String getAk() {
        if (Objects.nonNull(currentAk) && currentAk.get() != null) {
            return currentAk.get();
        }
        return ak;
    }

    public String getSk() {
        if (Objects.nonNull(currentSk) && currentSk.get() != null) {
            return currentSk.get();
        }
        return sk;
    }

    public String getLang() {
        if (Objects.nonNull(currentLang) && currentLang.get() != null) {
            return currentLang.get();
        }
        return lang;
    }


    public void clear() {
        currentAk.remove();
        currentSk.remove();
        currentLang.remove();
    }

    public void setLang(String lang) {
        this.lang = lang;
        currentLang.set(lang);
    }

    public void setAk(String ak) {
        this.ak = ak;
        currentAk.set(ak);
    }

    public void setSk(String sk) {
        this.sk = sk;
        currentSk.set(sk);
    }

    public ApiDataSource(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ApiDataSource(String baseUrl, String ak, String sk) {
        this.baseUrl = baseUrl;
        this.ak = ak;
        this.sk = sk;
    }

    public ApiDataSource(String baseUrl, String ak, String sk,String entry) {
        this.baseUrl = baseUrl;
        this.ak = ak;
        this.sk = sk;
        this.entry = entry;
    }

    Timeout timeout;

    ConnectionPool connectionPool;

    Logging.Level loggingLevel;

    Logging.Strategy loggingStrategy;

    boolean autoSetHeader;

    boolean autoRefreshToken;

    HeaderProcessor headerProcessor;

    TokenManager<?> tokenManager;

    ContextManager contextManager;

    ErrorProcessorRegister errorProcessorRegister;

    /**
     * specify a client for this data source, if not null, will use this client and ignore other configurations
     */
    OkHttpClient specificClient;

    GsonBuilder gsonBuilder;
}
