package ru.otr.integration.smev3client.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.beans.NamespaceSwapper;

/**
 * Created by tartanov.mikhail on 26.07.2016.
 */
@Component
public class ProxyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("servlet:///request")
            .routeId("smevProxyService")
            .setHeader("Direction", simple(NamespaceSwapper.Direction.TO_SMEV))
            .to("bean:namespaceSwapper")
            .to("bean:operationSetter")
                .to("log:ru.otr.integration.smev3client.smevProxyService?level=DEBUG&showAll=true&multiline=true")
            .to("cxf:bean:smevMessageExchangeService")
            .setHeader("Direction", simple(NamespaceSwapper.Direction.FROM_SMEV))
            .to("bean:namespaceSwapper")
                .to("log:ru.otr.integration.smev3client.smevProxyService?level=DEBUG&showAll=true&multiline=true");
    }
}
