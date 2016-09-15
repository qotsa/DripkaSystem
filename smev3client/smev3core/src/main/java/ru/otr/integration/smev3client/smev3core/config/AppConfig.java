package ru.otr.integration.smev3client.smev3core.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.jolokia.http.AgentServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tartanov.mikhail on 31.08.2016.
 */
@Configuration
public class AppConfig {
    private static final String JOLOKIA_URL_MAPPING = "/jolokia/*";
    private static final String JOLOKIA_SERVLET_NAME = "JolokiaAgent";

    @Bean
    public ServletRegistrationBean jolokiaServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new AgentServlet(), JOLOKIA_URL_MAPPING);
        registration.setName(JOLOKIA_SERVLET_NAME);
        return registration;
    }

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
