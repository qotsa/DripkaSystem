package ru.otr.integration.smev3client.smev3adapter.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.smev3adapter.beans.NamespaceSwapper;

/**
 * Created by tartanov.mikhail on 26.07.2016.
 */
@Component
public class Routes extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("servlet:///request").routeId("smevProxyService")
            .onException(Throwable.class)
                .handled(true)
                .setHeader("ERROR_MESSAGE", simple("${exception.message}"))
                .setBody(simple("${exception.stacktrace}"))
                .to("{{routes.log}}")
            .end()
            .setHeader("Direction", simple(NamespaceSwapper.Direction.TO_SMEV))
            .to("{{routes.log}}")
            .to("bean:namespaceSwapper")
            .to("bean:operationSetter")
            .to("cxf:bean:smevMessageExchangeService")
            .setHeader("Direction", simple(NamespaceSwapper.Direction.FROM_SMEV))
            .to("bean:namespaceSwapper")
            .to("{{routes.log}}");
    }
}
