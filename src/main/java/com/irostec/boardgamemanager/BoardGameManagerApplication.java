package com.irostec.boardgamemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.irostec.boardgamemanager.configuration.aws", "com.irostec.boardgamemanager.boundary"})
public class BoardGameManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardGameManagerApplication.class, args);
	}

}
