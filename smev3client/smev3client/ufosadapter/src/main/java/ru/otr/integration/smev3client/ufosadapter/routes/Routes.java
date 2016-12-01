package ru.otr.integration.smev3client.ufosadapter.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.stereotype.Component;

/**
 * Created by nikitin.maksim on 29.08.2016.
 */
@Component
public class Routes extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("{{routes.toUfos.outboundQueue}}").routeId("UfosMessageTerminator")
            .multicast()
                .aggregationStrategy(AggregationStrategies.useOriginal()).aggregationStrategyMethodAllowNull()
                .choice()
                    .when(xpath("//*:FSAttachmentsList/*:FSAttachment"))
                        /*.to("xslt:xslt/extract_attachments.xsl")
                        .split(xpath("//Attachments/Attachment")).stopOnException()
                            .setHeader("attachmentUuid", xpath("//uuid/text()", String.class))
                            .setHeader("attachmentFilename", xpath("//filename/text()", String.class))
                            .pollEnrich().simple("{{routes.ftp.server}}/replication/${headers.breadcrumbid}/${headers.attachmentUuid}?username={{routes.ftp.user}}&password={{routes.ftp.pass}}&throwExceptionOnConnectFailed=true&disconnect=true&passiveMode=true&download=false&autoCreate=false&delete=true&fileName=${headers.attachmentFilename}")
                                .cacheSize(1) //-1 is not compatible with delete=true !!!
                                .timeout(1000 * 30)
                            .end()
                        .end()*/
                        .to("attachmentsTerminator")
                    .end()
                .end()
                .choice()
                    .when(xpath("//*:AttachmentHeaderList/*:AttachmentHeader"))
                        /*.to("xslt:xslt/extract_embedded_attachments.xsl")
                        .split(xpath("//Attachments/Attachment")).stopOnException()
                            .setHeader("contentId", xpath("//contentId/text()", String.class))
                            .pollEnrich().simple("{{routes.ftp.server}}/replication/${headers.breadcrumbid}?username={{routes.ftp.user}}&password={{routes.ftp.pass}}&throwExceptionOnConnectFailed=true&disconnect=true&passiveMode=true&download=false&autoCreate=false&delete=true&fileName=${headers.contentId}.raw")
                                .cacheSize(1)
                                .timeout(1000 * 30)
                            .end()
                        .end()*/
                        .to("attachmentsTerminator")
                    .end()
                .end()
            .end()
            .stop();
    }
}
