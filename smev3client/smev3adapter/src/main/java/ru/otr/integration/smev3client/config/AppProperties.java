package ru.otr.integration.smev3client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by tartanov.mikhail on 27.07.2016.
 */

@Component
@ConfigurationProperties//(prefix = "dev")
public class AppProperties {
    private String smevServiceNamespace;
    private List<String> operations;
    private Map<String, String> namespaceMappings;
    public Map<String, String> getNamespaceMappings() {
        return namespaceMappings;
    }
    public Map<String, String> getImmutableNamespaceMappings() {
        return Collections.unmodifiableMap(namespaceMappings);
    }

    public void setNamespaceMappings(Map<String, String> namespaceMappings) {
        this.namespaceMappings = namespaceMappings;
    }

    public List<String> getOperations() {
        return operations;
    }

    public List<String> getImmutableOperations() {
        return Collections.unmodifiableList(operations);
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public String getSmevServiceNamespace() {
        return smevServiceNamespace;
    }

    public void setSmevServiceNamespace(String namespace) {
        this.smevServiceNamespace = namespace;
    }
}
