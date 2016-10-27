package ru.otr.integration.smev3client.smev3core.routes;

import org.apache.camel.Header;
import org.apache.camel.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.smev3core.config.RoutesProperties;

import java.util.Map;

/**
 * Created by dez on 30/08/16.
 */
@Component
public class PostprocessorRouter {

    public static final String IS_ROUTED = "isRouted";

    @Autowired
    private RoutesProperties routesProperties;

    @Value("${routes.log}")
    private String log;

    public String route(@Header("recipient") String recipient, @Properties Map<String, Object> properties) {
        boolean isRouted = properties.get(IS_ROUTED) != null && (boolean) properties.get(IS_ROUTED);

        if (isRouted) {
            properties.remove(IS_ROUTED);
            return null;
        }

        properties.put(IS_ROUTED, true);
        String endpoint = routesProperties.getSlipEndpoints().get(recipient);

        return endpoint != null ? endpoint : log;
    }
}
