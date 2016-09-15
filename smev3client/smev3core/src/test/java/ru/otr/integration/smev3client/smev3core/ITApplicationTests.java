package ru.otr.integration.smev3client.smev3core;


import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.io.InputStream;

@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@UseAdviceWith
public class ITApplicationTests {

    @Autowired
    private ModelCamelContext context;

    //сообщение с аттачем -- уходит на сервис репликации
    @Value("classpath:RequestBodyAttach.xml")
    private Resource requestBodyAttach;

    //сообщение без аттача с получателем для которого не определен ендпоинт
    @Value("classpath:RequestBodyNoAttach.xml")
    private Resource requestBodyNoAttach;

    //сообщение без аттача с получателем STUB
    @Value("classpath:RequestBodyNoAttachStub.xml")
    private Resource requestBodyNoAttachStub;

    /*@EndpointInject(uri = "{{routes.preprocessor.inbound}}")
    protected ProducerTemplate input1Endpoint;

    @EndpointInject(uri = "{{routes.postprocessor.inbound}}")
    protected ProducerTemplate input2Endpoint;

    @EndpointInject(uri = "{{routes.preprocessor.outbound.default}}")
    protected MockEndpoint preOutDefEndpoint;

    @EndpointInject(uri = "{{routes.preprocessor.outbound.replication}}")
    protected MockEndpoint preOutRepEndpoint;

    @EndpointInject(uri = "{{routes.log}}")
    protected MockEndpoint postOutDefEndpoint;

    @EndpointInject(uri = "{{routes.metadata.STUB}}")
    protected MockEndpoint outStubEndpoint;*/

    @Test
    public void testRoutes() throws Exception {

        /*context.start();

//        есть вложения
        preOutRepEndpoint.expectedBodiesReceived(TestUtils.getResourceAsString(requestBodyAttach));
        preOutRepEndpoint.expectedMessageCount(1);

//        нет вложений и не определен маршрут в маппинге
        preOutDefEndpoint.expectedBodiesReceived(TestUtils.getResourceAsString(requestBodyNoAttach));
        preOutDefEndpoint.expectedMessageCount(1);

//        нет вложений, определен маршрут
        postOutDefEndpoint.expectedBodiesReceived(TestUtils.getResourceAsString(requestBodyNoAttachStub));
        postOutDefEndpoint.expectedMessageCount(1);

//        выходная очередь для постпроцессора и препроцессора из маппинга
        outStubEndpoint.expectedBodiesReceived(
                TestUtils.getResourceAsString(requestBodyNoAttachStub),
                TestUtils.getResourceAsString(requestBodyNoAttachStub));
        outStubEndpoint.expectedMessageCount(2);

        //preprocessor tests
        input1Endpoint.sendBody(TestUtils.getResourceAsString(requestBodyAttach));
        input1Endpoint.sendBody(TestUtils.getResourceAsString(requestBodyNoAttachStub));
        input1Endpoint.sendBody(TestUtils.getResourceAsString(requestBodyNoAttach));


        //postprocessor tests
        input2Endpoint.sendBodyAndHeader(
                TestUtils.getResourceAsString(requestBodyNoAttachStub),
                "messageReplicationAndVerification",
                "OK");

        input2Endpoint.sendBodyAndHeader(TestUtils.getResourceAsString(requestBodyNoAttachStub),
                "messageReplicationAndVerification", "FAILED");


        preOutRepEndpoint.assertIsSatisfied();
        preOutDefEndpoint.assertIsSatisfied();
        postOutDefEndpoint.assertIsSatisfied();
        outStubEndpoint.assertIsSatisfied();*/
    }

    private static class TestUtils {
        static String getResourceAsString(Resource resource) throws IOException {
            try (InputStream is = resource.getInputStream()) {
                return IOUtils.toString(is, "utf-8");
            }
        }

        private TestUtils() {
        }
    }
}

