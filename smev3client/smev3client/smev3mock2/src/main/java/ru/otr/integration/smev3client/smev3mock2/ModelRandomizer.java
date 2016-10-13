package ru.otr.integration.smev3client.smev3mock2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otr.integration.smev3client.smev3mock2.config.AppProperties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by tartanov.mikhail on 10.10.2016.
 */

@Component
public class ModelRandomizer {
    @Autowired
    AppProperties appProperties;

    Map<String, String> getRandomizedModel() {
        Map<String, String> randomizedModel = new HashMap<>();
        randomizedModel.put("MessageId", String.valueOf(UUID.randomUUID()));
        randomizedModel.put("ReferenceMessageID", String.valueOf(UUID.randomUUID()));
            /*randomizedModel.put("FileName1", RandomStringUtils.random(6, "zxcvbnmasdfghjklqwertyuiop1234567890") + ".zip");
            randomizedModel.put("FileName2", RandomStringUtils.random(6, "zxcvbnmasdfghjklqwertyuiop1234567890") + ".zip");
*/
        //no random yet
        randomizedModel.put("FileName1", "Enterprise OSGi in Action.zip");
        randomizedModel.put("FileName2", "the-devops.zip");

        randomizedModel.put("user", appProperties.getUser());
        randomizedModel.put("password", appProperties.getPassword());

        return Collections.unmodifiableMap(randomizedModel);
    }

}
