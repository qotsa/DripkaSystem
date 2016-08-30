package ru.otr.integration.smev3client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * Created by dez on 29/08/16.
 */
@Component
@ConfigurationProperties
public class AppProperties {

    private Map<String, String> metadataEndpoints;

    public Map<String, String> getMetadataEndpointsImmutable() {
        return Collections.unmodifiableMap(metadataEndpoints);
    }

    public Map<String, String> getMetadataEndpoints() {
        return metadataEndpoints;
    }

    public void setMetadataEndpoints(Map<String, String> metadataEndpoints) {
        this.metadataEndpoints = metadataEndpoints;
    }

}
