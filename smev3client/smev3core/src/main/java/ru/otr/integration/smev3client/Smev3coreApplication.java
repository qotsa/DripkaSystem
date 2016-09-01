package ru.otr.integration.smev3client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

@ImportResource("classpath:/applicationContext.xml")
@SpringBootApplication
@EnableConfigurationProperties
public class Smev3coreApplication {
    public static void main(String[] args) {
        SpringApplication.run(Smev3coreApplication.class, args);
    }
}
