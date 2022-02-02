package com.test.api.user;

import com.test.api.category.CategoryTest;
import com.test.api.dto.UserDTO;
import com.test.api.execution.BaseTest;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserTest extends BaseTest {

    private final List<Integer> idUserToClean = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(CategoryTest.class);
    private final String idQueryParam = "id";

    @AfterEach
    void cleanup(TestInfo testInfo) {
        if (testInfo.getTags().contains("needs-cleanup")) {
            for(int id : idUserToClean) {
                given().spec(defaultRequestSpec())
                        .queryParam(idQueryParam, id)
                        .when()
                        .delete(getTestEnvironment().getUserPath());
            }
        }
    }

    @Test
    public void canGetUserByCorrectId(){
        List<UserDTO> users = getListOfUsers();
        final int id = users.get(new Random().nextInt(users.size())).getId();

        Response response = getResponseFromUserById(id);

        UserDTO user = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .log().all()
                .get(getTestEnvironment().getUserPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(user.getId())
                .as(String.format("Should be user with id %s, but was with %s", id, user.getId()))
                .isEqualTo(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -20, (long) 0, Long.MAX_VALUE})
    public void canNotGetUserByWrongId(long id){

        Response response = getResponseFromUserById((int)id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void canListAllUsers(){
        List<UserDTO> usersList = getListOfUsers();
        UserDTO userWithEmptyFields = new UserDTO();
        userWithEmptyFields.setLoginName("");
        userWithEmptyFields.setPassword("");
        userWithEmptyFields.setFirstName("");
        userWithEmptyFields.setMiddleName("");
        userWithEmptyFields.setSurname("");
        userWithEmptyFields.setEmail("");
        userWithEmptyFields.setPathToAvatarImage("");

        assertThat(usersList).as("User list shouldn't be empty").isNotEmpty();

        assertThat(usersList).as("Fields shouldn't be null")
                .allSatisfy(user -> assertThat(user)
                .hasNoNullFieldsOrProperties());

        assertThat(usersList).as("Fields shouldn't be empty")
                .allSatisfy(user -> assertThat(user)
                .usingRecursiveComparison().isNotEqualTo(userWithEmptyFields));
    }

    @Test
    public void canGetUserByLoginName(){
        final String loginQueryParam = "login";

        List<UserDTO> users = getListOfUsers();
        String userLogin = users.get(new Random().nextInt(users.size())).getLoginName();

        List<UserDTO> usersList = given().spec(defaultRequestSpec())
                .queryParam(loginQueryParam, userLogin)
                .when()
                .log().all()
                .get(getTestEnvironment().getUserLoginPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", UserDTO.class);

        assertThat(usersList).as("List shouldn't be empty").isNotEmpty();

        assertThat(usersList.size()).as("There should be no users with same login")
                .isEqualTo(1);

        assertThat(usersList)
                .as("User with wrong login was found", userLogin)
                .allMatch(user -> user.getLoginName().equals(userLogin));
    }


    @Test
    public void canNotGetUserByWrongLoginName(){
        final String loginQueryParam = "login";
        final String userLogin = "ThereIsNotUserWithThatLogin";

        List<UserDTO> usersList = given().spec(defaultRequestSpec())
                .queryParam(loginQueryParam, userLogin)
                .when()
                .log().all()
                .get(getTestEnvironment().getUserLoginPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", UserDTO.class);

        assertThat(usersList).isEmpty();
    }

    @Test
    public void canSearchUserByTerm(){
        final String termQueryParam = "term";
        Random random = new Random();
        List<UserDTO> users = getListOfUsers();

        assertThat(users)
                .as("User list is empty, can't check if can find user")
                .isNotEmpty();

        int size = random.nextInt(users.size()) + 2;

        String term =  users.stream()
                .map(UserDTO::getLoginName)
                .limit(size)
                .map(user  -> user.replaceAll(" ", "~"))
                .collect(Collectors.joining("|"));

        List<UserDTO> resultList = given().spec(defaultRequestSpec())
                .queryParam(termQueryParam, term)
                .when()
                .log().all()
                .get(getTestEnvironment().getUserSearchPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().body().jsonPath().getList(".", UserDTO.class);

        assertThat(resultList)
                .as("User search result list shouldn't be empty")
                .isNotEmpty();

        assertThat(resultList.size())
                .as(String.format("Should find %s, but find only %s Users", size, resultList.size()))
                .isEqualTo(size);
    }

    @Test
    public void canGetUserBySurname(){
        final String surnameQueryParam = "surname";
        List<UserDTO> usersList = getListOfUsers();

        String userSurname = usersList.get(new Random().nextInt(usersList.size())).getSurname();

        List<UserDTO> resultList = given().spec(defaultRequestSpec())
                .queryParam(surnameQueryParam, userSurname)
                .when()
                .log().all()
                .get(getTestEnvironment().getUserSurnamePath())
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

    // when creating new user pathToAvatarImage is created, so we have to omit that field while asserting equals
    @ParameterizedTest()
    @MethodSource("goodParametersForPost")
    @Tag("needs-cleanup")
    public void canPostNewUser(String loginName, String password, String email, String firstName, String middleName,
                               String surname, String pathToAvatarImage, int[] addressIds, int[] paymentCardIds, String message){
        logger.info(message);

        UserDTO newUser = new UserDTO();
        newUser.setLoginName(loginName);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setMiddleName(middleName);
        newUser.setSurname(surname);
        newUser.setPathToAvatarImage(pathToAvatarImage);
        newUser.setAddressIds(addressIds);
        newUser.setPaymentCardIds(paymentCardIds);

        UserDTO createdUser = given().spec(defaultRequestSpec())
                .body(newUser)
                .log().all()
                .when()
                .post(getTestEnvironment().getUserPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(UserDTO.class);

        Response responseGet = getResponseFromUserById(createdUser.getId());
        idUserToClean.add(createdUser.getId());

        assertThat(createdUser).usingRecursiveComparison()
                .ignoringFields(idQueryParam, "pathToAvatarImage")
                .isEqualTo(newUser);

        assertThat(responseGet.getStatusCode()).isEqualTo(200);
    }


    @ParameterizedTest()
    @MethodSource("wrongParametersForPost")
    public void canNotPostNewUserWithWrongParameters(String loginName, String password, String email, String firstName,
                                                     String middleName, String surname, String pathToAvatarImage, int[] addressIds, int[] paymentCardIds, String message){
        logger.info(message);

        UserDTO newUser = new UserDTO();
        newUser.setLoginName(loginName);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setMiddleName(middleName);
        newUser.setSurname(surname);
        newUser.setPathToAvatarImage(pathToAvatarImage);
        newUser.setAddressIds(addressIds);
        newUser.setPaymentCardIds(paymentCardIds);

        Response response = given().spec(defaultRequestSpec())
                .body(newUser)
                .log().all()
                .when()
                .post(getTestEnvironment().getUserPath())
                .then()
                .log().all()
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Disabled("Put request don't work, returns code 500")
    @Test
    public void canUpdateExistingUser(){
        UserDTO newUser = new UserDTO();
        newUser.setLoginName("jan");
        newUser.setPassword("password123");
        newUser.setEmail("j@k");
        newUser.setFirstName("Jan");
        newUser.setMiddleName("Janusz");
        newUser.setSurname("Kowalski");
        newUser.setPathToAvatarImage("path");
        newUser.setAddressIds(new int[]{0});
        newUser.setPaymentCardIds(new int[]{0});

        UserDTO createdUser = given().spec(defaultRequestSpec())
                .body(newUser)
                .log().all()
                .when()
                .post(getTestEnvironment().getUserPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(UserDTO.class);

        int id = createdUser.getId();
        String newName = "Tadeusz";
        createdUser.setFirstName(newName);
        createdUser.setId(id);

        idUserToClean.add(id);
        UserDTO updatedUser = getListOfUsers().stream().filter(user -> user.getId().equals(id)).collect(Collectors.toList()).get(0);

        assertThat(updatedUser.getFirstName())
                .as(String.format("FirstName %s wasn't changed to %s", updatedUser.getFirstName(), newName))
                .isEqualTo(newName);
    }

    @Test
    public void canDeleteExistingUser(){
        UserDTO newUser = new UserDTO();
        newUser.setLoginName("jan");
        newUser.setPassword("password123");
        newUser.setEmail("j@k");
        newUser.setFirstName("Jan");
        newUser.setMiddleName("Janusz");
        newUser.setSurname("Kowalski");
        newUser.setPathToAvatarImage("path");
        newUser.setAddressIds(new int[]{0});
        newUser.setPaymentCardIds(new int[]{0});

        UserDTO createdUser = given().spec(defaultRequestSpec())
                .body(newUser)
                .log().all()
                .when()
                .post(getTestEnvironment().getUserPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(UserDTO.class);

        int id = createdUser.getId();

        Response responseDelete = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .log().all()
                .delete(getTestEnvironment().getUserPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        Response responseGetDeleted = getResponseFromUserById(id);

        assertThat(responseDelete.getStatusCode()).isEqualTo(200);

        assertThat(responseGetDeleted.getStatusCode())
                .as("Category should be deleted")
                .isEqualTo(404);
    }

    @Test
    public void canNotDeleteNonExistingUser(){
        int id = 0;

        Response response = getResponseFromUserById(id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    /**
     * Method returns User and expects that user exist
     *
     * @param id category identifier
     * @return UserDTO object
     */
    private Response getResponseFromUserById(int id){
        return given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .get(getTestEnvironment().getUserPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().response();
    }

    /**
     * Method returns List<UserDTO> and expects that user exist
     */
    private List<UserDTO> getListOfUsers(){
        return given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getUsersListPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", UserDTO.class);
    }

    private static Stream<Arguments> goodParametersForPost() {
        String user = RandomStringUtils.randomAlphabetic(20);
        String[] domains = new String[]{"@gmail.com", "@opoczta.pl", "@op.pl", "@poczta.onet.pl"};

        return Stream.of(
                Arguments.of(RandomStringUtils.randomAlphabetic(1),"password123", "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with min value of loginName length"),
                Arguments.of(RandomStringUtils.randomAlphabetic(729),"password123", "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with max value of loginName length"),
                Arguments.of("jan",RandomStringUtils.randomAlphabetic(1), "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with min value of password length"),
                Arguments.of("jan",RandomStringUtils.randomAlphabetic(288), "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with min value of password length"),
                Arguments.of("jan","password123", "j@k", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with min value of email length"),
                Arguments.of("jan","password123",String.format("%s%s", user, domains[new Random().nextInt(domains.length)]) , "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with max email length that pass"),
                Arguments.of("jan","password123", "email@123.a", RandomStringUtils.randomAlphabetic(1), "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with min value of firstName length"),
                Arguments.of("jan","password123", "email@123.a", RandomStringUtils.randomAlphabetic(288), "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with min value of firstName length"),
                Arguments.of("jan","password123", "email@123.a", "Jan", RandomStringUtils.randomAlphabetic(1), "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with min value of middleName length"),
                Arguments.of("jan","password123", "email@123.a", "Jan", RandomStringUtils.randomAlphabetic(288), "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with min value of middleName length"),
                Arguments.of("jan","password123", "email@123.a", "Jan", "Janusz", RandomStringUtils.randomAlphabetic(1), "avatar", new int[]{}, new int[]{}, "Case with min value of surname length"),
                Arguments.of("jan","password123", "email@123.a", "Jan", "Janusz", RandomStringUtils.randomAlphabetic(288), "avatar", new int[]{}, new int[]{}, "Case with min value of surname length")
        );
    }

    private static Stream<Arguments> wrongParametersForPost() {
        String user = RandomStringUtils.randomAlphabetic(100);
        String domain = RandomStringUtils.randomAlphabetic(300);

        return Stream.of(
                Arguments.of("","password123", "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with empty loginName"),
                Arguments.of(RandomStringUtils.randomAlphabetic(730),"password123", "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too long loginName"),
                Arguments.of(RandomStringUtils.randomAlphabetic(900),"password123", "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too much long loginName"),
                Arguments.of("jan","", "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with empty password"),
                Arguments.of("jan",RandomStringUtils.randomAlphabetic(289), "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too long password"),
                Arguments.of("jan",RandomStringUtils.randomAlphabetic(350), "email@123.a", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too much long password"),
                Arguments.of("jan","password123", "", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with empty email"),
                Arguments.of("jan","password123", "j@", "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too small email length"),
                Arguments.of("jan","password123", String.format("%s@%s", user, domain), "Jan", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too long email"),
                Arguments.of("jan","password123", "email@123.a", "", "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with min value of firstName length"),
                Arguments.of("jan","password123", "email@123.a", RandomStringUtils.randomAlphabetic(289), "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too long firstName"),
                Arguments.of("jan","password123", "email@123.a", RandomStringUtils.randomAlphabetic(300), "Janusz", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too much long firstName"),
                Arguments.of("jan","password123", "email@123.a", "Jan", "", "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with empty middleName"),
                Arguments.of("jan","password123", "email@123.a", "Jan", RandomStringUtils.randomAlphabetic(289), "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too long middleName"),
                Arguments.of("jan","password123", "email@123.a", "Jan", RandomStringUtils.randomAlphabetic(300), "Kowalski", "avatar", new int[]{}, new int[]{}, "Case with too much long middleName"),
                Arguments.of("jan","password123", "email@123.a", "Jan", "Janusz", "", "avatar", new int[]{}, new int[]{}, "Case with empty surname"),
                Arguments.of("jan","password123", "email@123.a", "Jan", "Janusz", RandomStringUtils.randomAlphabetic(289), "avatar", new int[]{}, new int[]{}, "Case with too long surname"),
                Arguments.of("jan","password123", "email@123.a", "Jan", "Janusz", RandomStringUtils.randomAlphabetic(350), "avatar", new int[]{}, new int[]{}, "Case with too much long surname")
        );
    }
}
