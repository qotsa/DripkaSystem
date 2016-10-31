package ru.otr.integration.smev3client.pollers.routes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tartanov.mikhail on 16.08.2016.
 */

@Component
public class Routes extends SpringRouteBuilder {

    @Autowired
    HazelcastIdempotentRepository repository;

    @Override
    public void configure() throws Exception {
        Namespaces ns2 = new Namespaces("ns2", "http://otr.ru/irs/services/message-exchange/types");

        from("scheduler://foo?initialDelay=60s&delay=30s").routeId("GetRequestPoller")
        //from("quartz2://myTimer?trigger.repeatInterval=500&trigger.repeatCount=-1").routeId("GetRequestPoller")
            .transacted()
            .log("ping")
            .to("freemarker:templates/GetRequestRequest.ftl")
            .to("{{routes.smev3adapter}}")
            .choice()
                .when(header("ERROR_MESSAGE"))
                    .stop()
                .otherwise()
                    .choice()
                        .when().xpath("//*:GetRequestResponse[not(node())]") // if response is empty then stop
                        .stop()
                    .otherwise()
                        .idempotentConsumer(xpath("//*:MessageMetadata/*:MessageId").resultType(String.class), repository).skipDuplicate(false)
                        .filter(exchangeProperty(Exchange.DUPLICATE_MESSAGE).isEqualTo(true))
                            .to("activemq:queue:DuplicatesQueue")
                            .stop()
                        .end()
                        .to("{{routes.GetRequestPoller.GetRequestResponseQueue}}")
                        .log("pong")
                    .end()
            .end();

        /*from("scheduler://foo1?initialDelay=60s&delay=60s").routeId("GetResponsePoller")
            .transacted()
            .log("ping")
            .to("freemarker:templates/GetResponseRequest.ftl")
            .to("{{routes.smev3adapter}}")
            .choice()
                .when(header("ERROR_MESSAGE"))
                    .stop()
                .otherwise()
                    .choice()
                        .when().xpath("//*:GetResponseResponse[not(node())]") // if response is empty then stop
                            .stop()
                        .otherwise()
                            .idempotentConsumer(xpath("//*:MessageMetadata/*:MessageId").resultType(String.class), repository).skipDuplicate(false)
                            .filter(exchangeProperty(Exchange.DUPLICATE_MESSAGE).isEqualTo(true))
                                .to("activemq:queue:DuplicatesQueue")
                                .stop()
                            .end()
                            .to("{{routes.GetResponsePoller.GetResponseResponseQueue}}")
                            .log("pong")
                    .end()
            .end();

        from("scheduler://foo2?initialDelay=60s&delay=60s").routeId("GetStatusPoller")
            .transacted()
            .log("ping")
            .to("freemarker:templates/GetStatusRequest.ftl")
            .to("{{routes.smev3adapter}}")
            .choice()
                .when().xpath("//*:GetStatusResponse[not(node())]") // if response is empty then stop
                    .stop()
                .otherwise()
                    .idempotentConsumer(xpath("//ns2:OriginalMessageId").resultType(String.class).namespaces(ns2), repository).skipDuplicate(false)
                    .filter(exchangeProperty(Exchange.DUPLICATE_MESSAGE).isEqualTo(true))
                        .to("activemq:queue:DuplicatesQueue")
                        .stop()
                    .end()
                    .to("{{routes.GetStatusPoller.GetStatusResponseQueue}}")
                    .log("pong")
            .end();*/
    }
}
