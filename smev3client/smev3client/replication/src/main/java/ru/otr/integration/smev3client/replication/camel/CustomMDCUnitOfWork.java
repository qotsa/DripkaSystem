package ru.otr.integration.smev3client.replication.camel;

import org.apache.camel.Exchange;
import org.apache.camel.impl.MDCUnitOfWork;
import org.apache.camel.spi.RouteContext;
import org.slf4j.MDC;

/**
 * Created by nikitin.maksim on 23.08.2016.
 */
public class CustomMDCUnitOfWork extends MDCUnitOfWork {

    public static final String KEY_FROM_ROUTE_ID = "FromRouteID";
    public static final String KEY_CURRENT_ROUTE_ID = "CurrentRouteID";

    public CustomMDCUnitOfWork(Exchange exchange) {
        super(exchange);
        MDC.put(KEY_FROM_ROUTE_ID, exchange.getFromRouteId());
    }

    @Override
    public void pushRouteContext(RouteContext routeContext) {
        MDC.put(KEY_CURRENT_ROUTE_ID, routeContext.getRoute().getId());
        super.pushRouteContext(routeContext);
    }

    @Override
    public RouteContext popRouteContext() {
        MDC.remove(KEY_CURRENT_ROUTE_ID);
        return super.popRouteContext();
    }
}
