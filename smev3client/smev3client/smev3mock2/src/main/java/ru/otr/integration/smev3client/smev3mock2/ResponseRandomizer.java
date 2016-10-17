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


    @Value("classpath:Enterprise OSGi in Action.zip")
    private Resource entOsgiinAct;

    @Value("classpath:the-devops.zip")
    private Resource theDevops;


    private RandomCollection<Template> responses;

    @PostConstruct
    public void postConstructSetup() {
        logger.debug("Entering postConstructSetup()");
        //getRequestResponseWithoutAttachString = ResponseRandomizer.TestUtils.getResourceAsString(getRequestResponseWithoutAttach);
        //getRequestResponseFTPAttachString = ResponseRandomizer.TestUtils.getResourceAsString(getRequestResponseFTPAttach);
        responses = new RandomCollection<>();
        try {
            responses.add(1, configuration.getTemplate("GetRequestResponseFTPAttach.ftl"));
            responses.add(1, configuration.getTemplate("GetRequestResponseWithoutAttach.ftl"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        String curDir = new File(".").getAbsolutePath();




        //little portion of hardcode )


        //File temp1 = File.createTempFile("temp1",".tmp");
        //File temp2 = File.createTempFile("temp2",".tmp");

        //Files.copy(TestUtils.getResourceAsStream(entOsgiinAct), temp1.toPath());
        //Files.copy(TestUtils.getResourceAsStream(theDevops), temp2.toPath());


        //logger.debug("entOsgiinAct.getURI().toString(): {} ", entOsgiinAct.getURI().toString());
        //logger.debug("theDevops.getURI().toString(): {} ", theDevops.getURI().toString());


        /*ftpServer.writeToFtp(temp1.getAbsolutePath(), "ftp://" + appProperties.getUser() + ":" + appProperties.getPassword() +
                "@localhost:" + appProperties.getPort() + "/Enterprise OSGi in Action.zip");*/



        /*try(InputStream is1 = new FileInputStream("target/lib/Enterprise OSGi in Action.zip"); InputStream is2 = new FileInputStream("target/lib/the-devops.zip")) {

                ftpServer.writeToFtp(is1, "Enterprise OSGi in Action.zip");

                ftpServer.writeToFtp(is2, "the-devops.zip");


                //ftpServer.writeToFtp(temp2.getAbsolutePath(), "ftp://user_STUB2:qwerty123@localhost:3333/the-devops.zip");
            }
        catch (IOException e) {
            e.printStackTrace();
        }*/


        try {

            //ftpServer.writeToFtp(is1, "Enterprise OSGi in Action.zip");
            ftpServer.writeToFtp("target/lib/Enterprise OSGi in Action.zip", "ftp://" + appProperties.getUser()+":"+
            appProperties.getPassword() + "@" + appProperties.getHost() + ":" + appProperties.getPort()+ "ftpFiles/Enterprise OSGi in Action.zip");

            //ftpServer.writeToFtp(is2, "the-devops.zip");
            ftpServer.writeToFtp("target/lib/the-devops.zip", "ftp://" + appProperties.getUser()+":"+
                    appProperties.getPassword() + "@" + appProperties.getHost() + ":" + appProperties.getPort()+ "/ftpFiles/the-devops.zip");

            //ftpServer.writeToFtp(temp2.getAbsolutePath(), "ftp://user_STUB2:qwerty123@localhost:3333/the-devops.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            //ftpServer.writeToFtp(is1, "Enterprise OSGi in Action.zip");
            ftpServer.writeToFtp(System.getProperty("user.dir") + "/ftpFiles/Enterprise OSGi in Action.zip", "ftp://" + appProperties.getUser()+":"+
                    appProperties.getPassword() + "@" + appProperties.getHost() + ":" + appProperties.getPort()+ "/Enterprise OSGi in Action.zip");

            //ftpServer.writeToFtp(is2, "the-devops.zip");
            ftpServer.writeToFtp(System.getProperty("user.dir") + "/ftpFiles/the-devops.zip","ftp://" + appProperties.getUser()+":"+
                    appProperties.getPassword() + "@" + appProperties.getHost() + ":" + appProperties.getPort()+ "/the-devops.zip");

            //ftpServer.writeToFtp(temp2.getAbsolutePath(), "ftp://user_STUB2:qwerty123@localhost:3333/the-devops.zip");
        }
        catch (Exception e) {
            e.printStackTrace();
        }



        /*try(InputStream is1 = new FileInputStream("lib/Enterprise OSGi in Action.zip"); InputStream is2 = new FileInputStream("lib/the-devops.zip")) {

            //ftpServer.writeToFtp(is1, "Enterprise OSGi in Action.zip");
            ftpServer.writeToFtp(is1, "Enterprise OSGi in Action.zip");

            //ftpServer.writeToFtp(is2, "the-devops.zip");
            ftpServer.writeToFtp(is2, "the-devops.zip");

            //ftpServer.writeToFtp(temp2.getAbsolutePath(), "ftp://user_STUB2:qwerty123@localhost:3333/the-devops.zip");
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/






        /*try(InputStream is1 = new FileInputStream("C:\\test\\Enterprise OSGi in Action.zip"); InputStream is2 = new FileInputStream("C:\\test\\the-devops.zip")) {

            ftpServer.writeToFtp(is1, "Enterprise OSGi in Action.zip");

            ftpServer.writeToFtp(is2, "the-devops.zip");

            //ftpServer.writeToFtp(temp2.getAbsolutePath(), "ftp://user_STUB2:qwerty123@localhost:3333/the-devops.zip");
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/






        /*try (InputStream is1 = this.getClass().getClassLoader().getResourceAsStream("Enterprise OSGi in Action.zip"); InputStream is2 = this.getClass().getClassLoader().getResourceAsStream("the-devops.zip")) {

            ftpServer.writeToFtp(is1, "Enterprise OSGi in Action.zip");

            ftpServer.writeToFtp(is2, "the-devops.zip");

            //ftpServer.writeToFtp(temp2.getAbsolutePath(), "ftp://user_STUB2:qwerty123@localhost:3333/the-devops.zip");


        }*/


        /*Resource res1 = applicationContext.getResource("classpath:Enterprise OSGi in Action.zip");
        Resource res2 = applicationContext.getResource("classpath:the-devops.zip");

        try(InputStream is1 = res1.getInputStream(); InputStream is2 = res2.getInputStream()) {

            ftpServer.writeToFtp(is1, "Enterprise OSGi in Action.zip");

            ftpServer.writeToFtp(is2, "the-devops.zip");

            //ftpServer.writeToFtp(temp2.getAbsolutePath(), "ftp://user_STUB2:qwerty123@localhost:3333/the-devops.zip");
*/




    }

    public void setLoadProfile(Double withoutAttachmentWeight, Double withEmbeddedWeigth, Double withFptWeight) throws IOException {
        loadProfile.withoutAttachmentWeight = withoutAttachmentWeight;
        loadProfile.withEmbeddedWeigth = withEmbeddedWeigth;
        loadProfile.withFptWeight = withFptWeight;
        responses = new RandomCollection<>();
        responses.add(withoutAttachmentWeight,configuration.getTemplate("GetRequestResponseWithoutAttach.ftl") );
        responses.add(withFptWeight,configuration.getTemplate("GetRequestResponseFTPAttach.ftl") );
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


   /* public static class TestUtils {
        public static InputStream getResourceAsStream(Resource resource) throws IOException {
            try(InputStream is = resource.getInputStream()) {
                return is;
            }
        }
    }*/


    @Component
     static class LoadProfile {
        private Double withoutAttachmentWeight = 1.0;
        private Double withEmbeddedWeigth = 1.0;
        private Double withFptWeight = -1.0;
    }

}
