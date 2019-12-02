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
    dataSourceBuilder.url(java.lang.System.getenv("DB_URL"));
    dataSourceBuilder.username(java.lang.System.getenv("DB_LOGIN"));
    dataSourceBuilder.password(java.lang.System.getenv("DB_PASSWORD"));
    return dataSourceBuilder.build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
