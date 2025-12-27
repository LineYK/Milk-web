package com.milk.milkweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {

	@Value("${DB_TYPE}")
	private String dbType;

	@Value("${DB_HOST}")
	private String dbHost;

	@Value("${DB_NAME:milkweb}")
	private String dbName;

	@Value("${DB_USERNAME}")
	private String dbUsername;

	@Value("${DB_PASSWORD}")
	private String dbPassword;

	@Value("${DB_PORT}")
	private String dbPort;

	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource.hikari")
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		
		if ("postgresql".equalsIgnoreCase(dbType)) {
			// PostgreSQL 설정
			config.setDriverClassName("org.postgresql.Driver");
			String port = dbPort.isEmpty() ? "5432" : dbPort;
			config.setJdbcUrl(String.format("jdbc:postgresql://%s:%s/%s", dbHost, port, dbName));
		} else {
			// MySQL 설정
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			String port = dbPort.isEmpty() ? "3306" : dbPort;
			config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", dbHost, port, dbName));
		}
		
		config.setUsername(dbUsername);
		config.setPassword(dbPassword);
		
		return new HikariDataSource(config);
	}
}

