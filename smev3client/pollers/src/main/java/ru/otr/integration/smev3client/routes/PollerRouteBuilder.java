package ru.otr.integration.smev3client.routes;

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
public class PollerRouteBuilder extends RouteBuilder {

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
                .to("velocity:velocity/GetRequestRequest.vm")
                .to("http://smev3adapter:{{smev3adapter.http.port}}/camel/request")
                .to("activemq:GetRequestResponseQueue");

        /*from("scheduler://foo1?initialDelay=120s&delay=60s").routeId("GetResponsePoller")
                .to("velocity:velocity/GetResponseRequest.vm")
                .to("http://smev3adapter:8090/camel/request")
                .to("activemq:GetResponseResponseQueue");*/

        from("scheduler://foo2?initialDelay=60s&delay=15s").routeId("GetStatusPoller")
                .transacted()
                .to("velocity:velocity/GetStatusRequest.vm")
                .to("http://smev3adapter:{{smev3adapter.http.port}}/camel/request")
                .idempotentConsumer(xpath("//ns2:OriginalMessageId").resultType(String.class).namespaces(ns2), repository)
                .skipDuplicate(false)
                .filter(exchangeProperty(Exchange.DUPLICATE_MESSAGE).isEqualTo(true))
                .to("activemq:DuplicatesQueue")
                .stop()
                .end()
                .to("activemq:GetStatusResponseQueue");
    }
}
