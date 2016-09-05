package ru.otr.integration.smev3client.smev3adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

//@Import({/*ServletConfig.class,*/ BeansConfig.class})
@ImportResource("classpath:/config/staticConfig.xml")
@SpringBootApplication
@EnableConfigurationProperties
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
