package com.test.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class TestEnvironment {

    @Value("${baseURI}")
    private String baseURI;

    @Value("${port}")
    private int port;

    @Value("${basePath}")
    private String basePath;

    @Value("${categoryPath}")
    private String categoryPath;

    @Value("${categoriesPath}")
    private String categoriesPath;

    @Value("${categoriesSearchPath}")
    private String categoriesSearchPath;

    @Value("${userPath}")
    private String userPath;

    @Value("${usersListPath}")
    private String usersListPath;

    @Value("${userLoginPath}")
    private String userLoginPath;

    @Value("${userSearchPath}")
    private String userSearchPath;

    @Value("${userSurnamePath}")
    private String userSurnamePath;

    @Value("${paymentCardPath}")
    private String paymentCardPath;

    @Value("${paymentCardListPath}")
    private String paymentCardListPath;

    @Value("${paymentCardSearchPath}")
    private String paymentCardSearchPath;

    @Value("${addressPath}")
    private String addressPath;

    @Value("${addressListPath}")
    private String addressListPath;

    @Value("${addressSearchPath}")
    private String addressSearchPath;

    @Value("${imageitemPath}")
    private String imageitemPath;

    @Value("${imageitemLinkPath}")
    private String imageitemLinkPath;

    @Value("${imageitemUnlinkPath}")
    private String imageitemUnlinkPath;

    @Value("${imageitemListPath}")
    private String imageitemListPath;

    @Value("${imageitemSearchPath}")
    private String imageitemSearchPath;

    @Value("${imageitemSearchAdvancedPath}")
    private String imageitemSearchAdvancedPath;
}
