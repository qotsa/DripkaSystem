package ru.otr.integration.smev3client.bpmengine.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by nikitin.maksim on 29.08.2016.
 */
@Configuration
public class AppConfig {

    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                context.setUseMDCLogging(true);
                context.setTracing(true);
                context.setStreamCaching(true);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
            }
        };
    }
}
