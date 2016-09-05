package ru.otr.integration.smev3client.pollers.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.CamelContext;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by tartanov.mikhail on 23.08.2016.
 */


@Configuration
@Profile("dev")
public class AppConfig {

    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                context.setUseMDCLogging(true);
                context.setTracing(true);
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
            }
        };
    }

    @Bean
    public HazelcastIdempotentRepository hazelcastIdempotentRepositoryBean() {
        return new HazelcastIdempotentRepository(hazelcastBean());
    }

    @Bean
    public HazelcastInstance hazelcastBean()    {
        return Hazelcast.newHazelcastInstance(configBean());
    }

    @Bean
    public Config configBean()  {
        return new Config();
    }
}
