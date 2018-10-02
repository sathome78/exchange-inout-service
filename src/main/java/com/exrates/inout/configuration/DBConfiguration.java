package com.exrates.inout.configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Value("${}")
    private String dbMasterClassname;

    @Value("${}")
    private String dbMasterUrl;

    @Value("${}")
    private String dbMasterUser;

    @Value("${d}")
    private String dbMasterPassword;

    @Value("")
    private String dbSlaveClassname;

    @Value("")
    private String dbSlaveUrl;

    @Value("")
    private String dbSlaveUser;

    @Value("")
    private String dbSlavePassword;

    @Bean(name = "masterHikariDataSource")
    public DataSource masterHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dbMasterClassname);
        hikariConfig.setJdbcUrl(dbMasterUrl);
        hikariConfig.setUsername(dbMasterUser);
        hikariConfig.setPassword(dbMasterPassword);
        hikariConfig.setMaximumPoolSize(50);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    @Bean(name = "slaveHikariDataSource")
    public DataSource slaveHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dbSlaveClassname);
        hikariConfig.setJdbcUrl(dbSlaveUrl);
        hikariConfig.setUsername(dbSlaveUser);
        hikariConfig.setPassword(dbSlavePassword);
        hikariConfig.setMaximumPoolSize(50);
        hikariConfig.setReadOnly(true);
        return new HikariDataSource(hikariConfig);
    }

    @Primary
    @DependsOn("masterHikariDataSource")
    @Bean(name = "masterTemplate")
    public NamedParameterJdbcTemplate masterNamedParameterJdbcTemplate(@Qualifier("masterHikariDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @DependsOn("slaveHikariDataSource")
    @Bean(name = "slaveTemplate")
    public NamedParameterJdbcTemplate slaveNamedParameterJdbcTemplate(@Qualifier("slaveHikariDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Primary
    @DependsOn("masterHikariDataSource")
    @Bean
    public JdbcTemplate jdbcTemplate(@Qualifier("masterHikariDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Primary
    @Bean(name = "masterTxManager")
    public PlatformTransactionManager masterPlatformTransactionManager() {
        return new DataSourceTransactionManager(masterHikariDataSource());
    }

    @Bean(name = "slaveTxManager")
    public PlatformTransactionManager slavePlatformTransactionManager() {
        return new DataSourceTransactionManager(slaveHikariDataSource());
    }
}
