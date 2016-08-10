package ru.otr.integration.smev3client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import ru.otr.integration.smev3client.config.ServletConfig;
import ru.otr.integration.smev3client.config.BeansConfig;

//@Import({/*ServletConfig.class,*/ BeansConfig.class})
@ImportResource("classpath:/config/staticConfig.xml")
@SpringBootApplication
@EnableConfigurationProperties
public class Smev3adapterApplication {
	public static void main(String[] args) {
		SpringApplication.run(Smev3adapterApplication.class, args);
	}
}
