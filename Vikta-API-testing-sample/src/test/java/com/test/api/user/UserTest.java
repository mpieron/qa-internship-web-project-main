package com.test.api.user;

import com.test.api.dto.UserDTO;
import com.test.api.execution.BaseTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;


import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class UserTest extends BaseTest {

    @Test
    public void canGetUserByCorrectId(){
        List<UserDTO> users = getListOfUsers();
        final int id = users.get(new Random().nextInt(users.size())).getId();
        final String idQueryParam = "id";

        Response response = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .log().all()
                .get(getTestEnvironment().getUserPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        UserDTO user = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .log().all()
                .get(getTestEnvironment().getUserPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .as(UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(user.getId())
                .as(String.format("Should be user with id %s, but was with %s", id, user.getId()))
                .isEqualTo(id);
    }

    @Test
    public void canListAllUsers(){
        List<UserDTO> users = getListOfUsers();

        assertThat(users).as("User list shouldn't be empty").isNotEmpty();
        assertThat(users).as("User don't have loginName field").allMatch(user -> !user.getLoginName().isEmpty());
        assertThat(users).as("User don't have password field").allMatch(user -> !user.getPassword().isEmpty());
        assertThat(users).as("User don't have email field").allMatch(user -> !user.getEmail().isEmpty());
        assertThat(users).as("User don't have firstName field").allMatch(user -> !user.getFirstName().isEmpty());
        assertThat(users).as("User don't have middleName field").allMatch(user -> !user.getMiddleName().isEmpty());
        assertThat(users).as("User don't have surname field").allMatch(user -> !user.getSurname().isEmpty());
    }

    @Test
    public void canGetUserByLoginName(){
        final String loginPath = getTestEnvironment().getUserPath() + "/login";
        final String loginQueryParam = "login";

        List<UserDTO> users = getListOfUsers();
        String userLogin = users.get(new Random().nextInt(users.size())).getLoginName();

        List<UserDTO> user = given().spec(defaultRequestSpec())
                .queryParam(loginQueryParam, userLogin)
                .when()
                .log().all()
                .get(loginPath)
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", UserDTO.class);

        assertThat(user.size())
                .as("There should be no users with same login")
                .isLessThan(2);

        assertThat(user.get(0).getLoginName())
                .as(String.format("Should be user with login %s, but was with %s", userLogin, user.get(0).getLoginName()))
                .isEqualTo(userLogin);
    }

    // when user is not found (does not exist) returns empty body, I think that better will be returned code 404 - not found
    @Test
    public void canNotGetUserByWrongLoginName(){
        final String loginPath = getTestEnvironment().getUserPath() + "/login";
        final String loginQueryParam = "login";
        final String userLogin = "ThereIsNotUserWithThatLogin";

        List<UserDTO> user = given().spec(defaultRequestSpec())
                .queryParam(loginQueryParam, userLogin)
                .when()
                .log().all()
                .get(loginPath)
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", UserDTO.class);

        assertThat(user).isEmpty();
    }

    @Test
    public void canSearchUserByTerm(){
        final String searchPath = getTestEnvironment().getUserPath() + "/search";
        final String termQueryParam = "term";
        Random random = new Random();
        List<UserDTO> users = getListOfUsers();

        assertThat(users.size()).isGreaterThan(0);

        int size = random.nextInt(users.size()-1) + 2;

        String term =  users.stream()
                .map(UserDTO::getLoginName)
                .limit(size)
                .map(user  -> user.replaceAll(" ", "~"))
                .collect(Collectors.joining("|"));

        List<UserDTO> resultList = given().spec(defaultRequestSpec())
                .queryParam(termQueryParam, term)
                .when()
                .log().all()
                .get(searchPath)
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().body().jsonPath().getList(".", UserDTO.class);

        assertThat(users)
                .as("User list is empty, can't check if can find user")
                .isNotEmpty();

        assertThat(resultList)
                .as("User search result list shouldn't be empty")
                .isNotEmpty();

        assertThat(resultList.size())
                .as(String.format("Should find %s, but find only %s Users", size, resultList.size()))
                .isEqualTo(size);
    }

    @Test
    public void canGetUserBySurname(){
        final String surnamePath = getTestEnvironment().getUserPath() + "/surname";
        final String surnameQueryParam = "surname";
        List<UserDTO> users = getListOfUsers();

        String userSurname = users.get(new Random().nextInt(users.size())).getSurname();

        List<UserDTO> resultList = given().spec(defaultRequestSpec())
                .queryParam(surnameQueryParam, userSurname)
                .when()
                .log().all()
                .get(surnamePath)
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", UserDTO.class);

        assertThat(resultList.size())
                .as("There should be no users with same login")
                .isGreaterThan(0);

        assertThat(resultList)
                .as("Found user with wrong surname")
                .allMatch(user -> user.getSurname().equals(userSurname));
    }

    private List<UserDTO> getListOfUsers(){
        String pathToList = getTestEnvironment().getUserPath() + "/list";
        return given().spec(defaultRequestSpec())
                .when()
                .log().all()
                .get(pathToList)
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", UserDTO.class);

    }
}
