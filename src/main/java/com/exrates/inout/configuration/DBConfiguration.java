package com.exrates.inout.configuration;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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
@EntityScan(basePackages = {"com.exrates.inout"})
public class DBConfiguration {

    @Value("${spring.datasource.hikari.driver-class-name}")
    private String dbMasterClassname;
    @Value("${spring.datasource.hikari.jdbc-url}")
    private String dbMasterUrl;
    @Value("${spring.datasource.hikari.username}")
    private String dbMasterUser;
    @Value("${spring.datasource.hikari.password}")
    private String dbMasterPassword;

    @Value("${slave.datasource.hikari.driver-class-name}")
    private String dbSlaveClassname;
    @Value("${slave.datasource.hikari.jdbc-url}")
    private String dbSlaveUrl;
    @Value("${slave.datasource.hikari.username}")
    private String dbSlaveUser;
    @Value("${slave.datasource.hikari.password}")
    private String dbSlavePassword;

    @Bean(name = "masterHikariDataSource")
    public DataSource masterHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(dbMasterClassname);
        hikariConfig.setJdbcUrl(dbMasterUrl);
        hikariConfig.setUsername(dbMasterUser);
        hikariConfig.setPassword(dbMasterPassword);
        hikariConfig.setMaximumPoolSize(50);
        return new HikariDataSource(hikariConfig);
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
