package com.netcracker.testerritto;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@PropertySource("classpath:application.properties")
public class DataSourceConfig {
  @Autowired
  private Environment environment;

  @Bean
  public DataSource getDataSource() {
    Locale.setDefault(Locale.ENGLISH);
    DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
    dataSourceBuilder.driverClassName(environment.getProperty("spring.datasource.driver-class-name"));
    dataSourceBuilder.url(environment.getProperty("spring.datasource.url"));
    dataSourceBuilder.username(environment.getProperty("spring.datasource.username"));
    dataSourceBuilder.password(environment.getProperty("spring.datasource.password"));
    return dataSourceBuilder.build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
