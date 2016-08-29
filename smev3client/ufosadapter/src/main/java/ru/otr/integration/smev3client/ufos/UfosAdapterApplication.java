package ru.otr.integration.smev3client.ufos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by nikitin.maksim on 29.08.2016.
 */
@ImportResource("classpath:/applicationContext.xml")
@SpringBootApplication
@EnableConfigurationProperties
@EnableJms
@EnableTransactionManagement
public class UfosAdapterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UfosAdapterApplication.class, args);
    }
}
