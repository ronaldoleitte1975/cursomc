package com.ronaldo.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ronaldo.cursomc.services.DBService;
import com.ronaldo.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;

	@Bean
	public boolean instantiateDataBase() throws ParseException {

		if (!strategy.equals("create")) {
			return false;
		}
		
		dbService.instantiateTestDataBase();
		return true;

	}
	
	@Bean
	public SmtpEmailService emailService() {
		return new SmtpEmailService();
	}

}
