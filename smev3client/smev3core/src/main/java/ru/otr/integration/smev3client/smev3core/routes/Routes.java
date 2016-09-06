package ru.otr.integration.smev3client.smev3core.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.springframework.stereotype.Component;

@Component
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        Namespaces ns = new Namespaces("bas", "http://otr.ru/irs/services/message-exchange/types/basic")
                .add("typ2", "http://otr.ru/irs/services/message-exchange/types");

        from("{{routes.Smev2Vis.preprocessor.GetRequestResponseQueue}}").to("direct:Smev2Vis.preprocessor");
        from("{{routes.Smev2Vis.preprocessor.GetResponseResponseQueue}}").to("direct:Smev2Vis.preprocessor");
        from("direct:Smev2Vis.preprocessor").to("{{routes.Smev2Vis.preprocessor.inbound}}");

        from("{{routes.Smev2Vis.preprocessor.inbound}}")
                .transacted()
                .routeId("Smev2VisPreprocessor")
                .choice()
                    .when(ns.xpath("//bas:FSAttachmentsList/bas:FSAttachment"))
                        .to("{{routes.Smev2Vis.replication}}").stop()
                    .otherwise()
                        .to("direct:Smev2Vis.postprocessor")
                .end();

        from("direct:Smev2Vis.postprocessor").to("{{routes.Smev2Vis.postprocessor.inbound}}");

        from("{{routes.Smev2Vis.postprocessor.inbound}}")
                .transacted()
                .routeId("Smev2VisPostprocessor")
                .setHeader("recipient").xpath("//typ2:MessageMetadata/typ2:Recipient/typ2:Mnemonic/text()", ns)
                .choice()
                    .when(header("messageReplicationAndVerification").isNotEqualTo("OK"))
                        .to("{{routes.log}}").stop()
                    .otherwise()
                        .dynamicRouter(method(PostprocessorRouter.class, "route"))
                .end();

        from("{{routes.Vis2Smev.preprocessor.SendRequestResponseQueue}}").to("direct:Vis2Smev.preprocessor");
        from("{{routes.Vis2Smev.preprocessor.SendResponseResponseQueue}}").to("direct:Vis2Smev.preprocessor");
        from("direct:Vis2Smev.preprocessor").to("{{routes.Vis2Smev.preprocessor.inbound}}");

        from("{{routes.Vis2Smev.preprocessor.inbound}}")
                .transacted()
                .routeId("Vis2SmevPreprocessor")
                .choice()
                .when(ns.xpath("//bas:FSAttachmentsList/bas:FSAttachment"))
                .to("{{routes.Vis2Smev.replication}}").stop()
                .otherwise()
                .to("direct:Vis2Smev.postprocessor")
                .end();

        from("direct:Vis2Smev.postprocessor").to("{{routes.Vis2Smev.postprocessor.inbound}}");

        // TODO route to pushers module

        from("{{routes.Vis2Smev.postprocessor.inbound}}")
                .transacted()
                .routeId("Vis2SmevPostprocessor")
                .to("{{routes.log}}");
    }
}
