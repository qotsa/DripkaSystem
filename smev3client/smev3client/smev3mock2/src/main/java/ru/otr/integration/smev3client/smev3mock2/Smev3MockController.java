package ru.otr.integration.smev3client.smev3mock2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * Created by tartanov.mikhail on 06.10.2016.
 */

@RestController
@RequestMapping("mock-web")
public class Smev3MockController {
    @Autowired
    Configuration configuration;

    @Autowired
    ResponseGenerator responseGenerator;

    @RequestMapping("/setloadprofile")
    public void setLoadProfile(@RequestParam(value = "withoutattach", required=false,  defaultValue = "1") double withoutAttachmentWeight,
                               @RequestParam(value = "withembedded", required=false, defaultValue = "1" ) double withEmbeddedWeight,
                               @RequestParam(value = "withattach", required=false, defaultValue = "1") double withFptWeight, HttpServletResponse response) throws TemplateException, IOException {
        responseGenerator.setLoadProfile(withoutAttachmentWeight, withEmbeddedWeight, withFptWeight);
        response.setContentType("text/html; charset=UTF-8");
        getLoadProfile(response);
    }

    @RequestMapping("/getloadprofile")
    public void getLoadProfile(HttpServletResponse response) throws TemplateException {
        response.setContentType("text/html; charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            Template template = configuration.getTemplate("getloadprofile.ftl");
            template.process(responseGenerator.getLoadProfile(),writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getrequestresponse")
    public void getRequestResponse(HttpServletResponse response) throws TemplateException {
        response.setContentType("text/plain; charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(responseGenerator.getRequestResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/ackrequest")
    public void ackRequest(HttpServletResponse response) throws TemplateException {
        response.setContentType("text/plain; charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(responseGenerator.ackRequest());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
