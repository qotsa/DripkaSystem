package ru.otr.integration.smev3client.replication.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by tartanov.mikhail on 31.08.2016.
 */
@Component
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("{{routes.replicationService.inboundQueue}}").routeId("replicationService")
                .transacted()
                .to("bean:responseRandomizer")
                .setHeader("CamelFileName", simple("${id}/body.xml"))
                .to("{{routes.ftp}}")
                .to("{{routes.replicationService.outboundQueue}}");
    }

    @Component("responseRandomizer")
    public class ResponseRandomizer {
        public void getOkorFailed(Exchange exchange)  {
            exchange.getIn().getHeaders().put("messageReplicationAndVerification", new Random().nextInt() % 2 == 0 ? "OK" : "FAILED");
        }
    }
}
