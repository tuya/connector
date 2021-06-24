package com.tuya.connector.api.autoheader;

import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.config.Logging;
import com.tuya.connector.api.context.ContextManager;
import com.tuya.connector.api.core.ConnectorFactory;
import com.tuya.connector.api.core.DefaultConnectorFactory;
import com.tuya.connector.api.header.HeaderProcessor;
import com.tuya.connector.api.model.HttpRequest;
import com.tuya.connector.api.token.Token;
import com.tuya.connector.api.token.TokenManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/4 8:26 下午
 */
public class AutoSetHeaderTest {

    static HeaderAbility ability;

    @BeforeAll
    static void init() {
        Configuration config = new Configuration();
        ApiDataSource apiDataSource = ApiDataSource.DEFAULT_BUILDER.build();
        apiDataSource.setBaseUrl("http://localhost:8080");
        apiDataSource.setLoggingStrategy(Logging.Strategy.BODY);
        apiDataSource.setAutoSetHeader(true);

        HeaderProcessor headerProcessor = new HeaderProcessor() {
            @Override
            public Map<String, String> value(HttpRequest request) {
                Map<String, String> map = new HashMap<>();
                map.put("autoheader", "enable");
                return map;
            }
            @Override
            public String sign(String content) {
                return content;
            }

            @Override
            public Configuration getConfiguration() {
                return config;
            }
        };
        apiDataSource.setHeaderProcessor(headerProcessor);
        ContextManager contextManager = new HeaderContextManagerImpl(config);
        apiDataSource.setContextManager(contextManager);
        config.setApiDataSource(apiDataSource);
        apiDataSource.setTokenManager(new TokenManager<Token>() {
            @Override
            public Token getToken() {
                return null;
            }

            @Override
            public Token refreshToken() {
                return null;
            }

            @Override
            public Configuration getConfiguration() {
                return config;
            }
        });

        config.init();
        ConnectorFactory connectorFactory = new DefaultConnectorFactory(config);
        ability = connectorFactory.loadConnector(HeaderConnector.class);
    }

    @Test
    void enableTest() {
        String enable = ability.enable();
        assertEquals(enable, "enable");
    }

}
