package ru.otr.integration.smev3client.smev3adapter.config;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.jolokia.http.AgentServlet;
//import org.springframework.boot.context.embedded.ServletRegistrationBean;
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
    private static final String CAMEL_URL_MAPPING = "/camel/*";
    private static final String CAMEL_SERVLET_NAME = "CamelServlet";
    private static final String JOLOKIA_URL_MAPPING = "/jolokia/*";
    private static final String JOLOKIA_SERVLET_NAME = "JolokiaAgent";

    @Bean
    public ServletRegistrationBean camelServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), CAMEL_URL_MAPPING);
        registration.setName(CAMEL_SERVLET_NAME);
        return registration;
    }

    @Bean
    public ServletRegistrationBean jolokiaServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new AgentServlet(), JOLOKIA_URL_MAPPING);
        registration.setName(JOLOKIA_SERVLET_NAME);
        return registration;
    }
}
