package com.griddynamics.qa.vikta.uitesting.sample.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "test-data")
public class TestDataConfiguration {

    String adminName;
    String adminPassword;
    String adminMail;
    String adminSurname;
    String adminFirstName;
    String adminMiddleName;

    String userName;
    String userPassword;
    String userMail;
    String userSurname;
    String userFirstName;
    String userMiddleName;

    String nickname;

    String cardCode;
    String cardTag;

    String firstCatTitle;
    String firstCatDesc;
    String secondCatTitle;
    String secondCatDesc;
}
