package ru.otr.integration.smev3client.pollers.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.camel.CamelContext;
import org.apache.camel.component.scheduler.SchedulerComponent;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;
import org.apache.camel.spi.ExecutorServiceManager;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tartanov.mikhail on 23.08.2016.
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

                SchedulerComponent scheduler = context.getComponent("scheduler", SchedulerComponent.class);
                scheduler.setConcurrentTasks(15);

                /*ExecutorServiceManager manager = context.getExecutorServiceManager();
                ThreadPoolProfile defaultProfile = manager.getDefaultThreadPoolProfile();
                defaultProfile.setMaxQueueSize(1000);
                defaultProfile.setMaxPoolSize(20);
                defaultProfile.setPoolSize(1);*/
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
        Config config =  new Config();
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        return config;
    }
}
