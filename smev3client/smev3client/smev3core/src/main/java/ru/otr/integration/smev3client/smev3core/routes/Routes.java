package ru.otr.integration.smev3client.smev3core.routes;

import org.apache.camel.LoggingLevel;
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
            .to("{{routes.Smev2Vis.postprocessor.inbound}}");

        from("{{routes.Smev2Vis.preprocessor.GetResponseResponseQueue}}").routeId("GetResponseResponse")
            .transacted()
            .to("{{routes.Smev2Vis.postprocessor.inbound}}");

        from("{{routes.Smev2Vis.GetStatusResponseQueue}}").routeId("GetStatusResponse")
            .transacted()
            .setHeader("messageReplicationAndVerification", simple("OK"))
            .to("{{routes.Smev2Vis.postprocessor.inbound}}");

        from("{{routes.Smev2Vis.postprocessor.inbound}}").routeId("Smev2VisPostprocessor")
            .onException(AckFailedException.class).useOriginalMessage()
                .handled(true)
                .to("{{routes.log}}")
            .end()
            .transacted()
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .setHeader("recipient").xpath("//*:MessageMetadata/*:Recipient/*:Mnemonic/text()", String.class)
            .choice()
                .when(header("messageReplicationAndVerification").isNotEqualTo("OK"))
                .otherwise()
                    .multicast().stopOnException()
                        .aggregationStrategy(AggregationStrategies.useOriginal()).aggregationStrategyMethodAllowNull()
                        .dynamicRouter(method(PostprocessorRouter.class, "route"))
                        .to("direct:ack")
                    .end()
            .end()
            .to("{{routes.log}}");

        // Ack

        from("direct:ack").routeId("ack")
            .errorHandler(noErrorHandler())
            .setHeader("businessMessageId", xpath("//*:MessageMetadata/*:MessageId/text()", String.class))
            .to("freemarker:templates/AckRequest.ftl")
            //.to("{{routes.smev3adapter}}")
            .choice().when(header("ERROR_MESSAGE"))
                .throwException(new AckFailedException("Ack Failed"))
            .end();

        // VIS => SMEV

        from("{{routes.Vis2Smev.preprocessor.inboundQueue}}").routeId("Vis2SmevPreprocessor")
            .transacted()
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .choice()
                .when(xpath("//*:FSAttachmentsList/*:FSAttachment"))
                    .to("{{routes.Vis2Smev.replication}}")
                    .setHeader("messageCopy", simple("OK"))
                .otherwise()
                    .to("{{routes.Vis2Smev.postprocessor.inbound}}")
            .end()
            .to("{{routes.log}}");

        from("{{routes.Vis2Smev.postprocessor.inbound}}").routeId("Vis2SmevPostprocessor")
            .transacted()
            .choice()
                .when(header("messageCopy").isNotEqualTo("OK"))
                    .to("{{routes.log}}")
                .otherwise()
                    .to("{{routes.Vis2Smev.pushers}}")
            .end();
    }
}
