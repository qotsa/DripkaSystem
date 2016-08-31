package ru.otr.integration.smev3client.routes.routers;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.config.RoutesMetadataProperties;

/**
 * Created by dez on 30/08/16.
 */
@Component
public class PreprocessorMetadataRouter extends MetadataRouter {

    @Autowired
    private RoutesMetadataProperties metadataProperties;

    @EndpointInject(uri = "{{routes.preprocessor.outbound.default}}")
    protected Endpoint outEndpoint;

    @Override
    public RoutesMetadataProperties getMetadataProperties() {
        return metadataProperties;
    }

    @Override
    public Endpoint getOutEndpoint() {
        return outEndpoint;
    }
}
