package ru.otr.integration.smev3client.ufosadapter.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

/**
 * Created by nikitin.maksim on 29.08.2016.
 */
@Component
public class Routes extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        /*from("{{routes.toUfos.inboundQueue}}")
            .transacted("PROPAGATION_REQUIRED")
            .routeId("toUfos")
            .to("{{routes.toUfos.log}}")
            .to("{{routes.toUfos.outboundQueue}}");

        from("scheduler://foo?initialDelay=10s&delay=10s")
            .transacted("PROPAGATION_REQUIRES_NEW")
            .routeId("test")
            .to("{{routes.toUfos.inboundQueue}}");*/
    }
}
