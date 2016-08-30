package ru.otr.integration.smev3client.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.routers.PreprocessorRouter;

@Component
public class ProxyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        Namespaces ns = new Namespaces("bas", "http://otr.ru/irs/services/message-exchange/types/basic")
                .add("typ2", "http://otr.ru/irs/services/message-exchange/types");

        from("{{smevToVisPreprocessor.queue.in}}")
                .routeId("smevToVisPreprocessor")
                .setHeader("recipient").xpath("//typ2:MessageMetadata/typ2:Recipient/typ2:Mnemonic/text()", ns)
                .choice().when(ns.xpath("//bas:FSAttachmentsList/bas:FSAttachment"))
                .to("{{smevToVisPreprocessor.queue.replication}}")
                .otherwise()
                .dynamicRouter(method(PreprocessorRouter.class, "route"));

        from("{{smevToVisPostprocessor.queue.in}}")
                .routeId("smevToVisPostprocessor")
                .to("{{smevToVisPostprocessor.queue.out}}");
    }
}
