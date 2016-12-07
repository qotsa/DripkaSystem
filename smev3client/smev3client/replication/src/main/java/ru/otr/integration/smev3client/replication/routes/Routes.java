package ru.otr.integration.smev3client.replication.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.spring.spi.TransactionErrorHandlerBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.replication.config.FtpPollingAggregationStrategy;
import ru.otr.integration.smev3client.replication.exception.FtpPollFailedException;

/**
 * Created by tartanov.mikhail on 31.08.2016.
 */
@Component
public class Routes extends SpringRouteBuilder {

    @Autowired
    @Qualifier("transactionErrorHandlerBuilder")
    private TransactionErrorHandlerBuilder errorHandlerBuilder;

    @Override
    public void configure() throws Exception {
        from("{{routes.smev2vis.inboundQueue}}").routeId("smev2visReplicationService")
            .errorHandler(errorHandlerBuilder)
            .onException(FtpPollFailedException.class).useOriginalMessage()
                .handled(true)
                .setHeader("messageReplicationAndVerification", simple("FAIL"))
                .to("{{routes.smev2vis.outboundQueue}}")
            .end()
            .transacted()
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .multicast().stopOnException()
                .aggregationStrategy(AggregationStrategies.useOriginal()).aggregationStrategyMethodAllowNull()
                .to("direct:replicateSmev2Vis")
            .end()
            .setHeader("messageReplicationAndVerification", simple("OK"))
            .to("{{routes.log}}")
            .log(LoggingLevel.DEBUG, "metrics", "pong")
            .to("{{routes.smev2vis.outboundQueue}}");

        from("direct:replicateSmev2Vis").routeId("ftpReplicationSmev2Vis")
            .errorHandler(noErrorHandler())
            .setHeader("originalMessageId", simple("${id}"))
            .to("xslt:xslt/extract_attachments.xsl")
            .split(xpath("//Attachments/Attachment")).stopOnException()
                .setHeader("attachmentUuid", xpath("//uuid/text()", String.class))
                .setHeader("attachmentFilename", xpath("//filename/text()", String.class))
                .setHeader("attachmentUsername", xpath("//username/text()", String.class))
                .setHeader("attachmentPassword", xpath("//password/text()", String.class))
                .pollEnrich().simple("{{routes.smev2vis.ftpsmev}}?username=${headers.attachmentUsername}&password=${headers.attachmentPassword}&disconnect=true&passiveMode=true&fileName=${headers.attachmentFilename}&localWorkDirectory=/replication_cache/${headers.breadcrumbid}")
                    .cacheSize(-1)
                    .timeout(1000 * 60 * 60) // 1h is enough to download large file
                    .aggregationStrategy(new FtpPollingAggregationStrategy())
                    .aggregateOnException(false)
                .end()
                .choice()
                    .when(header("pollFailed"))
                        .throwException(new FtpPollFailedException("FTP Poll Failed"))
                    .otherwise()
                        .setHeader("CamelFileName", simple("fs/${headers.breadcrumbid}/${headers.attachmentUuid}/${headers.attachmentFilename}"))
                        .to("{{routes.smev2vis.ftp}}")
                .end()
            .end();

        from("{{routes.vis2smev.inboundQueue}}").routeId("vis2smevCopyService")
            .errorHandler(errorHandlerBuilder)
            .onException(FtpPollFailedException.class).useOriginalMessage()
                .handled(true)
                .setHeader("messageCopy", simple("FAIL"))
                .to("{{routes.vis2smev.outboundQueue}}")
            .end()
            .transacted()
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .multicast().stopOnException()
                .aggregationStrategy(AggregationStrategies.useOriginal()).aggregationStrategyMethodAllowNull()
                .to("direct:copyVis2Smev")
            .end()
            .to("xslt:xslt/remove_fsattachments.xsl")
            .setHeader("messageCopy", simple("OK"))
            .to("{{routes.log}}")
            .log(LoggingLevel.DEBUG, "metrics", "pong")
            .to("{{routes.vis2smev.outboundQueue}}");

        from("direct:copyVis2Smev").routeId("ftpCopyVis2Smev")
            .errorHandler(noErrorHandler())
            .setHeader("originalMessageId", simple("${id}"))
            .to("xslt:xslt/extract_attachments.xsl")
            .split(xpath("//Attachments/Attachment")).stopOnException()
                .setHeader("attachmentUuid", xpath("//uuid/text()", String.class))
                .setHeader("attachmentFilename", xpath("//filename/text()", String.class))
                .pollEnrich().simple("{{routes.vis2smev.ftpvis}}&fileName=${headers.attachmentFilename}&localWorkDirectory=/replication_cache/${headers.breadcrumbid}")
                    .cacheSize(-1)
                    .timeout(1000 * 60 * 60)
                    .aggregationStrategy(new FtpPollingAggregationStrategy())
                    .aggregateOnException(false)
                .end()
                .choice()
                    .when(header("pollFailed"))
                        .throwException(new FtpPollFailedException("FTP Poll Failed"))
                    .otherwise()
                        .setHeader("CamelFileName", simple("${headers.breadcrumbid}/${headers.attachmentUuid}/${headers.attachmentFilename}"))
                        .to("{{routes.vis2smev.ftpsmev}}")
                .end()
            .end();
    }
}
