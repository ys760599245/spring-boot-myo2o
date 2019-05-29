package com.chen.myo2o.config.dao;


import com.chen.myo2o.util.DESUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

/**
 *
 */
@Configuration
@MapperScan("com.chen.myo2o.dao")
public class DataSourcesConfiguration {
    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbdPassword;


    @Bean(name = "dataScource")
    public ComboPooledDataSource createDataScource() throws PropertyVetoException {
        //生成datasource实例
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(jdbcDriver);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(DESUtils.getDecryptString(jdbcUsername));
        dataSource.setPassword(DESUtils.getDecryptString(jdbdPassword));
        dataSource.setMinPoolSize(30);
        dataSource.setMinPoolSize(10);
        //关闭连接后不自动commit
        dataSource.setAutoCommitOnClose(false);
        //连接超时时间
        dataSource.setCheckoutTimeout(10000);
        //失败重连次数
        dataSource.setAcquireRetryAttempts(2);
        return dataSource;

    }


}
