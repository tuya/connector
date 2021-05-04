package com.tuya.connector.api.core;

import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.core.delegate.DelegateFactory;
import com.tuya.connector.api.core.delegate.ProxyDelegate;
import com.tuya.connector.api.exceptions.ConnectorException;
import com.tuya.connector.api.exceptions.ExceptionFactory;
import com.tuya.connector.api.utils.Utils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p> TODO 注意 JDK版本、接口默认方法
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/18 4:42 下午
 */
@Slf4j
public class ConnectorProxy<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 3849328772210651623L;

    private final Configuration configuration;
    private final Class<T> connector;

    /**
     * 委托给实际API调用的实现者，目前基于Retrofit实现
     */
    private ProxyDelegate delegate;

    public ConnectorProxy(@NonNull Configuration configuration, @NonNull Class<T> connector) {
        this.configuration = configuration;
        this.connector = connector;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            // set context
            //setContext();

            if (Objects.isNull(delegate)) {
                delegate = DelegateFactory.forRetrofit(configuration, connector);
            }
            return delegate.execute(method, args);
        }  catch (Throwable e) {
            log.error("Error invoke connector[{}]", Utils.classMethodSignature(method, args));
            if (e instanceof ConnectorException) {
                throw e;
            }
            throw ExceptionFactory.wrapException("Error invoke connector. Cause: " + e, e);
        } finally {
            //clearContext();
        }
    }

    /*private void setContext() {
        ContextManager contextManager = configuration.getApiDataSource().getContextManager();
        if (Objects.nonNull(contextManager)) {
            contextManager.start();
        }
    }*/

    /*private void clearContext() {
        ContextManager contextManager = configuration.getApiDataSource().getContextManager();
        if (Objects.nonNull(contextManager)) {
            contextManager.clear();
        }
    }*/
}
