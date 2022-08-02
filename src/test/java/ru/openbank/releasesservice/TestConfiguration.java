package ru.openbank.releasesservice;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class TestConfiguration {

    @Bean(name = "databaseDataSourceConnection")
    public DatabaseDataSourceConnection customDatabaseDataSourceConnection(@Autowired DataSource testDataSource) throws SQLException {
        return new DatabaseDataSourceConnection(testDataSource);
    }
}