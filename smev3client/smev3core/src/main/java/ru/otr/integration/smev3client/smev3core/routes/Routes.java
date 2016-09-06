package ru.otr.integration.smev3client.smev3core.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.springframework.stereotype.Component;

@Component
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        Namespaces ns = new Namespaces("bas", "http://otr.ru/irs/services/message-exchange/types/basic")
                .add("typ2", "http://otr.ru/irs/services/message-exchange/types");

        from("{{routes.preprocessor.GetRequestResponseQueue}}").to("direct:preprocessor");
        from("{{routes.preprocessor.GetResponseResponseQueue}}").to("direct:preprocessor");
        from("direct:preprocessor").to("{{routes.preprocessor.inbound}}");

        from("{{routes.preprocessor.inbound}}")
                .transacted()
                .routeId("preprocessor")
                .choice()
                    .when(ns.xpath("//bas:FSAttachmentsList/bas:FSAttachment"))
                        .to("{{routes.preprocessor.replication}}").stop()
                    .otherwise()
                        .to("direct:postprocessor")
                .end();

        from("direct:postprocessor").to("{{routes.postprocessor.inbound}}");

        from("{{routes.postprocessor.inbound}}")
                .transacted()
                .routeId("postprocessor")
                .setHeader("recipient").xpath("//typ2:MessageMetadata/typ2:Recipient/typ2:Mnemonic/text()", ns)
                .choice()
                    .when(header("messageReplicationAndVerification").isNotEqualTo("OK"))
                        .to("{{routes.log}}").stop()
                    .otherwise()
                        .dynamicRouter(method(PostprocessorRouter.class, "route"))
                .end();
    }
}
