package ru.otr.integration.smev3client;


import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;

import org.apache.camel.test.spring.CamelSpringBootRunner;

import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.camel.builder.Builder.simple;

@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@UseAdviceWith
public class Smev3coreApplicationTests extends XMLTestCase {

    @Autowired
    private ModelCamelContext context;

    @Value("classpath:RequestBodyAttach.xml")
    private Resource requestBodyAttach;

    @Value("classpath:RequestBodyNoAttach.xml")
    private Resource requestBodyNoAttach;

    @Value("classpath:RequestBodyNoAttachStub.xml")
    private Resource requestBodyNoAttachStub;

    @EndpointInject(uri = "{{smevToVisPreprocessor.queue.out.default}}")
    protected MockEndpoint preOutDefEndpoint;

    @EndpointInject(uri = "{{smevToVisPreprocessor.queue.out.stub}}")
    protected MockEndpoint preOutStubEndpoint;

    @EndpointInject(uri = "{{smevToVisPreprocessor.queue.out.replication}}")
    protected MockEndpoint preOutRepEndpoint;

    @EndpointInject(uri = "{{smevToVisPostprocessor.queue.out}}")
    protected MockEndpoint out2Endpoint;

    @EndpointInject(uri = "{{smevToVisPreprocessor.queue.in}}")
    protected ProducerTemplate input1Endpoint;

    @EndpointInject(uri = "{{smevToVisPostprocessor.queue.in}}")
    protected ProducerTemplate input2Endpoint;

    @Test
    public void testRoutes() throws Exception {

//        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
//                    @Override
//                    public void configure() throws Exception {
//                        weaveAddLast().to("mock:{{smevToVisPreprocessor.queue.out}}");
//                    }
//                }
//        );

//        context.getRouteDefinitions().get(1).adviceWith(context, new AdviceWithRouteBuilder() {
//                    @Override
//                    public void configure() throws Exception {
//                        weaveAddLast().to("mock:{{smevToVisPostprocessor.queue.out}}");
//                    }
//                }
//        );

        context.start();

        input1Endpoint.sendBody(TestUtils.getResourceAsString(requestBodyAttach));
        preOutRepEndpoint.expectedBodiesReceived(TestUtils.getResourceAsString(requestBodyAttach));
        preOutRepEndpoint.expectedMessageCount(1);
        preOutRepEndpoint.assertIsSatisfied();

        input1Endpoint.sendBody(TestUtils.getResourceAsString(requestBodyNoAttachStub));
        preOutStubEndpoint.expectedBodiesReceived(TestUtils.getResourceAsString(requestBodyNoAttachStub));
        preOutStubEndpoint.expectedMessageCount(1);
        preOutStubEndpoint.assertIsSatisfied();

        input1Endpoint.sendBody(TestUtils.getResourceAsString(requestBodyNoAttach));
        preOutDefEndpoint.expectedBodiesReceived(TestUtils.getResourceAsString(requestBodyNoAttach));
        preOutDefEndpoint.expectedMessageCount(1);
        preOutDefEndpoint.assertIsSatisfied();


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

