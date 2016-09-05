package ru.otr.integration.smev3client.pollers.config;

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
}
