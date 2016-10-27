package ru.otr.integration.smev3client.smev3core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by dez on 29/08/16.
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "routes")
public class RoutesProperties {

    private Map<String, String> slipEndpoints;

    public Map<String, String> getSlipEndpoints() {
        return slipEndpoints;
    }

    public void setSlipEndpoints(Map<String, String> slipEndpoints) {
        this.slipEndpoints = slipEndpoints;
    }
}
