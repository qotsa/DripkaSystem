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

    @Autowired
    private RoutesProperties routesProperties;

    @Value("${routes.log}")
    private String log;

    public String route(@Header("recipient") String recipient, @Properties Map<String, Object> properties) {
        boolean isRouted = null != properties.get("isRouted") && (boolean) properties.get("isRouted");

        if (!isRouted) {
            properties.put("isRouted", true);
            String endpoint = routesProperties.getSlipEndpoints().get(recipient);

            if (endpoint != null) {
                return endpoint;
            } else {
                return log;
            }
        } else {
            return null;
        }
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
