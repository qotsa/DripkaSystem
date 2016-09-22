package ru.otr.integration.smev3client.replication.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.replication.config.FtpFileAggregationStrategy;

/**
 * Created by tartanov.mikhail on 31.08.2016.
 */
@Component
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("{{routes.replicationService.inboundQueue}}").routeId("replicationService")
            .onException(Throwable.class)
                .handled(true)
                .setHeader("messageReplicationAndVerification", simple("FAIL"))
                .to("{{routes.log}}")
            .end()

            .transacted()
            .setHeader("messageReplicationAndVerification", simple("OK"))
            .multicast().stopOnException().to("direct:replicate").end()
            .to("{{routes.replicationService.outboundQueue}}");

        from("direct:replicate").routeId("ftpReplication")
            .transacted()
            .setHeader("originalMessageId", simple("${id}"))
            .to("xslt:xslt/extract_attachments.xsl")
            .split(xpath("//Attachments/Attachment"))
                .setHeader("attachmentUuid", xpath("//uuid/text()", String.class))
                .setHeader("attachmentFilename", xpath("//filename/text()", String.class))
                .pollEnrich().simple("{{routes.smev.ftp}}?fileName=${headers.attachmentUuid}/${headers.attachmentFilename}").timeout(10000).aggregationStrategy(new FtpFileAggregationStrategy())
                .setHeader("CamelFileName", simple("${headers.originalMessageId}/${headers.attachmentUuid}/${headers.attachmentFilename}"))
                .to("{{routes.ftp}}")
            .end();
    }
}
