package ru.otr.integration.smev3client.beans;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.camel.Exchange;

import org.apache.camel.Header;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.config.AppProperties;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tartanov.mikhail on 26.07.2016.
 */
@Component
public class NamespaceSwapper {
    @Autowired
    private AppProperties appProperties;

    public void swapNamespaces(Exchange exchange, @Header("Direction") String direction) throws Exception {
        String template = exchange.getIn().getBody(String.class);
        final BiMap namespaceMappings = HashBiMap.create(appProperties.getImmutableNamespaceMappings());
        StringBuffer sb1 = new StringBuffer();
        if (!template.isEmpty()) {
            switch (direction) {
                case Direction.TO_SMEV: {
                    //Compile single pattern with "" to avoid replacing in namespaces, consisting namespaces
                    String patternString = "\"" + StringUtils.join(appProperties.getImmutableNamespaceMappings().keySet(), "\"|\"") + "\"";
                    Pattern pattern = Pattern.compile(patternString);
                    Matcher matcher = pattern.matcher(template);
                    while (matcher.find()) {
                        matcher.appendReplacement(sb1, "\"" + namespaceMappings.get(matcher.group().substring(1, matcher.group().length() - 1)) + "\"");
                    }
                    matcher.appendTail(sb1);
                    break;
                }
                case Direction.FROM_SMEV: {
                    String patternString = "\"" + StringUtils.join(appProperties.getImmutableNamespaceMappings().values(), "\"|\"") + "\"";
                    Pattern pattern = Pattern.compile(patternString);
                    Matcher matcher = pattern.matcher(template);
                    while (matcher.find()) {
                        matcher.appendReplacement(sb1, "\"" + namespaceMappings.inverse().get(matcher.group().substring(1, matcher.group().length() - 1)) + "\"");
                    }
                    matcher.appendTail(sb1);
                    break;
                }
                default:
                    throw new RuntimeException("Direction is unknown!");
            }
        } else throw new RuntimeException("Request body is empty or null!");
        exchange.getIn().setBody(sb1);
    }

    public static class Direction {
        public static final String TO_SMEV = "to_smev";
        public static final String FROM_SMEV = "from_smev";

        private Direction() {}
    }
}

