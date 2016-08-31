package ru.otr.integration.smev3client.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.routes.routers.PostprocessorMetadataRouter;
import ru.otr.integration.smev3client.routes.routers.PreprocessorMetadataRouter;

@Component
public class ProxyRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        Namespaces ns = new Namespaces("bas", "http://otr.ru/irs/services/message-exchange/types/basic")
                .add("typ2", "http://otr.ru/irs/services/message-exchange/types");

        from("{{routes.preprocessor.inbound}}")
                .transacted()
                .routeId("smevToVisPreprocessor")
                .setHeader("recipient").xpath("//typ2:MessageMetadata/typ2:Recipient/typ2:Mnemonic/text()", ns)
                .choice()
                    .when(ns.xpath("//bas:FSAttachmentsList/bas:FSAttachment"))
                        .to("{{routes.preprocessor.outbound.replication}}").stop()
                    .otherwise()
                        .dynamicRouter(method(PreprocessorMetadataRouter.class, "route"));

        from("{{routes.postprocessor.inbound}}")
                .transacted()
                .routeId("smevToVisPostprocessor")
                .setHeader("recipient").xpath("//typ2:MessageMetadata/typ2:Recipient/typ2:Mnemonic/text()", ns)
                .choice().
                    when(header("messageReplicationAndVerification").isNotEqualTo("OK"))
                        .to("{{routes.postprocessor.outbound.default}}").stop()
                    .otherwise()
                        .dynamicRouter(method(PostprocessorMetadataRouter.class, "route"));
    }
}
