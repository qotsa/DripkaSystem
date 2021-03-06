package ru.otr.integration.smev3client.pollers.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.processor.idempotent.hazelcast.HazelcastIdempotentRepository;
import org.apache.camel.spring.SpringRouteBuilder;
import org.apache.camel.spring.spi.TransactionErrorHandlerBuilder;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by tartanov.mikhail on 16.08.2016.
 */

@Component
public class Routes extends SpringRouteBuilder {

    @Autowired
    @Qualifier("transactionErrorHandlerBuilder")
    private TransactionErrorHandlerBuilder errorHandlerBuilder;

    @Autowired
    HazelcastIdempotentRepository repository;

    @Override
    public void configure() throws Exception {
        from("scheduler://foo?useFixedDelay=false&initialDelay=60s&delay=1000&scheduler.concurrentTasks=1").routeId("GetRequestPoller")
            .errorHandler(errorHandlerBuilder)
            .transacted()
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .to("freemarker:templates/GetRequestRequest.ftl")
            //.hystrix().id("call_smev3adapter")
            //    .hystrixConfiguration()
            //        .fallbackEnabled(false)
            //    .end()
                .to("{{routes.smev3adapter}}")
            //.end()
            .choice()
                .when(header("ERROR_MESSAGE"))
                    .stop()
                .otherwise()
                    /*.idempotentConsumer(xpath("//*:MessageMetadata/*:MessageId").resultType(String.class), repository).skipDuplicate(false)
                        .filter(exchangeProperty(Exchange.DUPLICATE_MESSAGE).isEqualTo(true))
                        .to("activemq:queue:DuplicatesQueue")
                        .stop()
                    .end()*/
                    .choice()
                        .when().xpath("//*:GetRequestResponse[not(node())]") // if response is empty then stop
                        .stop()
                    .otherwise()
                        .choice()
                            .when(xpath("//*:AttachmentHeaderList/*:AttachmentHeader"))
                                .to("seda:saveAttachments?size=100&concurrentConsumers=10")
                                .to("{{routes.log}}")
                                .stop()
                        .end()
                        .choice()
                            .when(xpath("//*:FSAttachmentsList/*:FSAttachment"))
                                .to("{{routes.replication}}")
                                .to("{{routes.log}}")
                                .stop()
                        .end()
                        .setHeader("messageReplicationAndVerification", simple("OK"))
                        .to("{{routes.GetRequestPoller.GetRequestResponseQueue}}")
                        .to("{{routes.log}}")
                    .end()
            .end();

        from("seda:saveAttachments?size=100&concurrentConsumers=10").routeId("saveAttachments")
            .errorHandler(errorHandlerBuilder)
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .multicast().stopOnException()
                .aggregationStrategy(AggregationStrategies.useOriginal()).aggregationStrategyMethodAllowNull()
                .to("direct:saveAttachmentsToFtp")
            .end()
            .to("xslt:xslt/replace_embedded_attachments.xsl")
            .setHeader("messageReplicationAndVerification", simple("OK"))
            .to("{{routes.GetRequestPoller.GetRequestResponseQueue}}");

        from("direct:saveAttachmentsToFtp").routeId("saveAttachmentsToFtp")
            .errorHandler(noErrorHandler())
            .to("xslt:xslt/extract_embedded_attachments.xsl")
            .split(xpath("//Attachments/Attachment")).stopOnException()
                .setHeader("contentId", xpath("//contentId/text()", String.class))
                .setHeader("CamelFileName", simple("embedded/${headers.breadcrumbid}/${headers.contentId}.raw"))
                .to("{{routes.ftp}}")
            .end();

        /*from("scheduler://foo1?initialDelay=60s&delay=60s").routeId("GetResponsePoller")
            .transacted()
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .to("freemarker:templates/GetResponseRequest.ftl")
            .to("{{routes.smev3adapter}}")
            .choice()
                .when(header("ERROR_MESSAGE"))
                    .stop()
                .otherwise()
                    .choice()
                        .when().xpath("//*:GetResponseResponse[not(node())]") // if response is empty then stop
                            .stop()
                        .otherwise()
                            .idempotentConsumer(xpath("//*:MessageMetadata/*:MessageId").resultType(String.class), repository).skipDuplicate(false)
                            .filter(exchangeProperty(Exchange.DUPLICATE_MESSAGE).isEqualTo(true))
                                .to("activemq:queue:DuplicatesQueue")
                                .stop()
                            .end()
                            .to("{{routes.GetResponsePoller.GetResponseResponseQueue}}")
                            .log(LoggingLevel.DEBUG, "metrics", "pong")
                    .end()
            .end();

        from("scheduler://foo2?initialDelay=60s&delay=60s").routeId("GetStatusPoller")
            .transacted()
            .log(LoggingLevel.DEBUG, "metrics", "ping")
            .to("freemarker:templates/GetStatusRequest.ftl")
            .to("{{routes.smev3adapter}}")
            .choice()
                .when().xpath("//*:GetStatusResponse[not(node())]") // if response is empty then stop
                    .stop()
                .otherwise()
                    .idempotentConsumer(xpath("//*:OriginalMessageId").resultType(String.class), repository).skipDuplicate(false)
                    .filter(exchangeProperty(Exchange.DUPLICATE_MESSAGE).isEqualTo(true))
                        .to("activemq:queue:DuplicatesQueue")
                        .stop()
                    .end()
                    .to("{{routes.GetStatusPoller.GetStatusResponseQueue}}")
                    .log(LoggingLevel.DEBUG, "metrics", "pong")
            .end();*/
    }
}
