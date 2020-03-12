package edu.leicester.co2103cw3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DbConfig {
	private String USERNAME = "root";
	private String PASSWORD = "root";
	private String HOST = "127.0.0.1";
	private String PORT = "3306";
	
	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://" + HOST + ":" + PORT + "/" + "test_db");
		ds.setUsername(USERNAME);
		ds.setPassword(PASSWORD);
		return ds;
	}
}