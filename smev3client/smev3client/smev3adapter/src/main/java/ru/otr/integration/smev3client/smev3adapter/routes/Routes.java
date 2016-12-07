package ru.otr.integration.smev3client.smev3adapter.routes;

import org.apache.camel.LoggingLevel;
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
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .setHeader("Direction", simple(NamespaceSwapper.Direction.TO_SMEV))
            .to("bean:namespaceSwapper")
            .to("bean:operationSetter")
//            .hystrix().id("callSmev")
//                .hystrixConfiguration()
//                    .fallbackEnabled(false)
//                .end()
                .to("cxf:bean:smevMessageExchangeService")
                .to("xslt:xslt/remove_smevsignature.xsl")
//            .onFallback()
//                .setHeader("ERROR_MESSAGE", simple("${exception.message}"))
//                .setBody(simple("oh nooooo"))
//                .to("{{routes.log}}")
//            .end()
            .setHeader("PayloadReferenceMessageID", xpath("//*:SenderProvidedRequestData/*:ReferenceMessageID/text()", String.class))
            .setHeader("PayloadMessageID", xpath("//*:SenderProvidedRequestData/*:MessageID/text()", String.class))
            .setHeader("Direction", simple(NamespaceSwapper.Direction.FROM_SMEV))
            .to("bean:namespaceSwapper")
            .to("{{routes.log}}")
            .log(LoggingLevel.DEBUG, "metrics", "pong");
    }
}
