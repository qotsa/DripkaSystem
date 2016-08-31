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
                .to("{{routes.smevProxyService.log}}")
                .to("cxf:bean:smevMessageExchangeService")
                .setHeader("Direction", simple(NamespaceSwapper.Direction.FROM_SMEV))
                .to("bean:namespaceSwapper")
                .to("{{routes.smevProxyService.log}}");
    }
}
