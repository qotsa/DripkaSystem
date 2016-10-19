package ru.otr.integration.smev3client.replication.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.replication.config.FtpPollingAggregationStrategy;

/**
 * Created by tartanov.mikhail on 31.08.2016.
 */
@Component
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("{{routes.replicationService.inboundQueue}}").routeId("replicationService")
            .onException(Throwable.class).useOriginalMessage()
                .handled(true)
                .setHeader("messageReplicationAndVerification", simple("FAIL"))
                .to("{{routes.log}}")
                .to("{{routes.replicationService.outboundQueue}}")
            .end()
            .transacted()
            .threads(1, 1)
            .log("ping")
            .multicast().stopOnException()
                .aggregationStrategy(AggregationStrategies.useOriginal()).aggregationStrategyMethodAllowNull()
                .to("direct:replicate")
            .end()
            .setHeader("messageReplicationAndVerification", simple("OK"))
            .to("{{routes.replicationService.outboundQueue}}")
            .log("pong");

        from("direct:replicate").routeId("ftpReplication")
            .errorHandler(noErrorHandler())
            .setHeader("originalMessageId", simple("${id}"))
            .to("xslt:xslt/extract_attachments.xsl")
            .to("{{routes.log}}")
            .split(xpath("//Attachments/Attachment")).stopOnException()
                .setHeader("attachmentUuid", xpath("//uuid/text()", String.class))
                .setHeader("attachmentFilename", xpath("//filename/text()", String.class))
                .pollEnrich().simple("ftp://{{routes.smev.host}}:{{routes.smev.port}}?username={{routes.smev.username}}&password={{routes.smev.password}}&disconnect=true&passiveMode=true&fileName=${headers.attachmentFilename}&localWorkDirectory=/replication_cache/${headers.breadcrumbid}")
                    .timeout(1000 * 60 * 60) // optimal choice, set timeout which is enough to download large file
                    //.timeout(10000) // will fail with large attachment
                    //.timeout(-1) // will block if attachment file does not exist
                    .aggregationStrategy(new FtpPollingAggregationStrategy())
                    .aggregateOnException(true)
                .end()
                .choice()
                    .when(header("pollFailed"))
                        .throwException(new Exception("FTP Poll Failed"))
                    .otherwise()
                        .setHeader("CamelFileName", simple("${headers.originalMessageId}/${headers.attachmentUuid}/${headers.attachmentFilename}"))
                        // TODO error check!!!
                        .to("{{routes.ftp}}")
                .end()
            .end();
    }
}
