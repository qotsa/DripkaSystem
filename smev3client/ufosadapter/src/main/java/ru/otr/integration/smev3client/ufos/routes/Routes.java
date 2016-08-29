package ru.otr.integration.smev3client.ufos.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by nikitin.maksim on 29.08.2016.
 */
@Component
public class Routes extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:queue:ufosAdapterInboundQueue?transacted=true")
            .routeId("toUfos")
            .transacted()
            .to("log:ru.otr.integration.smev3client.ufosadapter?level=DEBUG&showAll=true&multiline=true")
            .to("activemq:queue:ufosAdapterOutboundQueue");

        from("scheduler://foo?initialDelay=10s&delay=10s")
                .routeId("test")
                .transacted()
                .to("activemq:queue:ufosAdapterInboundQueue");
    }
}
