package ru.otr.integration.smev3client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * Created by dez on 29/08/16.
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "routes")
public class RoutesMetadataProperties {

    private Map<String, String> metadata;

    public Map<String, String> getMetadataImmutable() {
        return Collections.unmodifiableMap(metadata);
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

}
