package com.tuya.connector.api.error;

import com.tuya.connector.api.config.ApiDataSource;
import com.tuya.connector.api.config.Configuration;
import com.tuya.connector.api.config.Logging;
import com.tuya.connector.api.context.ContextManager;
import com.tuya.connector.api.core.ConnectorFactory;
import com.tuya.connector.api.core.DefaultConnectorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/2/5 12:14 下午
 */
@Slf4j
public class ErrorProcessorTest {

    static ErrorProcessorAbility ability;

    @BeforeAll
    static void init() {
        Configuration config = new Configuration();
        ApiDataSource apiDataSource = ApiDataSource.DEFAULT_BUILDER.build();
        apiDataSource.setBaseUrl("http://localhost:8080");
        apiDataSource.setLoggingStrategy(Logging.Strategy.BODY);

        ContextManager contextManager = new ErrorContextManagerImpl(config);
        apiDataSource.setContextManager(contextManager);

        apiDataSource.getErrorProcessorRegister().register(new ErrorProcessorForCode8888());
        config.setApiDataSource(apiDataSource);

        config.init();
        ConnectorFactory connectorFactory = new DefaultConnectorFactory(config);
        ability = connectorFactory.loadConnector(ErrorProcessorConnector.class);
    }

    @Test
    void success() {
        Boolean success = ability.success();
        Assertions.assertEquals(success, true);
    }

    @ParameterizedTest
    @ValueSource(ints = {8888, 6666})
    void error(int code) {
        Boolean error = false;
        if (code == 8888) {
            error = ability.error(code);
            Assertions.assertEquals(error, true);
        } else {
            try {
                ability.error(code);
            } catch (Exception e) {
                Assertions.fail("no error processor for " + code);
            }
        }
    }

}
