package ru.otr.integration.smev3client.pollers.config;

import org.jolokia.http.AgentServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * Created by tartanov.mikhail on 26.07.2016.
 */

@Configuration
@Profile({"dev", "test"})
public class ServletConfig {
    private static final String JOLOKIA_URL_MAPPING = "/jolokia/*";
    private static final String JOLOKIA_SERVLET_NAME = "JolokiaAgent";

    @Bean
    public ServletRegistrationBean jolokiaServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new AgentServlet(), JOLOKIA_URL_MAPPING);
        registration.setName(JOLOKIA_SERVLET_NAME);
        return registration;
    }
}
