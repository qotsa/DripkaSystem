package ru.otr.integration.smev3client.smev3core.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.smev3core.exception.AckFailedException;

@Component
public class Routes extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        // SMEV => VIS

        from("{{routes.Smev2Vis.preprocessor.GetRequestResponseQueue}}").routeId("GetRequestResponse")
            .transacted()
            .to("direct:Smev2Vis_preprocessor");

        from("{{routes.Smev2Vis.preprocessor.GetResponseResponseQueue}}").routeId("GetResponseResponse")
            .transacted()
            .to("direct:Smev2Vis_preprocessor");

        from("direct:Smev2Vis_preprocessor").routeId("Smev2VisPreprocessor")
            .choice()
                .when(xpath("//*:FSAttachmentsList/*:FSAttachment"))
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
            .onException(AckFailedException.class).useOriginalMessage()
                .handled(true)
                .to("{{routes.log}}")
            .end()
            .transacted()
            .setHeader("recipient").xpath("//*:MessageMetadata/*:Recipient/*:Mnemonic/text()", String.class)
            .choice()
                .when(header("messageReplicationAndVerification").isNotEqualTo("OK"))
                    .to("{{routes.log}}")
                .otherwise()
                    .multicast().stopOnException()
                        .aggregationStrategy(AggregationStrategies.useOriginal()).aggregationStrategyMethodAllowNull()
                        .to("direct:ack")
                        .dynamicRouter(method(PostprocessorRouter.class, "route"))
                    .end()
            .end();

        // VIS => SMEV

        from("{{routes.Vis2Smev.preprocessor.inboundQueue}}").routeId("Vis2SmevPreprocessor")
            .transacted()
            .choice()
                .when(xpath("//*:FSAttachmentsList/*:FSAttachment"))
                    .to("{{routes.replication}}")
                .otherwise()
                    .to("direct:Vis2Smev_postprocessor")
            .end();

        from("direct:Vis2Smev_postprocessor").routeId("Vis2SmevPostprocessor")
            .to("{{routes.log}}")
            .to("{{routes.Vis2Smev.pushers}}");

        // Ack

        from("direct:ack").routeId("ack")
            .errorHandler(noErrorHandler())
            .log("ACK")
            .setHeader("businessMessageId", xpath("//*:MessageMetadata/*:MessageId/text()", String.class))
            .to("freemarker:templates/AckRequest.ftl")
            //.to("{{routes.smev3adapter}}")
            .choice().when(header("ERROR_MESSAGE"))
                .throwException(new AckFailedException("Ack Failed"))
            .end();
    }
}
