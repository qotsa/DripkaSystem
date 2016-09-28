package ru.otr.integration.smev3client.bpmengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by nikitin.maksim on 29.08.2016.
 */
@ImportResource("classpath:/applicationContext.xml")
@SpringBootApplication
@EnableConfigurationProperties
@EnableEurekaClient
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
