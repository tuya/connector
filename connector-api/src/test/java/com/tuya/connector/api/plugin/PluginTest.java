package com.tuya.connector.api.plugin;

import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.config.Logging;
import com.tuya.connector.api.core.DefaultConnectorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 10:18 上午
 */
@Slf4j
public class PluginTest {

    static PluginAbility ability;

    @BeforeAll
    static void init() {
        Configuration config = new Configuration();
        ApiDataSource apiDataSource = ApiDataSource.DEFAULT_BUILDER.build();
        apiDataSource.setBaseUrl("http://localhost:8080");
        apiDataSource.setLoggingStrategy(Logging.Strategy.BODY);
        config.setApiDataSource(apiDataSource);
        config.addInterceptor(
            new ConnectorInterceptor() {
                @Override
                public Object intercept(Invocation invocation) throws Throwable {
                    log.warn("plugin before...");
                    Object result = invocation.proceed();
                    log.warn("plugin after...");
                    return result;
                }
            }
        );

        ability = new DefaultConnectorFactory(config).loadConnector(PluginConnector.class);
    }

    @Test
    void pluginTest() {
        Boolean result = ability.plugin();
        Assertions.assertEquals(result, true);
    }

}
