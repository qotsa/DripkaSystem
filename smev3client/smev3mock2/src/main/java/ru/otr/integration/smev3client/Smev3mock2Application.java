package ru.otr.integration.smev3client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class Smev3mock2Application {

	public static void main(String[] args) {
		SpringApplication.run(Smev3mock2Application.class, args);
	}
}
