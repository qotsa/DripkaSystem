package ru.otr.integration.smev3client.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by tartanov.mikhail on 31.08.2016.
 */
@Component
public class ReplicationRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("{{routes.replicationService.inboundQueue}}").routeId("replicationService")
                .transacted()
                .setHeader("messageReplicationAndVerification").simple("OK")//.method(ResponseRandomizer.class, "getOkorFailed")
                .to("{{routes.replicationService.outboundQueue}}");
    }

    private static class ResponseRandomizer {
        public static String getOkorFailed()  {
            return new Random().nextInt() % 2 == 0 ? "OK" : "FAILED";
        }
    }
}
