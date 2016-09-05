package ru.otr.integration.smev3client.smev3adapter.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.otr.integration.smev3client.smev3adapter.beans.NamespaceSwapper;
import ru.otr.integration.smev3client.smev3adapter.beans.OperationSetter;

/**
 * Created by tartanov.mikhail on 03.08.2016.
 */
@Configuration
@Profile("dev")
public class AppConfig {

    @Bean
    public OperationSetter operationSetter()    {
        return new OperationSetter();
    }

    @Bean
    public NamespaceSwapper namespaceSwapper()  {
        return new NamespaceSwapper();
    }

    @Bean
    public CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                context.setUseMDCLogging(true);
                context.setTracing(true);
                //context.setUnitOfWorkFactory(new CustomMDCUnitOfWorkFactory());
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
            }
        };
    }
}
