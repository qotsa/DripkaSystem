package ru.otr.integration.smev3client.ufos.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by nikitin.maksim on 29.08.2016.
 */
@Component
public class Routes extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activemq:queue:ufosAdapterInboundQueue")
            .routeId("toUfos")
            .transacted("PROPAGATION_REQUIRED")
            .to("log:ru.otr.integration.smev3client.ufosadapter?level=DEBUG&showAll=true&multiline=true")
            .to("activemq:queue:ufosAdapterOutboundQueue");

        from("scheduler://foo?initialDelay=10s&delay=10s")
            .routeId("test")
            .transacted("PROPAGATION_REQUIRES_NEW")
            .to("activemq:queue:ufosAdapterInboundQueue");
    }
}
