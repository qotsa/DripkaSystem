package ru.otr.integration.smev3client.replication.config;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by nikitin.maksim on 21.09.2016.
 */
public class FtpFileAggregationStrategy implements AggregationStrategy {

    public FtpFileAggregationStrategy() {
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) return newExchange;
        //if (newExchange == null) return null;

        if (newExchange == null) {
            oldExchange.getIn().setHeader("pollFailed", true);
            return oldExchange;
        }

        newExchange.getIn().setHeader("originalMessageId", oldExchange.getIn().getHeader("originalMessageId"));
        newExchange.getIn().setHeader("attachmentUuid", oldExchange.getIn().getHeader("attachmentUuid"));
        newExchange.getIn().setHeader("attachmentFilename", oldExchange.getIn().getHeader("attachmentFilename"));

        return newExchange;
    }
}
