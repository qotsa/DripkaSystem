package ru.otr.integration.smev3client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:/applicationContext.xml")
@EnableConfigurationProperties
@SpringBootApplication
public class PollersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollersApplication.class, args);
	}
}
