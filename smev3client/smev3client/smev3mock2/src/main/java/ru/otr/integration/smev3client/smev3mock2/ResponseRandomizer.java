package ru.otr.integration.smev3client.smev3mock2;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


import ru.otr.integration.smev3client.smev3mock2.config.AppProperties;

import ru.otr.integration.smev3client.smev3mock2.ftp.FtpServerSmevMock;
import ru.otr.integration.smev3client.smev3mock2.util.RandomCollection;


import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;


/**
 * Created by tartanov.mikhail on 06.10.2016.
 */


@Component
public class ResponseRandomizer {

    private final Logger logger = LoggerFactory.getLogger(ResponseRandomizer.class);

    @Autowired
    Configuration configuration;

    @Autowired
    AppProperties appProperties;

    @Autowired
    FtpServerSmevMock ftpServer;

    @Autowired
    LoadProfile loadProfile;

    @Autowired
    ModelRandomizer modelRandomizer;

    @Autowired
    ApplicationContext applicationContext;

    private RandomCollection<Template> responses;

    @PostConstruct
    public void postConstructSetup() {
        logger.debug("Entering postConstructSetup()");
        responses = new RandomCollection<>();
        try {
            responses.add(1, configuration.getTemplate("GetRequestResponseFTPAttach.ftl"));
            responses.add(1, configuration.getTemplate("GetRequestResponseWithoutAttach.ftl"));
            responses.add(1, configuration.getTemplate("GetRequestResponseWithEmbeddedAttach.ftl"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ftpServer.writeToFtp("target/lib/Enterprise OSGi in Action.zip", "ftp://" + appProperties.getUser() + ":" +
                    appProperties.getPassword() + "@" + appProperties.getHost() + ":" + appProperties.getPort() + "ftpFiles/Enterprise OSGi in Action.zip");
            ftpServer.writeToFtp("target/lib/the-devops.zip", "ftp://" + appProperties.getUser() + ":" +
                    appProperties.getPassword() + "@" + appProperties.getHost() + ":" + appProperties.getPort() + "/ftpFiles/the-devops.zip");

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ftpServer.writeToFtp(System.getProperty("user.dir") + "/ftpFiles/Enterprise OSGi in Action.zip", "ftp://" + appProperties.getUser() + ":" +
                    appProperties.getPassword() + "@" + appProperties.getHost() + ":" + appProperties.getPort() + "/Enterprise OSGi in Action.zip");
            ftpServer.writeToFtp(System.getProperty("user.dir") + "/ftpFiles/the-devops.zip", "ftp://" + appProperties.getUser() + ":" +
                    appProperties.getPassword() + "@" + appProperties.getHost() + ":" + appProperties.getPort() + "/the-devops.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLoadProfile(Double withoutAttachmentWeight, Double withEmbeddedWeigth, Double withFptWeight) throws IOException {
        loadProfile.withoutAttachmentWeight = withoutAttachmentWeight;
        loadProfile.withEmbeddedWeigth = withEmbeddedWeigth;
        loadProfile.withFptWeight = withFptWeight;
        responses = new RandomCollection<>();
        responses.add(withoutAttachmentWeight, configuration.getTemplate("GetRequestResponseWithoutAttach.ftl"));
        responses.add(withEmbeddedWeigth, configuration.getTemplate("GetRequestResponseWithEmbeddedAttach.ftl"));
        responses.add(withFptWeight, configuration.getTemplate("GetRequestResponseFTPAttach.ftl"));
    }

    public Map<String, Double> getLoadProfile() throws IOException {
        Map<String, Double> loadProfileData = new HashMap<>();
        loadProfileData.put("withoutAttachmentWeight", loadProfile.withoutAttachmentWeight);
        loadProfileData.put("withEmbeddedWeigth", loadProfile.withEmbeddedWeigth);
        loadProfileData.put("withFptWeight", loadProfile.withFptWeight);
        Collections.unmodifiableMap(loadProfileData);
        return loadProfileData;
    }

    public String getRequestResponse() throws IOException, TemplateException {

        StringWriter sr = new StringWriter();
        Template responseTemplate = responses.next();
        responseTemplate.process(modelRandomizer.getRandomizedModel(), sr);
        return sr.toString();
    }

    @Component
    static class LoadProfile {
        private Double withoutAttachmentWeight = 1.0;
        private Double withEmbeddedWeigth = 1.0;
        private Double withFptWeight = 1.0;
    }
}
