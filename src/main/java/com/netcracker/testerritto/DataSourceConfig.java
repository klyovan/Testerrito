package com.netcracker.testerritto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class DataSourceConfig {
    @Autowired
    private Environment environment;

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSourceBuilder.url(java.lang.System.getenv("DB_URL"));//(environment.getProperty("spring.datasource.url"));
        dataSourceBuilder.username(java.lang.System.getenv("DB_LOGIN"));//dataSourceBuilder.username(environment.getProperty("spring.datasource.username"));
        dataSourceBuilder.password(java.lang.System.getenv("DB_PASSWORD"));//dataSourceBuilder.password(environment.getProperty("spring.datasource.password"));
        System.out.println("## DataSource: " + dataSourceBuilder);
        return dataSourceBuilder.build();
    }
}
