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

    /*public static final String PROCESSING_STEP = "PROCESSING_STEP";
    public static final String ACK_ROUTE = "direct:ack";

    @Autowired
    private RoutesProperties routesProperties;

    @Value("${routes.log}")
    private String log;

    public String route(@Header("recipient") String recipient, @Properties Map<String, Object> properties) {
        int step = properties.get(PROCESSING_STEP) != null ? (int) properties.get(PROCESSING_STEP) : 0;

        switch(step) {
            case 0:
                properties.put(PROCESSING_STEP, 1);
                return ACK_ROUTE;
            case 1:
                properties.put(PROCESSING_STEP, 2);
                String endpoint = routesProperties.getSlipEndpoints().get(recipient);
                return endpoint != null ? endpoint : log;
            case 2:
            default:
                properties.remove(PROCESSING_STEP);
                return null;
        }
    }*/
}
