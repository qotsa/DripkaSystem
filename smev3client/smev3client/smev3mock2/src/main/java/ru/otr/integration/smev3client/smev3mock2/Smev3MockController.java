package ru.otr.integration.smev3client.smev3mock2;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.io.IOUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Configuration;



/**
 * Created by tartanov.mikhail on 06.10.2016.
 */

@RestController
@RequestMapping("mock-web")
public class Smev3MockController {
    @Autowired
    Configuration configuration;

    @Autowired
    ResponseRandomizer responseRandomizer;

    @RequestMapping("/setloadprofile")
    public void setLoadProfile(@RequestParam(value = "withoutattach", required=false,  defaultValue = "1") double withoutAttachmentWeight,
                               @RequestParam(value = "withembedded", required=false, defaultValue = "1" ) double withEmbeddedWeight,
                               @RequestParam(value = "withattach", required=false, defaultValue = "1") double withFptWeight, HttpServletResponse response) throws TemplateException, IOException {
        responseRandomizer.setLoadProfile(withoutAttachmentWeight, withEmbeddedWeight, withFptWeight);
        response.setContentType("text/html; charset=UTF-8");
        getLoadProfile(response);
    }

    @RequestMapping("/getloadprofile")
    public void getLoadProfile(HttpServletResponse response) throws TemplateException {
        response.setContentType("text/html; charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            Template template = configuration.getTemplate("getloadprofile.ftl");
            template.process(responseRandomizer.getLoadProfile(),writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getrequestresponse")
    public void getRequestResponse(HttpServletResponse response) throws TemplateException {
        response.setContentType("text/plain; charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(responseRandomizer.getRequestResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
