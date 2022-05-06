package com.epam.esm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@PropertySource({"classpath:dev-connection.properties","classpath:prod-connection.properties"})
public class DaoConfig {

    @Autowired
    private Environment environment;
    @Bean
    @Profile("dev")
    public DataSource dataSource(HikariConfig hikariConfig) {
        hikariConfig.setJdbcUrl(environment.getProperty("db.dev.url"));
        hikariConfig.setUsername(environment.getProperty("db.dev.username"));
        hikariConfig.setPassword(environment.getProperty("db.dev.pw"));
        hikariConfig.setDriverClassName(environment.getProperty("db.dev.cn"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("db.dev.ps")));
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    @Bean
    @Profile("prod")
    public DataSource prodDataSource(HikariConfig hikariConfig) {
        hikariConfig.setJdbcUrl(environment.getProperty("db.prod.url"));
        hikariConfig.setUsername(environment.getProperty("db.prod.username"));
        hikariConfig.setPassword(environment.getProperty("db.prod.pw"));
        hikariConfig.setDriverClassName(environment.getProperty("db.prod.cn"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("db.prod.ps")));
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    @Bean
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
