package ru.otr.integration.smev3client.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.routes.routers.MetadataRouter;

@Component
public class ProxyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        Namespaces ns = new Namespaces("bas", "http://otr.ru/irs/services/message-exchange/types/basic")
                .add("typ2", "http://otr.ru/irs/services/message-exchange/types");

        from("{{smevToVisPreprocessor.queue.in}}")
                .transacted()
                .routeId("smevToVisPreprocessor")
                .setHeader("recipient").xpath("//typ2:MessageMetadata/typ2:Recipient/typ2:Mnemonic/text()", ns)
                .choice().when(ns.xpath("//bas:FSAttachmentsList/bas:FSAttachment"))
                .to("{{smevToVisPreprocessor.queue.replication}}")
                .otherwise()
                .dynamicRouter(method(MetadataRouter.class, "route"));

        from("{{smevToVisPostprocessor.queue.in}}")
                .transacted()
                .routeId("smevToVisPostprocessor")
                .setHeader("recipient").xpath("//typ2:MessageMetadata/typ2:Recipient/typ2:Mnemonic/text()", ns)
                .choice().when(simple("${in.header.messageReplicationAndVerification} != &#39;OK&#39;"))
                .to("log:ru.otr.integration.smev3client.smevToVisPostprocessor?level=DEBUG&showAll=true&multiline=true")
                .stop()
                .otherwise()
                .dynamicRouter(method(MetadataRouter.class, "route"));
    }
}
