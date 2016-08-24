package ru.otr.integration.smev3client;


import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
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

    @Value("classpath:RequestBody1.xml")
    private Resource requestBody;

    @EndpointInject(uri = "mock:activemq:queue:output1")
    protected MockEndpoint out1Endpoint;

    @EndpointInject(uri = "mock:activemq:queue:output2")
    protected MockEndpoint out2Endpoint;

    @EndpointInject(uri = "activemq:queue:input1")
    protected ProducerTemplate input1Endpoint;

    @EndpointInject(uri = "activemq:queue:input2")
    protected ProducerTemplate input2Endpoint;

    @Test
    public void testRoutes() throws Exception {

        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        weaveAddLast().to("mock:activemq:queue:output1");
                    }
                }
        );

        context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        weaveAddLast().to("mock:activemq:queue:output2");
                    }
                }
        );

        context.start();

        input1Endpoint.sendBody(TestUtils.getResourceAsString(requestBody));
        input2Endpoint.sendBody(TestUtils.getResourceAsString(requestBody));

        out1Endpoint.expectedBodiesReceived(TestUtils.getResourceAsString(requestBody));
        out1Endpoint.expectedMessageCount(1);

        out2Endpoint.expectedBodiesReceived(TestUtils.getResourceAsString(requestBody));
        out2Endpoint.expectedMessageCount(1);

        out1Endpoint.assertIsSatisfied();
        out2Endpoint.assertIsSatisfied();


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

