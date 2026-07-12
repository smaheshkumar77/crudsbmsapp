package com.example.crudsbmsapp.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(FeatureFlagProperties.class)
public class AppConfig {

    @Bean(name = "h2DataSource")
    public DataSource h2DataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:featuredb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean(name = "postgresDataSource")
    public DataSource postgresDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        String jdbcUrl = System.getenv().getOrDefault("POSTGRES_JDBC_URL", "jdbc:h2:mem:postgresdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        String username = System.getenv().getOrDefault("POSTGRES_USER", "sa");
        String password = System.getenv().getOrDefault("POSTGRES_PASSWORD", "");

        if (jdbcUrl.startsWith("jdbc:postgresql")) {
            dataSource.setDriverClassName("org.postgresql.Driver");
        } else {
            dataSource.setDriverClassName("org.h2.Driver");
        }

        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
