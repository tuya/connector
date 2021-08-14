package com.tuya.connector.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.StringUtils;

/**
 * <p> TODO
 *
 * @author qiufeng.yu@tuya.com
 * @since 2021/1/25 5:52 下午
 */

@Data
@ConfigurationProperties(prefix = ConnectorProperties.CONNETOR_PREFIX)
public class ConnectorProperties implements InitializingBean {
    protected static final String CONNETOR_PREFIX = "connector";

    /**
     * Connector Api Datasource
     */
    @NestedConfigurationProperty
    private ApiProperties api;

    private String ak;

    private String sk;

    private String application;

    /**
     * Export local http service from connector
     */
    private boolean autoExport;

    @Override
    public void afterPropertiesSet() {
        if (StringUtils.isEmpty(api.getAk()) && !StringUtils.isEmpty(ak)) {
            api.setAk(ak);
        }

        if (StringUtils.isEmpty(api.getSk()) && !StringUtils.isEmpty(sk)) {
            api.setSk(sk);
        }

        if (StringUtils.isEmpty(api.getApplication()) && !StringUtils.isEmpty(application)) {
            api.setApplication(application);
        }else{
            api.setApplication("Source");
        }
    }
}
