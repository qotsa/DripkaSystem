package ru.otr.integration.smev3client.beans;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.config.AppProperties;

/**
 * Created by tartanov.mikhail on 27.07.2016.
 */
@Component
public class OperationSetter implements Processor {
    @Autowired
    private AppProperties appProperties;

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        String targetOperation = appProperties.getImmutableOperations().stream().filter(body::contains).findFirst().orElseThrow(()-> new RuntimeException("Unable to find target operation in request to SMEV"));
        exchange.getIn().setHeader(CxfConstants.OPERATION_NAME, targetOperation);
        exchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, appProperties.getSmevServiceNamespace());
    }
}
