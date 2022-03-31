package com.griddynamics.qa.vikta.uitesting.sample.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "setup")
public class TestSetupConfiguration {
    String browser;

    int pageLoadTimeout;

    int waitTimeout;
    String baseHost;
    String basePort;
    String baseUrl;

    String loginPageUrl;
    String registrationPageUrl;
}
