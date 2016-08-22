package ru.otr.integration.smev3client.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;
import org.jolokia.http.AgentServlet;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;

/**
 * Created by tartanov.mikhail on 18.08.2016.
 */

@Configuration
@Profile({"dev", "test"})
public class HazelcastConfig {

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
