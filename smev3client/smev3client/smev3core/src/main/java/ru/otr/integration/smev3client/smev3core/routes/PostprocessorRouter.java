package ru.otr.integration.smev3client.smev3core.routes;

import org.apache.camel.Header;
import org.apache.camel.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.smev3core.config.RoutesMetadataProperties;

import java.util.Map;

/**
 * Created by dez on 30/08/16.
 */
@Component
public class PostprocessorRouter {

    @Autowired
    private RoutesMetadataProperties metadataProperties;

    @Value("${routes.log}")
    private String log;

    public String route(@Header("recipient") String recipient, @Properties Map<String, Object> properties) {
        boolean isRouted = null != properties.get("isRouted") && (boolean) properties.get("isRouted");

        if (!isRouted) {
            properties.put("isRouted", true);
            String endpoint = metadataProperties.getMetadata().get(recipient);

            if (endpoint != null) {
                return endpoint;
            } else {
                return log;
            }
        } else {
            return null;
        }
    }
}
