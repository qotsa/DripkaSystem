package ru.otr.integration.smev3client.smev3adapter.config.camel;

import org.apache.camel.Exchange;
import org.apache.camel.impl.MDCUnitOfWork;
import org.slf4j.MDC;

/**
 * Created by nikitin.maksim on 23.08.2016.
 */
public class CustomMDCUnitOfWork extends MDCUnitOfWork {

    public CustomMDCUnitOfWork(Exchange exchange) {
        super(exchange);
        //MDC.put("Key", "Value");
    }
}
