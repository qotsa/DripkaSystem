package ru.otr.integration.smev3client.smev3mock2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by tartanov.mikhail on 27.07.2016.
 */

@Component
@ConfigurationProperties(prefix = "ftp")
public class AppProperties {

    private String user;
    private String password;
    private int port;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }








}
