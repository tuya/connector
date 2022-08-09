package com.tuya.connector.api.config;

import com.tuya.connector.api.context.ContextInterceptor;
import com.tuya.connector.api.context.ContextManager;
import com.tuya.connector.api.error.ErrorProcessorInterceptor;
import com.tuya.connector.api.exceptions.ConnectorException;
import com.tuya.connector.api.plugin.ConnectorInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/18 4:08 下午
 */

public class Configuration {

    private ApiDataSource apiDataSource;

    private boolean validateEagerly = true;
    private boolean retryOnConnectionFailure = true;
    private boolean checkSSL = true;

    private List<ConnectorInterceptor> interceptors;

    public Configuration() {
    }

    public Configuration(ApiDataSource apiDataSource) {
        this.apiDataSource = apiDataSource;
    }

    public void addInterceptor(ConnectorInterceptor interceptor) {
        if (interceptors == null) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(interceptor);
    }

    public ApiDataSource getApiDataSource() {
        return apiDataSource;
    }

    public void setApiDataSource(ApiDataSource apiDataSource) {
        this.apiDataSource = apiDataSource;
    }

    public boolean isValidateEagerly() {
        return validateEagerly;
    }

    public void setValidateEagerly(boolean validateEagerly) {
        this.validateEagerly = validateEagerly;
    }

    public boolean isCheckSSL() {
        return checkSSL;
    }

    public void setCheckSSL(boolean checkSSL) {
        this.checkSSL = checkSSL;
    }

    public boolean isRetryOnConnectionFailure() {
        return retryOnConnectionFailure;
    }

    public void setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        this.retryOnConnectionFailure = retryOnConnectionFailure;
    }

    public List<ConnectorInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<ConnectorInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @SuppressWarnings("rawtypes")
    public void init() {
        ApiDataSource apiDataSource = Objects.requireNonNull(this.getApiDataSource());
        boolean autoSetHeader = apiDataSource.isAutoSetHeader();
        boolean autoRefreshToken = apiDataSource.isAutoRefreshToken();
        if (autoSetHeader || autoRefreshToken) {
            ContextManager contextManager = apiDataSource.getContextManager();
            if (Objects.isNull(contextManager)) {
                throw new ConnectorException("ContextManager required not null where enable auto set header or auto refresh token.");
            }
        }
        addInterceptor(new ErrorProcessorInterceptor(this));
        addInterceptor(new ContextInterceptor(this));
    }

}
