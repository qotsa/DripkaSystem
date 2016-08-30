package ru.otr.integration.smev3client.routes.routers;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.config.AppProperties;

/**
 * Created by dez on 30/08/16.
 */
@Component
public class PreprocessorMetadataRouter extends MetadataRouter {

    @Autowired
    private AppProperties appProperties;

    @EndpointInject(uri = "{{smevToVisPreprocessor.queue.out.default}}")
    protected Endpoint outEndpoint;

    @Override
    public AppProperties getAppProperties() {
        return appProperties;
    }

    @Override
    public Endpoint getOutEndpoint() {
        return outEndpoint;
    }
}
