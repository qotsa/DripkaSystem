package ru.otr.integration.smev3client.bpmengine.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Created by nikitin.maksim on 04.10.2016.
 */
@Configuration
public class ActivitiConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SpringProcessEngineConfiguration processEngineConfiguration;

    @PostConstruct
    public void init() {
        /*FormEngineConfiguration formEngineConfiguration = new FormEngineConfiguration();
        formEngineConfiguration.setDataSource(dataSource);
        FormEngineConfigurator formEngineConfigurator = new FormEngineConfigurator();
        formEngineConfigurator.setFormEngineConfiguration(formEngineConfiguration);
        processEngineConfiguration.addConfigurator(formEngineConfigurator);*/

        /*DmnEngineConfiguration dmnEngineConfiguration = new DmnEngineConfiguration();
        dmnEngineConfiguration.setDataSource(dataSource);
        DmnEngineConfigurator dmnEngineConfigurator = new DmnEngineConfigurator();
        dmnEngineConfigurator.setDmnEngineConfiguration(dmnEngineConfiguration);
        processEngineConfiguration.addConfigurator(dmnEngineConfigurator);*/
    }
}
