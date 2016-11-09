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
        from("{{routes.toUfos.outboundQueue}}").routeId("UfosEliminator")
                .stop();
    }
}
