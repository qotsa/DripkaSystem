package ru.otr.integration.smev3client.smev3core;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
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
    private ModelCamelContext camelContext;

    //сообщение с аттачем -- уходит на сервис репликации
    @Value("classpath:RequestBodyAttach.xml")
    private Resource requestBodyAttach;

    //сообщение без аттача с получателем для которого не определен ендпоинт
    @Value("classpath:RequestBodyNoAttach.xml")
    private Resource requestBodyNoAttach;

    //сообщение без аттача с получателем STUB
    @Value("classpath:RequestBodyNoAttachStub.xml")
    private Resource requestBodyNoAttachStub;

    @EndpointInject(uri = "{{routes.Smev2Vis.preprocessor.GetRequestResponseQueue}}")
    protected ProducerTemplate smev2VisPreprocessorGetRequestResponseQueue;

    @EndpointInject(uri = "{{routes.Smev2Vis.preprocessor.GetResponseResponseQueue}}")
    protected ProducerTemplate smev2VisPreprocessorGetResponseResponseQueue;

    @EndpointInject(uri = "{{routes.Vis2Smev.inbound}}")
    protected ProducerTemplate vis2SmevInbound;

    @EndpointInject(uri = "{{routes.replication}}")
    protected MockEndpoint replicationMock;

    @EndpointInject(uri = "{{routes.replication}}")
    protected ProducerTemplate replication;

    @EndpointInject(uri = "{{routes.log}}")
    protected MockEndpoint logMock;

    @EndpointInject(uri = "{{routes.metadata.STUB}}")
    protected MockEndpoint metadataSTUBMock;

    @EndpointInject(uri = "{{routes.metadata.UFOS}}")
    protected MockEndpoint metadataUFOSMock;

    //@EndpointInject(uri = "{{routes.Smev2Vis.GetStatusResponseQueue}}")
    //protected MockEndpoint smev2VisGetStatusResponseQueueMock;

    @Test
    public void testRoutes() throws Exception {
        camelContext.start();

//        есть вложения
        /*preOutRepEndpoint.expectedBodiesReceived(getResourceAsString(requestBodyAttach));
        preOutRepEndpoint.expectedMessageCount(1);

//        нет вложений и не определен маршрут в маппинге
        preOutDefEndpoint.expectedBodiesReceived(getResourceAsString(requestBodyNoAttach));
        preOutDefEndpoint.expectedMessageCount(1);

//        нет вложений, определен маршрут
        postOutDefEndpoint.expectedBodiesReceived(getResourceAsString(requestBodyNoAttachStub));
        postOutDefEndpoint.expectedMessageCount(1);

//        выходная очередь для постпроцессора и препроцессора из маппинга
        outStubEndpoint.expectedBodiesReceived(
                TestUtils.getResourceAsString(requestBodyNoAttachStub),
                TestUtils.getResourceAsString(requestBodyNoAttachStub));
        outStubEndpoint.expectedMessageCount(2);

        //preprocessor tests
        input1Endpoint.sendBody(getResourceAsString(requestBodyAttach));
        input1Endpoint.sendBody(getResourceAsString(requestBodyNoAttachStub));
        input1Endpoint.sendBody(getResourceAsString(requestBodyNoAttach));


        //postprocessor tests
        input2Endpoint.sendBodyAndHeader(
                TestUtils.getResourceAsString(requestBodyNoAttachStub),
                "messageReplicationAndVerification",
                "OK");

        input2Endpoint.sendBodyAndHeader(getResourceAsString(requestBodyNoAttachStub),
                "messageReplicationAndVerification", "FAILED");


        preOutRepEndpoint.assertIsSatisfied();
        preOutDefEndpoint.assertIsSatisfied();
        postOutDefEndpoint.assertIsSatisfied();
        outStubEndpoint.assertIsSatisfied();*/
    }

    private static String getResourceAsString(Resource resource) throws IOException {
        try (InputStream is = resource.getInputStream()) {
            return IOUtils.toString(is, "utf-8");
        }
    }
}
