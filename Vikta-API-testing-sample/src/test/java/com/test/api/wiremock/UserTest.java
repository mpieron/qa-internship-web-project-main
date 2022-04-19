package com.test.api.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.test.api.category.CategoryTest;
import com.test.api.execution.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;


public class UserTest extends BaseTest {

    private final List<Integer> idUserToClean = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(CategoryTest.class);
    private static final String idQueryParam = "id";
    private static final int port = 5054;
    private static final WireMockServer vikta = new WireMockServer(WireMockConfiguration.wireMockConfig().port(port));

    @BeforeClass
    public static void startVikta(){
        vikta.start();

        stubFor(get(urlMatching("http://localhost:5054/api/v1/user?id=-20"))
                .willReturn(aResponse().withStatus(404)));
    }

    @AfterClass
    public static void shutdownVikta(){
        vikta.shutdown();
    }

//    @ParameterizedTest
//    @ValueSource(longs = {Long.MIN_VALUE, -20, (long) 0, Long.MAX_VALUE})
    @Test
    public void canNotGetUserByWrongId(){

        given()
                .spec(defaultRequestSpec())
                .queryParam(idQueryParam, -20)
                .when()
                .get(getTestEnvironment().getUserPath())
                .then()
                .assertThat().statusCode(404);
    }

}
