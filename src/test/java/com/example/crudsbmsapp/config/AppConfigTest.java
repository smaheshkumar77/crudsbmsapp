package com.example.crudsbmsapp.config;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest {

    @Test
    void postgresDataSourceShouldDefaultToEmbeddedH2() throws SQLException {
        AppConfig appConfig = new AppConfig();
        DataSource dataSource = appConfig.postgresDataSource();

        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.getMetaData().getURL()).contains("jdbc:h2");
        }
    }
}
