package ru.otr.integration.smev3client.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProxyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:queue:input1")
                .transacted()
                .routeId("smevToVisPreprocessor")
                .to("activemq:queue:output1");

        from("activemq:queue:input2")
                .transacted()
                .routeId("smevToVisPostprocessor")
                .to("activemq:queue:output2");
    }
}
