package com.test.api.address;


import com.test.api.dto.AddressDTO;
import com.test.api.execution.BaseTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class AddressTest extends BaseTest {

    private final String idQueryParam = "id";

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
                .map(user -> user.replaceAll(" ", "~"))
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
}
