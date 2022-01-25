package com.test.api.execution;

import com.test.api.config.TestConfig;
import com.test.api.config.TestEnvironment;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
public abstract class BaseTest {
    @Getter
    @Autowired
    private TestEnvironment testEnvironment;

    protected RequestSpecification defaultRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(testEnvironment.getBaseURI())
                .setPort(testEnvironment.getPort())
                .setBasePath(testEnvironment.getBasePath())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON).build();
    }

    protected ResponseSpecification defaultResponseJsonSpec() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON).build();
    }

    protected ResponseSpecification defaultResponseTxtSpec() {
        return new ResponseSpecBuilder()
                .expectContentType("text/html;charset=UTF-8").build();
    }
}
