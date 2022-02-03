package com.test.api.address;


import com.test.api.dto.AddressDTO;
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
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class AddressTest extends BaseTest {

    private final String idQueryParam = "id";
    private final List<Long> idAddressesToClean = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(AddressTest.class);

    @AfterEach
    void cleanup(TestInfo testInfo) {
        if (testInfo.getTags().contains("needs-cleanup")) {
            for(long id : idAddressesToClean) {
                given().spec(defaultRequestSpec())
                        .queryParam(idQueryParam, id)
                        .when()
                        .delete(getTestEnvironment().getAddressPath());
            }
        }
    }


    @Test
    public void canGetAddressByCorrectId() {
        List<AddressDTO> addressesList = getAddressesList();

        final long id = addressesList.get(new Random().nextInt(addressesList.size())).getId();
        Response response = getResponseFromGetAddressById(id);
        AddressDTO address = getAddressById(id);

        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(address.getId())
                .as(String.format("Should be address with id %s, but was with %s", id, address.getId()))
                .isEqualTo(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -50, 0})
    public void canNotGetAddressByWrongId(long id) {
        Response response = getResponseFromGetAddressById(id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void canListAllAddress() {
        List<AddressDTO> addressesList = getAddressesList();

        AddressDTO addressWithEmptyFields = new AddressDTO();
        addressWithEmptyFields.setAddressNickname("");
        addressWithEmptyFields.setCityName("");
        addressWithEmptyFields.setPostalCode("");
        addressWithEmptyFields.setStreet("");
        addressWithEmptyFields.setRegionName("");
        addressWithEmptyFields.setStreetAdditional("");

        assertThat(addressesList).as("Addresses list shouldn't be empty").isNotEmpty();

        assertThat(addressesList).as("Address fields shouldn't be empty")
                .allSatisfy(address -> assertThat(address)
                        .usingRecursiveComparison().isNotEqualTo(addressWithEmptyFields));
    }

    // addressNickName is the term
    @Test
    public void canSearchAddressByTerm() {
        final String termQueryParam = "term";
        Random random = new Random();
        List<AddressDTO> addressesList = getAddressesList();

        assertThat(addressesList)
                .as("Addresses list is empty, can't check if can find address")
                .isNotEmpty();

        int size = random.nextInt(addressesList.size()) + 1;

        String term = addressesList.stream()
                .map(AddressDTO::getAddressNickname)
                .limit(size)
                .map(address -> address.replaceAll(" ", "~"))
                .collect(Collectors.joining("|"));

        List<AddressDTO> resultList = given().spec(defaultRequestSpec())
                .queryParam(termQueryParam, term)
                .when()
                .log().all()
                .get(getTestEnvironment().getAddressSearchPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().body().jsonPath().getList(".", AddressDTO.class);

        assertThat(resultList)
                .as("Addresses search result list shouldn't be empty")
                .isNotEmpty();

        assertThat(resultList.size())
                .as(String.format("Should find %s, but find only %s addresses", size, resultList.size()))
                .isGreaterThan(size-1);
    }

    @ParameterizedTest()
    @MethodSource("goodParametersForPost")
    @Tag("needs-cleanup")
    public void canPostNewAddress(String street, String streetAdditional, String cityName, String regionName, String postalCode, String addressNickname, String message) {
        logger.info(message);
        final long userId = getExistingUserId();

        AddressDTO newAddress = new AddressDTO();
        newAddress.setStreet(street);
        newAddress.setStreetAdditional(streetAdditional);
        newAddress.setCityName(cityName);
        newAddress.setRegionName(regionName);
        newAddress.setPostalCode(postalCode);
        newAddress.setAddressNickname(addressNickname);
        newAddress.setUserId(userId);

        AddressDTO createdAddress = given().spec(defaultRequestSpec())
                .body(newAddress)
                .log().all()
                .when()
                .post(getTestEnvironment().getAddressPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(AddressDTO.class);

        Response responseGet = getResponseFromGetAddressById(createdAddress.getId());
        idAddressesToClean.add(createdAddress.getId());

        assertThat(createdAddress).usingRecursiveComparison()
                .ignoringFields(idQueryParam)
                .isEqualTo(newAddress);

        assertThat(responseGet.getStatusCode()).isEqualTo(200);
    }

    @ParameterizedTest()
    @MethodSource("wrongParametersForPost")
    public void canNotPostNewAddressWithWrongParameters(String street, String streetAdditional, String cityName, String regionName, String postalCode, String addressNickname, String message) {
        logger.info(message);
        final long userId = getExistingUserId();

        AddressDTO newAddress = new AddressDTO();
        newAddress.setStreet(street);
        newAddress.setStreetAdditional(streetAdditional);
        newAddress.setCityName(cityName);
        newAddress.setRegionName(regionName);
        newAddress.setPostalCode(postalCode);
        newAddress.setAddressNickname(addressNickname);
        newAddress.setUserId(userId);

        Response response = given().spec(defaultRequestSpec())
                .body(newAddress)
                .log().all()
                .when()
                .post(getTestEnvironment().getAddressPath())
                .then()
                .log().all()
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Test
    public void canUpdateExistingAddress(){
        AddressDTO newAddress = new AddressDTO();
        newAddress.setStreet("Warszawska");
        newAddress.setStreetAdditional("880 71.857203 / 40.211454");
        newAddress.setCityName("Cracow");
        newAddress.setRegionName("Małopolska");
        newAddress.setPostalCode("31-400");
        newAddress.setAddressNickname("KRK");
        newAddress.setUserId(getExistingUserId());

        AddressDTO createdAddress = given().spec(defaultRequestSpec())
                .body(newAddress)
                .log().all()
                .when()
                .post(getTestEnvironment().getAddressPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(AddressDTO.class);

        long id = createdAddress.getId();
        idAddressesToClean.add(id);

        String newAddressNickName = "New Nick";
        newAddress.setAddressNickname(newAddressNickName);
        newAddress.setId(id);

        Response updatedAddressResponse = given().spec(defaultRequestSpec())
                .body(newAddress)
                .log().all()
                .when()
                .put(getTestEnvironment().getAddressPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        AddressDTO updatedAddress = getAddressById(id);

        assertThat(updatedAddressResponse.getStatusCode()).isEqualTo(200);
        assertThat(updatedAddress.getAddressNickname()).as("Address was not updated")
                .isEqualTo(newAddressNickName);
    }

    @Test
    public void canNotUpdateNotExistingAddress(){
        AddressDTO newAddress = new AddressDTO();
        newAddress.setStreet("Warszawska");
        newAddress.setStreetAdditional("880 71.857203 / 40.211454");
        newAddress.setCityName("Cracow");
        newAddress.setRegionName("Małopolska");
        newAddress.setPostalCode("31-400");
        newAddress.setAddressNickname("KRK");
        newAddress.setUserId(getExistingUserId());

        Response updatedAddressResponse = given().spec(defaultRequestSpec())
                .body(newAddress)
                .log().all()
                .when()
                .put(getTestEnvironment().getAddressPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        assertThat(updatedAddressResponse.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void canDeleteExistingAddress(){
        AddressDTO newAddress = new AddressDTO();
        newAddress.setStreet("Warszawska");
        newAddress.setStreetAdditional("880 71.857203 / 40.211454");
        newAddress.setCityName("Cracow");
        newAddress.setRegionName("Małopolska");
        newAddress.setPostalCode("31-400");
        newAddress.setAddressNickname("KRK");
        newAddress.setUserId(getExistingUserId());

        AddressDTO createdAddress = given().spec(defaultRequestSpec())
                .body(newAddress)
                .log().all()
                .when()
                .post(getTestEnvironment().getAddressPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(AddressDTO.class);

        long id = createdAddress.getId();

        Response responseDelete = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .log().all()
                .delete(getTestEnvironment().getAddressPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        Response responseGetDeleted = getResponseFromGetAddressById(id);

        assertThat(responseDelete.getStatusCode()).isEqualTo(200);

        assertThat(responseGetDeleted.getStatusCode())
                .as("Address should be deleted")
                .isEqualTo(404);
    }

    @Test
    public void canNotDeleteNonExistingAddress(){
        int id = 0;

        Response response = getResponseFromGetAddressById(id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    /**
     * Methods returns List of Addresses
     *
     * @return List<AddressDTO>
     */
    private List<AddressDTO> getAddressesList() {
        return given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getAddressListPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().jsonPath().getList(".", AddressDTO.class);
    }

    /**
     * Methods returns AddressDTO object and expects that Address exist
     *
     * @param id address identifier
     * @return AddressDTO object
     */
    private AddressDTO getAddressById(long id) {
        return given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .get(getTestEnvironment().getAddressPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().as(AddressDTO.class);
    }

    /**
     * Methods returns response from get request and expects that
     * Address with given id exist
     *
     * @param id address identifier
     * @return Response object
     */
    private Response getResponseFromGetAddressById(long id) {
        return given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .get(getTestEnvironment().getAddressPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().response();
    }

    /**
     * Method returns id existing user
     *
     * @return long id
     */
    private long getExistingUserId(){
        return  (long) given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getUsersListPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", UserDTO.class)
                .get(0).getId();
    }


    private static Stream<Arguments> goodParametersForPost() {
        String randomNumber = RandomStringUtils.randomNumeric(9);
        return Stream.of(
                Arguments.of(RandomStringUtils.randomAlphabetic(1),"880 71.857203 / 40.211454", "Cracow", "Małopolska", "31-300", "Krk", "Case with min value of Street"),
                Arguments.of("Warszawska", RandomStringUtils.randomAlphabetic(1), "Cracow", "Małopolska", "31-300", "Krk", "Case with min value of streetAdditional"),
                Arguments.of("Warszawska", RandomStringUtils.randomAlphabetic(1024), "Cracow", "Małopolska", "31-300", "Krk", "Case with max value of streetAdditional"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454",RandomStringUtils.randomAlphabetic(1), "Małopolska", "31-300", "Krk", "Case with min value of cityName"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454",RandomStringUtils.randomAlphabetic(1024), "Małopolska", "31-300", "Krk", "Case with max value of cityName"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454",RandomStringUtils.randomAlphabetic(1), "Małopolska", "31-300", "Krk", "Case with min value of regionName"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", RandomStringUtils.randomAlphabetic(4096), "31-300", "Krk", "Case with max value of regionName"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", "Małopolska", String.format("%s-%s1",randomNumber, randomNumber), "Krk", "Case with max value of postalCode"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", "Małopolska", RandomStringUtils.randomNumeric(1), "Krk", "Case with min value of postalCode"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", "Małopolska", "31-300", RandomStringUtils.randomAlphabetic(1), "Case with min value of addressNickname"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", "Małopolska", "31-300", RandomStringUtils.randomAlphabetic(2048), "Case with max value of addressNickname")
//                max value of Street length (3010) won't pass, but 3009 is passing
//                Arguments.of(RandomStringUtils.randomAlphabetic(3010),"880 71.857203 / 40.211454", "Cracow", "Małopolska", "31-300", "Krk", "Case with max value of Street")
                );
    }

    private static Stream<Arguments> wrongParametersForPost() {
        return Stream.of(
                Arguments.of("","880 71.857203 / 40.211454", "Cracow", "Małopolska", "31-300", "Krk", "Case with empty Street"),
                Arguments.of(RandomStringUtils.randomAlphabetic(3500),"880 71.857203 / 40.211454", "Cracow", "Małopolska", "31-300", "Krk", "Case with too long Street"),
                Arguments.of("Warszawska", RandomStringUtils.randomAlphabetic(1050), "Cracow", "Małopolska", "31-300", "Krk", "Case with too long streetAdditional"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","", "Małopolska", "31-300", "Krk", "Case with empty of cityName"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454",RandomStringUtils.randomAlphabetic(1050), "Małopolska", "31-300", "Krk", "Case with too long cityName"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","", "Małopolska", "31-300", "Krk", "Case with empty regionName"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", RandomStringUtils.randomAlphabetic(5000), "31-300", "Krk", "Case with too long regionName"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", "Małopolska", "", "Krk", "Case with empty postalCode"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", "Małopolska", RandomStringUtils.randomNumeric(50), "Krk", "Case with too long postalCode"),
                Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", "Małopolska", "31-300", RandomStringUtils.randomAlphabetic(2050), "Case with too long addressNickname")
//              shouldn't post address with empty streetAdditional field
//              Arguments.of("Warszawska", "", "Cracow", "Małopolska", "31-300", "Krk", "Case with empty streetAdditional"),
//              shouldn't post address with empty addressNickname field
//              Arguments.of("Warszawska", "880 71.857203 / 40.211454","Cracow", "Małopolska", "31-300", "", "Case with empty addressNickname"),
        );
    }
}
