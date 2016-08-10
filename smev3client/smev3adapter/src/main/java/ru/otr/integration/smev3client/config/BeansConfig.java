package ru.otr.integration.smev3client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.otr.integration.smev3client.beans.NamespaceSwapper;
import ru.otr.integration.smev3client.beans.OperationSetter;

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

}
