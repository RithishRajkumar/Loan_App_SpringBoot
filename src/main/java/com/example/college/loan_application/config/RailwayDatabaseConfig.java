package com.example.college.loan_application.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RailwayDatabaseConfig {

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        String dbUrl = System.getenv("DATABASE_URL");
        if (dbUrl != null && dbUrl.startsWith("postgresql://")) {
            URI dbUri = new URI(dbUrl);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrlJdbc = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            
            return DataSourceBuilder.create()
                    .url(dbUrlJdbc)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }
        
        // Fallback to local H2 database if DATABASE_URL is not set
        return DataSourceBuilder.create()
                .url("jdbc:h2:mem:loandb")
                .username("sa")
                .password("password")
                .driverClassName("org.h2.Driver")
                .build();
    }
}
