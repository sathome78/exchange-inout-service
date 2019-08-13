package com.exrates.inout.configuration;


import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.impl.NamedParameterJdbcTemplateWrapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Log4j2
@Configuration
@EntityScan(basePackages = {"com.exrates.inout"})
@ComponentScan({"com.exrates.inout"})
public class DBConfiguration {

    private final static String NAME_PROD_PROFILE = "prod";
    private final static String JDBC_URL_TEMPLATE_THAT_NEED_REPLACE = "DB_HOST";
    private final static String JDBC_URL_CONNECT_TEMPLATE = "jdbc:mysql://" + JDBC_URL_TEMPLATE_THAT_NEED_REPLACE + ":3306/birzha?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true";
    private final static String SECRET_PROD_DB_MASTER_HOST = "db_master_host\":\"";
    private final static String SECRET_PROD_DB_SLAVE_HOST = "db_slave_host\":\"";
    private final static String SECRET_PROD_DB_USER = "db_user\":\"";
    private final static String SECRET_PROD_DB_PASSWORD = "db_password\":\"";

    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.hikari.jdbc-url}")
    private String jdbcUrl;
    @Value("${spring.datasource.hikari.username}")
    private String user;
    @Value("${spring.datasource.hikari.password}")
    private String password;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    private AlgorithmService algorithmService;

    @Bean(name = "masterHikariDataSource")
    public DataSource masterHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        if(activeProfile.equals(NAME_PROD_PROFILE)) {
            hikariConfig.setJdbcUrl(JDBC_URL_CONNECT_TEMPLATE.replace(JDBC_URL_TEMPLATE_THAT_NEED_REPLACE, algorithmService.getSecret(SECRET_PROD_DB_MASTER_HOST)));
            hikariConfig.setUsername(algorithmService.getSecret(SECRET_PROD_DB_USER));
            hikariConfig.setPassword(algorithmService.getSecret(SECRET_PROD_DB_PASSWORD));

            log.info("DB_CONFIG Master| JdbcUrl: {} | Db user: {}",
                JDBC_URL_CONNECT_TEMPLATE.replace(JDBC_URL_TEMPLATE_THAT_NEED_REPLACE, algorithmService.getSecret(SECRET_PROD_DB_MASTER_HOST)),
                algorithmService.getSecret(SECRET_PROD_DB_USER));

        } else {
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);
        }
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(1);

        return new HikariDataSource(hikariConfig);
    }

    @Bean(name = "slaveHikariDataSource")
    public DataSource slaveHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        if(activeProfile.equals(NAME_PROD_PROFILE)) {
            hikariConfig.setJdbcUrl(JDBC_URL_CONNECT_TEMPLATE.replace(JDBC_URL_TEMPLATE_THAT_NEED_REPLACE, algorithmService.getSecret(SECRET_PROD_DB_SLAVE_HOST)));
            hikariConfig.setUsername(algorithmService.getSecret(SECRET_PROD_DB_USER));
            hikariConfig.setPassword(algorithmService.getSecret(SECRET_PROD_DB_PASSWORD));

            log.info("DB_CONFIG Slave| JdbcUrl: {} | Db user: {}",
                JDBC_URL_CONNECT_TEMPLATE.replace(JDBC_URL_TEMPLATE_THAT_NEED_REPLACE, algorithmService.getSecret(SECRET_PROD_DB_SLAVE_HOST)),
                algorithmService.getSecret(SECRET_PROD_DB_USER));

        } else {
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);
        }
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setMaximumPoolSize(1);
        hikariConfig.setReadOnly(true);

        return new HikariDataSource(hikariConfig);
    }

    @Primary
    @DependsOn("masterHikariDataSource")
    @Bean(name = "masterTemplate")
    public NamedParameterJdbcTemplate masterNamedParameterJdbcTemplate(@Qualifier("masterHikariDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplateWrapper(dataSource);
    }

    @DependsOn("slaveHikariDataSource")
    @Bean(name = "slaveTemplate")
    public NamedParameterJdbcTemplate slaveNamedParameterJdbcTemplate(@Qualifier("slaveHikariDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplateWrapper(dataSource);
    }

    @Primary
    @DependsOn("masterHikariDataSource")
    @Bean(name = "jMasterTemplate")
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
