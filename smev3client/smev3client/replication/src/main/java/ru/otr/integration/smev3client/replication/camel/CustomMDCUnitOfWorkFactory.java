package ru.otr.integration.smev3client.replication.camel;

import org.apache.camel.Exchange;
import org.apache.camel.spi.UnitOfWork;
import org.apache.camel.spi.UnitOfWorkFactory;

/**
 * Created by nikitin.maksim on 23.08.2016.
 */
public class CustomMDCUnitOfWorkFactory implements UnitOfWorkFactory {
    @Override
    public UnitOfWork createUnitOfWork(Exchange exchange) {
        return new CustomMDCUnitOfWork(exchange);
    }
}
