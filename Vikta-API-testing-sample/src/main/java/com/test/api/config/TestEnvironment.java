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
}
