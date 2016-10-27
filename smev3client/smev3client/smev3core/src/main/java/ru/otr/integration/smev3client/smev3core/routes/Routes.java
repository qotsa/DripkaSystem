package ru.otr.integration.smev3client.smev3core.routes;

import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

@Component
public class Routes extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        Namespaces ns = new Namespaces("bas", "http://otr.ru/irs/services/message-exchange/types/basic")
                .add("typ2", "http://otr.ru/irs/services/message-exchange/types");

        // SMEV => VIS

        from("{{routes.Smev2Vis.preprocessor.GetRequestResponseQueue}}").routeId("GetRequestResponse")
            .transacted()
            .to("direct:Smev2Vis_preprocessor");

        from("{{routes.Smev2Vis.preprocessor.GetResponseResponseQueue}}").routeId("GetResponseResponse")
            .transacted()
            .to("direct:Smev2Vis_preprocessor");

        from("direct:Smev2Vis_preprocessor").routeId("Smev2VisPreprocessor")
            .choice()
                .when(ns.xpath("//bas:FSAttachmentsList/bas:FSAttachment"))
                    .to("{{routes.replication}}")
                .otherwise()
                    .setHeader("messageReplicationAndVerification", simple("OK"))
                    .to("direct:Smev2VisPostprocessor")
            .end();

        from("{{routes.Smev2Vis.GetStatusResponseQueue}}").routeId("GetStatusResponse")
            .transacted()
            .setHeader("messageReplicationAndVerification", simple("OK"))
            .to("direct:Smev2VisPostprocessor");

        from("direct:Smev2VisPostprocessor").to("{{routes.Smev2Vis.postprocessor.inbound}}");

        from("{{routes.Smev2Vis.postprocessor.inbound}}").routeId("Smev2VisPostprocessor")
            .transacted()
            .setHeader("recipient").xpath("//typ2:MessageMetadata/typ2:Recipient/typ2:Mnemonic/text()", String.class, ns)
            .choice()
                .when(header("messageReplicationAndVerification").isNotEqualTo("OK"))
                    .to("{{routes.log}}")
                .otherwise()
                    .multicast().stopOnException()
                        .aggregationStrategy(AggregationStrategies.useOriginal()).aggregationStrategyMethodAllowNull()
                        .to("direct:ack")
                        .dynamicRouter(method(PostprocessorRouter.class, "route"))
                    .end()
                    //.dynamicRouter(method(PostprocessorRouter.class, "route"))
            .end();

        // VIS => SMEV

        from("{{routes.Vis2Smev.preprocessor.SendRequestResponseQueue}}").routeId("SendRequestResponse")
            .transacted()
            .to("direct:Vis2SmevPreprocessor");

        from("{{routes.Vis2Smev.preprocessor.SendResponseResponseQueue}}").routeId("SendResponseResponse")
            .transacted()
            .to("direct:Vis2SmevPreprocessor");

        from("direct:Vis2SmevPreprocessor").routeId("Vis2SmevPreprocessor")
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

        // Ack

        from("direct:ack").routeId("ack")
            .log("ACK")
            .setHeader("businessMessageId", xpath("//*:MessageMetadata/*:MessageId/text()", String.class))
            .to("freemarker:templates/AckRequest.ftl")
            .to("{{routes.log}}");
    }
}
