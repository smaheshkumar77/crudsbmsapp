package com.example.crudsbmsapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DatabaseInitConfig {

    @Bean
    public DataSourceInitializer h2DataSourceInitializer(DataSource h2DataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new org.springframework.core.io.ClassPathResource("schema.sql"));
        populator.addScript(new org.springframework.core.io.ClassPathResource("data.sql"));

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(h2DataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    @Bean
    public DataSourceInitializer postgresDataSourceInitializer(DataSource postgresDataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new org.springframework.core.io.ClassPathResource("postgres-schema.sql"));

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(postgresDataSource);
        initializer.setDatabasePopulator(populator);
        return initializer;
    }
}
