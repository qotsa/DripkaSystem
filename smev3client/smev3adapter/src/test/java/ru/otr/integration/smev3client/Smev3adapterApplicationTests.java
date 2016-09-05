package ru.otr.integration.smev3client;


import org.apache.camel.test.spring.*;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLTestCase;
import  org.custommonkey.xmlunit.XMLUnit;


import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.camel.builder.Builder.simple;
import static org.junit.Assert.assertEquals;

@RunWith(CamelSpringBootRunner.class)
@MockEndpointsAndSkip("cxf:*")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@SpringBootApplication
@SpringBootTest(/*classes = Smev3adapterApplicationTests.class,*/ webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@UseAdviceWith


public class Smev3adapterApplicationTests  extends XMLTestCase {

	@Autowired
	private ModelCamelContext context;

	@Autowired
	private ProducerTemplate template;

	@Value("classpath:RequestBody1.xml")
	private Resource requestBody;

	@Value("classpath:MockResponse1.xml")
	private Resource mockResponse;

	@Value("classpath:ExpectedBody1.xml")
	private Resource expectedBody;

	@LocalServerPort
	private int port;

	@Test
	public void testRoute() throws Exception {

		//set adivces and start context
		context.getRouteDefinitions().get(0).adviceWith(context, new AdviceWithRouteBuilder() {
					@Override
					public void configure() throws Exception {
						weaveAddLast().to("mock:result");
						//weaveById("activemqnode").remove();
					}
				}
		);
		context.start();

		//set env
		String requestBodyString = TestUtils.getResourceAsString(requestBody);
		String mockResponseString = TestUtils.getResourceAsString(mockResponse);
		String expectedBodyString = TestUtils.getResourceAsString(expectedBody);

		XMLUnit.setIgnoreWhitespace(true);

		MockEndpoint mock = context.getEndpoint("mock://cxf:bean:smevMessageExchangeService", MockEndpoint.class);
		mock.returnReplyBody(simple(mockResponseString));

		//send request
		String result = template.requestBody("netty4-http:http://localhost:" + port + "/camel/request", requestBodyString, String.class);

		//assert
		//assertXMLEqual("Comparing expected and fact response: ", expectedBodyString, result);
	}

	private static class TestUtils {
		static String getResourceAsString(Resource resource) throws IOException {
			try(InputStream is = resource.getInputStream())	{
				return IOUtils.toString(is, "utf-8");
			}
		}
		private TestUtils() {}
	}
}

