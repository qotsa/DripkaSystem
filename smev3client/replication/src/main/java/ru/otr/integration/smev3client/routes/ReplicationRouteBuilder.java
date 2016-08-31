package ru.otr.integration.smev3client.routes;

import org.apache.camel.builder.RouteBuilder;

import java.util.Random;

/**
 * Created by tartanov.mikhail on 31.08.2016.
 */
public class ReplicationRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("{{routes.replicationService.inboundQueue}}").routeId("replicationService")
                .transacted()
                .setHeader("messageReplicationAndVerification").method(ResponseRandomizer.class, "getOkorFailed")
                .to("routes.replicationService.outboundQueue");
    }

    private static class ResponseRandomizer {
        private String getOkorFailed()  {
            return new Random().nextInt() % 2 == 0 ? "OK" : "FAILED";
        }
    }
}
