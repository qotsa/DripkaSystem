package ru.otr.integration.smev3client.pollers.routes;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.camel.builder.xml.Namespaces;

/**
 * Created by tartanov.mikhail on 16.08.2016.
 */

@Component
public class Routes extends RouteBuilder {

    @Autowired
    CamelContext camelContext;

    @Autowired
    HazelcastIdempotentRepository repository;

    @Override
    public void configure() throws Exception {
        Namespaces ns2 = new Namespaces("ns2", "http://otr.ru/irs/services/message-exchange/types");

        camelContext.setStreamCaching(true);

        from("scheduler://foo?initialDelay=70s&delay=70s").routeId("GetRequestPoller")
            .transacted()
            .to("freemarker:templates/GetRequestRequest.ftl")
            .to("http://smev3adapter:{{smev3adapter.http.port}}/camel/request")
            .choice()
                .when(header("ERROR_MESSAGE"))
                .stop()
            .otherwise()
                .to("{{routes.GetRequestPoller.GetRequestResponseQueue}}")
            .end();

        /*from("scheduler://foo1?initialDelay=120s&delay=60s").routeId("GetResponsePoller")
                .to("freemarker:templates/GetResponseRequest.ftl")
                .to("http://smev3adapter:8090/camel/request")
                .to("{{routes.GetResponsePoller.GetResponseResponseQueue}}activemq:GetResponseResponseQueue");*/

        /*from("scheduler://foo2?initialDelay=60s&delay=15s").routeId("GetStatusPoller")
                .transacted()
                .to("freemarker:templates/GetStatusRequest.ftl")
                .to("http://smev3adapter:{{smev3adapter.http.port}}/camel/request")
                .idempotentConsumer(xpath("//ns2:OriginalMessageId").resultType(String.class).namespaces(ns2), repository)
                .skipDuplicate(false)
                .filter(exchangeProperty(Exchange.DUPLICATE_MESSAGE).isEqualTo(true))
                .to("activemq:queue:DuplicatesQueue")
                .stop()
                .end()
                .to("{{routes.GetStatusPoller.GetStatusResponseQueue}}");*/
    }
}
