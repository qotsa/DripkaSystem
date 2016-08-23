package ru.otr.integration.smev3client.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.otr.integration.smev3client.beans.NamespaceSwapper;
import ru.otr.integration.smev3client.beans.OperationSetter;
import ru.otr.integration.smev3client.config.camel.CustomMDCUnitOfWorkFactory;

/**
 * Created by tartanov.mikhail on 03.08.2016.
 */
@Configuration
@Profile("dev")
public class BeansConfig {

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
                context.setUnitOfWorkFactory(new CustomMDCUnitOfWorkFactory());
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {
            }
        };
    }
}
