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

        from("{{routes.Smev2Vis.preprocessor.GetRequestResponseQueue}}").to("direct:Smev2Vis_preprocessor");
        from("{{routes.Smev2Vis.preprocessor.GetResponseResponseQueue}}").to("direct:Smev2Vis_preprocessor");
        from("direct:Smev2Vis_preprocessor").to("{{routes.Smev2Vis.preprocessor.inbound}}");

        from("{{routes.Smev2Vis.preprocessor.inbound}}")
            .transacted()
            .routeId("Smev2VisPreprocessor")
            .choice()
                .when(ns.xpath("//bas:FSAttachmentsList/bas:FSAttachment"))
                    .to("{{routes.replication}}")
                .otherwise()
                    .setHeader("messageReplicationAndVerification", simple("OK"))
                    .to("direct:Smev2Vis_postprocessor")
            .end();

        from("{{routes.Smev2Vis.GetStatusResponseQueue}}")
            .transacted()
            .routeId("Smev2VisGetStatusPreprocessor")
            .setHeader("messageReplicationAndVerification", simple("OK"))
            .to("direct:Smev2Vis_postprocessor");

        from("direct:Smev2Vis_postprocessor").to("{{routes.Smev2Vis.postprocessor.inbound}}");

        from("{{routes.Smev2Vis.postprocessor.inbound}}")
            .transacted()
            .routeId("Smev2VisPostprocessor")
            .setHeader("recipient").xpath("//typ2:MessageMetadata/typ2:Recipient/typ2:Mnemonic/text()", ns)
            .choice()
                .when(header("messageReplicationAndVerification").isNotEqualTo("OK"))
                    .to("{{routes.log}}")
                .otherwise()
                    .dynamicRouter(method(PostprocessorRouter.class, "route"))
            .end();

        from("{{routes.Vis2Smev.preprocessor.SendRequestResponseQueue}}").to("direct:Vis2Smev_preprocessor");
        from("{{routes.Vis2Smev.preprocessor.SendResponseResponseQueue}}").to("direct:Vis2Smev_preprocessor");
        from("direct:Vis2Smev_preprocessor").to("{{routes.Vis2Smev.preprocessor.inbound}}");

        from("{{routes.Vis2Smev.preprocessor.inbound}}")
            .transacted()
            .routeId("Vis2SmevPreprocessor")
            .choice()
                .when(ns.xpath("//bas:FSAttachmentsList/bas:FSAttachment"))
                    .to("{{routes.replication}}")
                .otherwise()
                    .to("direct:Vis2Smev_postprocessor")
            .end();

        from("direct:Vis2Smev_postprocessor").to("{{routes.Vis2Smev.postprocessor.inbound}}");

        from("{{routes.Vis2Smev.postprocessor.inbound}}")
            .transacted()
            .routeId("Vis2SmevPostprocessor")
            .to("{{routes.log}}")
            .to("{{routes.Vis2Smev.inbound}}");
    }
}
