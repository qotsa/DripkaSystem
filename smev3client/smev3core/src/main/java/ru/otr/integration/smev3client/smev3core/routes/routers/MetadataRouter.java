package ru.otr.integration.smev3client.smev3core.routes.routers;

import org.apache.camel.Endpoint;
import org.apache.camel.Header;
import org.apache.camel.Properties;
import ru.otr.integration.smev3client.smev3core.config.RoutesMetadataProperties;

import java.util.Map;

/**
 * Created by dez on 30/08/16.
 */
public abstract class MetadataRouter {

    public String route(String body, @Header("recipient") String recipient, @Properties Map<String, Object> properties) {
        boolean isRouted = null != properties.get("isRouted") && (boolean) properties.get("isRouted");

        if (!isRouted) {
            properties.put("isRouted", true);

            String ep = getMetadataProperties().getMetadataImmutable().get(recipient);

            if (null != ep) {
                return ep;
            } else {
                return getOutEndpoint().getEndpointUri();
            }
        } else {
            return null;
        }
    }

    protected abstract Endpoint getOutEndpoint();

    protected abstract RoutesMetadataProperties getMetadataProperties();

}
