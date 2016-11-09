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
        from("{{routes.replicationService.inboundQueue}}").routeId("replicationService")
            .errorHandler(errorHandlerBuilder)
            .onException(FtpPollFailedException.class).useOriginalMessage()
                .handled(true)
                .setHeader("messageReplicationAndVerification", simple("FAIL"))
                .to("{{routes.replicationService.outboundQueue}}")
            .end()
            .transacted()
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .multicast().stopOnException()
                .aggregationStrategy(AggregationStrategies.useOriginal()).aggregationStrategyMethodAllowNull()
                .to("direct:replicate")
            .end()
            .setHeader("messageReplicationAndVerification", simple("OK"))
            .to("{{routes.replicationService.outboundQueue}}")
            .log(LoggingLevel.DEBUG, "metrics", "pong");

        from("direct:replicate").routeId("ftpReplication")
            .errorHandler(noErrorHandler())
            .setHeader("originalMessageId", simple("${id}"))
            .to("xslt:xslt/extract_attachments.xsl")
            .split(xpath("//Attachments/Attachment")).stopOnException()
                .setHeader("attachmentUuid", xpath("//uuid/text()", String.class))
                .setHeader("attachmentFilename", xpath("//filename/text()", String.class))
                .setHeader("attachmentUsername", xpath("//username/text()", String.class))
                .setHeader("attachmentPassword", xpath("//password/text()", String.class))
                .pollEnrich().simple("{{routes.smev}}?username=${headers.attachmentUsername}&password=${headers.attachmentPassword}&disconnect=true&passiveMode=true&fileName=${headers.attachmentFilename}&localWorkDirectory=/replication_cache/${headers.breadcrumbid}")
                    .timeout(1000 * 60 * 60) // 1h is enough to download large file
                    .aggregationStrategy(new FtpPollingAggregationStrategy())
                    .aggregateOnException(false)
                .end()
                .choice()
                    .when(header("pollFailed"))
                        .throwException(new FtpPollFailedException("FTP Poll Failed"))
                    .otherwise()
                        .setHeader("CamelFileName", simple("${headers.breadcrumbid}/${headers.attachmentUuid}/${headers.attachmentFilename}"))
                        .to("{{routes.ftp}}")
                .end()
            .end();
    }
}
