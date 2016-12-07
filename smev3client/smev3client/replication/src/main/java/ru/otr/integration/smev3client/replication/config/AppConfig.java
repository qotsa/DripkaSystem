package ru.otr.integration.smev3client.replication.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.replication.camel.CustomMDCUnitOfWorkFactory;

/**
 * Created by nikitin.maksim on 29.08.2016.
 */
@Component
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

                context.setUnitOfWorkFactory(new CustomMDCUnitOfWorkFactory());

                //ExecutorServiceManager manager = context.getExecutorServiceManager();
                //ThreadPoolProfile defaultProfile = manager.getDefaultThreadPoolProfile();
                //defaultProfile.setMaxQueueSize(1000);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
            }
        };
    }
}
