package ru.otr.integration.smev3client.routes.routers;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.Header;
import org.apache.camel.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.config.AppProperties;

import java.util.Map;

/**
 * Created by dez on 30/08/16.
 */
@Component
public class MetadataRouter {

    @Autowired
    private AppProperties appProperties;

    @EndpointInject(uri = "{{smevToVisPreprocessor.queue.out.default}}")
    protected Endpoint outEndpoint;

    public String route(String body, @Header("recipient") String recipient, @Properties Map<String, Object> properties) {
        boolean isRouted = null != properties.get("isRouted") && (boolean) properties.get("isRouted");

        if (!isRouted) {
            properties.put("isRouted", true);

            String ep = appProperties.getMetadataEndpointsImmutable().get(recipient);

            if (null != ep) {
                return ep;
            } else {
                return outEndpoint.getEndpointUri();
            }
        } else {
            return null;
        }
    }

}
