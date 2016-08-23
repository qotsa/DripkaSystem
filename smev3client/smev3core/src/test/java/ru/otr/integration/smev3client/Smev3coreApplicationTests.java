package ru.otr.integration.smev3client;


import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
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

@RunWith(CamelSpringBootRunner.class)
@MockEndpointsAndSkip("cxf:*")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@UseAdviceWith
public class Smev3coreApplicationTests extends XMLTestCase {

	@Autowired
	private ModelCamelContext context;

	@Test
	public void testRoute() throws Exception {

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

