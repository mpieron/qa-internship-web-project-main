package com.test.api.payment;

import com.test.api.dto.PaymentCardDTO;
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
import static org.assertj.core.api.Assertions.*;

public class PaymentCardTest extends BaseTest {

    private final String idQueryParam = "id";
    private final List<Long> idPaymentCardsToClean = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(PaymentCardTest.class);

    @AfterEach
    void cleanup(TestInfo testInfo) {
        if (testInfo.getTags().contains("needs-cleanup")) {
            for(long id : idPaymentCardsToClean) {
                given().spec(defaultRequestSpec())
                        .queryParam(idQueryParam, id)
                        .when()
                        .delete(getTestEnvironment().getPaymentCardPath());
            }
        }
    }


    @Test
    public void canGetCategoryByCorrectId() {
        List<PaymentCardDTO> paymentCardsList = getPaymentCardList();

        final long id = paymentCardsList.get(new Random().nextInt(paymentCardsList.size())).getId();
        Response response = getResponseFromGetPaymentCardById(id);
        PaymentCardDTO paymentCard = getPaymentCardById(id);

        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(paymentCard.getId())
                .as(String.format("Should be PaymentCard with id %s, but was with %s", id, paymentCard.getId()))
                .isEqualTo(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -20, 0})
    public void canNotGetCategoryByWrongId(long id) {
        Response response = getResponseFromGetPaymentCardById(id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void canListAllPaymentCards() {

        List<PaymentCardDTO> paymentCardsList = getPaymentCardList();

        PaymentCardDTO paymentCardWithEmptyFields = new PaymentCardDTO();
        paymentCardWithEmptyFields.setCardCode("");
        paymentCardWithEmptyFields.setCardNumber("");
        paymentCardWithEmptyFields.setCardNickName("");
        paymentCardWithEmptyFields.setExpirationDate("");
        paymentCardWithEmptyFields.setOwnerName("");

        assertThat(paymentCardsList).as("PaymentCard list shouldn't be empty").isNotEmpty();

        assertThat(paymentCardsList).as("PaymentCard fields shouldn't be empty")
                .allSatisfy(user -> assertThat(user)
                        .usingRecursiveComparison().isNotEqualTo(paymentCardWithEmptyFields));
    }

    // cardNickName is the term
    @Test
    public void canSearchPaymentCardByTerm() {
        final String termQueryParam = "term";
        Random random = new Random();
        List<PaymentCardDTO> paymentCardsList = getPaymentCardList();

        assertThat(paymentCardsList)
                .as("PaymentCard list is empty, can't check if can find PaymentCard")
                .isNotEmpty();

        int size = random.nextInt(paymentCardsList.size()) + 1;

        String term = paymentCardsList.stream()
                .map(PaymentCardDTO::getCardNickName)
                .limit(size)
                .map(user -> user.replaceAll(" ", "~"))
                .collect(Collectors.joining("|"));

        List<PaymentCardDTO> resultList = given().spec(defaultRequestSpec())
                .queryParam(termQueryParam, term)
                .when()
                .log().all()
                .get(getTestEnvironment().getPaymentCardSearchPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().body().jsonPath().getList(".", PaymentCardDTO.class);

        assertThat(resultList)
                .as("PaymentCard search result list shouldn't be empty")
                .isNotEmpty();

        assertThat(resultList.size())
                .as(String.format("Should find %s, but find only %s PaymentCards", size, resultList.size()))
                .isEqualTo(size);
    }

    @ParameterizedTest()
    @MethodSource("goodParametersForPost")
    @Tag("needs-cleanup")
    public void canPostNewPaymentCard(String cardNickName, String cardNumber, String cardCode, String ownerName, String expirationDate, String message) {
        logger.info(message);

        PaymentCardDTO newPaymentCard = new PaymentCardDTO();
        newPaymentCard.setCardNickName(cardNickName);
        newPaymentCard.setCardNumber(cardNumber);
        newPaymentCard.setCardCode(cardCode);
        newPaymentCard.setOwnerName(ownerName);
        newPaymentCard.setExpirationDate(expirationDate);
        newPaymentCard.setUserId(getExistingUserId());

        PaymentCardDTO createdPaymentCard = given().spec(defaultRequestSpec())
                .body(newPaymentCard)
                .log().all()
                .when()
                .post(getTestEnvironment().getPaymentCardPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(PaymentCardDTO.class);

        Response responseGet = getResponseFromGetPaymentCardById(createdPaymentCard.getId());
        idPaymentCardsToClean.add(createdPaymentCard.getId());

        assertThat(createdPaymentCard).usingRecursiveComparison()
                .ignoringFields(idQueryParam, "pathToAvatarImage")
                .isEqualTo(newPaymentCard);

        assertThat(responseGet.getStatusCode()).isEqualTo(200);
    }

    @ParameterizedTest()
    @MethodSource("wrongParametersForPost")
    public void canNotPostNewPaymentCardWithWrongParameters(String cardNickName, String cardNumber, String cardCode, String ownerName, String expirationDate, String message) {
        logger.info(message);

        PaymentCardDTO newPaymentCard = new PaymentCardDTO();
        newPaymentCard.setCardNickName(cardNickName);
        newPaymentCard.setCardNumber(cardNumber);
        newPaymentCard.setCardCode(cardCode);
        newPaymentCard.setOwnerName(ownerName);
        newPaymentCard.setExpirationDate(expirationDate);
        newPaymentCard.setUserId(getExistingUserId());

        Response response = given().spec(defaultRequestSpec())
                .body(newPaymentCard)
                .log().all()
                .when()
                .post(getTestEnvironment().getPaymentCardPath())
                .then()
                .log().all()
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Test
    public void canNotPostNewPaymentCardWithWrongUserId() {
        long notExistingUserId = 0;
        PaymentCardDTO newPaymentCard = new PaymentCardDTO();
        newPaymentCard.setCardNickName("cardNickName");
        newPaymentCard.setCardNumber(RandomStringUtils.randomNumeric(15));
        newPaymentCard.setCardCode(RandomStringUtils.randomNumeric(3));
        newPaymentCard.setOwnerName("Jan");
        newPaymentCard.setExpirationDate("2022-01-01");
        newPaymentCard.setUserId(notExistingUserId);

        Response response = given().spec(defaultRequestSpec())
                .body(newPaymentCard)
                .log().all()
                .when()
                .post(getTestEnvironment().getPaymentCardPath())
                .then()
                .log().all()
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void canUpdateExistingPaymentCard(){
        PaymentCardDTO newPaymentCard = new PaymentCardDTO();
        newPaymentCard.setCardNickName("Nick");
        newPaymentCard.setCardNumber(RandomStringUtils.randomNumeric(14));
        newPaymentCard.setCardCode(RandomStringUtils.randomNumeric(4));
        newPaymentCard.setOwnerName("Jan");
        newPaymentCard.setExpirationDate("2022-01-01");
        newPaymentCard.setUserId(getExistingUserId());

        PaymentCardDTO createdPaymentCard = given().spec(defaultRequestSpec())
                .body(newPaymentCard)
                .log().all()
                .when()
                .post(getTestEnvironment().getPaymentCardPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(PaymentCardDTO.class);

        long id = createdPaymentCard.getId();
        idPaymentCardsToClean.add(id);

        String newCardNickName = "New Nick";
        newPaymentCard.setCardNickName(newCardNickName);
        newPaymentCard.setId(id);

        Response updatedPaymentCardResponse = given().spec(defaultRequestSpec())
                .body(newPaymentCard)
                .log().all()
                .when()
                .put(getTestEnvironment().getPaymentCardPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        PaymentCardDTO updatedPaymentCard = getPaymentCardById(id);

        assertThat(updatedPaymentCardResponse.getStatusCode()).isEqualTo(200);
        assertThat(updatedPaymentCard.getCardNickName()).as("Payment card was not updated")
                .isEqualTo(newCardNickName);
    }

    @Test
    public void canNotUpdateNotExistingPaymentCard(){
        final long id = 0;

        PaymentCardDTO newPaymentCard = new PaymentCardDTO();
        newPaymentCard.setCardNickName("Nick");
        newPaymentCard.setCardNumber(RandomStringUtils.randomNumeric(14));
        newPaymentCard.setCardCode(RandomStringUtils.randomNumeric(4));
        newPaymentCard.setOwnerName("Jan");
        newPaymentCard.setExpirationDate("2022-01-01");
        newPaymentCard.setUserId(id);

        Response updatedPaymentCardResponse = given().spec(defaultRequestSpec())
                .body(newPaymentCard)
                .log().all()
                .when()
                .put(getTestEnvironment().getPaymentCardPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        assertThat(updatedPaymentCardResponse.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void canDeleteExistingPaymentCard(){
        PaymentCardDTO newPaymentCard = new PaymentCardDTO();
        newPaymentCard.setCardNickName("Nick");
        newPaymentCard.setCardNumber(RandomStringUtils.randomNumeric(14));
        newPaymentCard.setCardCode(RandomStringUtils.randomNumeric(4));
        newPaymentCard.setOwnerName("Jan");
        newPaymentCard.setExpirationDate("2022-01-01");
        newPaymentCard.setUserId(getExistingUserId());

        PaymentCardDTO createdPaymentCard = given().spec(defaultRequestSpec())
                .body(newPaymentCard)
                .log().all()
                .when()
                .post(getTestEnvironment().getPaymentCardPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(PaymentCardDTO.class);

        long id = createdPaymentCard.getId();

        Response responseDelete = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .log().all()
                .delete(getTestEnvironment().getPaymentCardPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        Response responseGetDeleted = getResponseFromGetPaymentCardById(id);

        assertThat(responseDelete.getStatusCode()).isEqualTo(200);

        assertThat(responseGetDeleted.getStatusCode())
                .as("PaymentCard should be deleted")
                .isEqualTo(404);
    }

    @Test
    public void canNotDeleteNonExistingPaymentCard(){
        int id = 0;

        Response response = getResponseFromGetPaymentCardById(id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    /**
     * Methods returns response from get request and expects that
     * PaymentCard with given id exist
     *
     * @param id paymentcard identifier
     * @return Response object
     */
    private Response getResponseFromGetPaymentCardById(long id) {
        return given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .get(getTestEnvironment().getPaymentCardPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().response();
    }

    /**
     * Methods returns PaymentCardDTO object and expects that PaymentCard exist
     *
     * @param id paymentcard identifier
     * @return PaymentCardDTO object
     */
    private PaymentCardDTO getPaymentCardById(long id) {
        return given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .get(getTestEnvironment().getPaymentCardPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().as(PaymentCardDTO.class);
    }

    /**
     * Methods returns List of PaymentCards
     *
     * @return List<PaymentCardDTO>
     */
    private List<PaymentCardDTO> getPaymentCardList() {
        return given().spec(defaultRequestSpec())
                .when()
                .log().all()
                .get(getTestEnvironment().getPaymentCardListPath())
                .then().
                log().all()
                .extract().jsonPath().getList(".", PaymentCardDTO.class);
    }

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
        return Stream.of(
                Arguments.of(RandomStringUtils.randomAlphabetic(1), RandomStringUtils.randomNumeric(15), "123", "Jan", "2022-02-12", "Case with min cardNickName length"),
                Arguments.of(RandomStringUtils.randomAlphabetic(4096), RandomStringUtils.randomNumeric(15), "123", "Jan", "2022-02-12", "Case with max cardNickName length"),
                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(14), "123", "Jan", "2022-02-12", "Case with min cardNumber length"),
                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(16), "123", "Janusz", "2022-02-12", "Case with min cardNumber length"),
                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(15), RandomStringUtils.randomNumeric(4), "Jan", "2022-02-12", "Case with max cardCode length"),
                Arguments.of("Card Nick Name 123", RandomStringUtils.randomNumeric(15), "123", RandomStringUtils.randomAlphabetic(1), "2022-02-12", "Case with min ownerName length"),
                Arguments.of("Card Nick 123 Name", RandomStringUtils.randomNumeric(15), "123", RandomStringUtils.randomAlphabetic(4096), "2022-02-12", "Case with max ownerName length")
        );
    }


    // owner name can be a number, should be?
    // expiration date don't have any requirements, can be empty, string, number - should be some pattern for that
    // when userId is negative, then created card has this field equals null - interesting :)
    private static Stream<Arguments> wrongParametersForPost() {
        return Stream.of(
                Arguments.of("", RandomStringUtils.randomNumeric(15), "123", "Jan", "2022-02-12",  "Case with empty cardNickName"),
                Arguments.of(RandomStringUtils.randomAlphabetic(4097), RandomStringUtils.randomNumeric(15), "123", "Jan", "2022-02-12",  "Case with too long cardNickName"),
                Arguments.of(RandomStringUtils.randomAlphabetic(4500), RandomStringUtils.randomNumeric(15), "123", "Jan", "2022-02-12",  "Case with much too long cardNickName"),
                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(13), "123", "Jan", "2022-02-12",  "Case with loo short cardNumber"),
                Arguments.of("Card Nick Name", "-5", "123", "Jan", "2022-02-12",  "Case with negative cardNumber"),
                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(17), "123", "Janusz", "2022-02-12",  "Case with too long cardNumber"),
                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(50), "123", "Janusz", "2022-02-12",  "Case with much too long cardNumber"),
                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(15), "", "Jan", "2022-02-12", "Case with empty cardCode"),
                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(15), "a", "Jan", "2022-02-12",  "Case with char instead number for cardCode"),
                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(15), RandomStringUtils.randomNumeric(15), "Jan", "2022-02-12",  "Case with too long cardCode"),
                Arguments.of("Card Nick Name 123", RandomStringUtils.randomNumeric(15), "123", "", "2022-02-12",  "Case with empty ownerName"),
                Arguments.of("Card Nick 123 Name", RandomStringUtils.randomNumeric(15), "123", RandomStringUtils.randomAlphabetic(4097), "2022-02-12",  "Case with too long ownerName"),
                Arguments.of("Card Nick 123 Name", RandomStringUtils.randomNumeric(15), "123", RandomStringUtils.randomAlphabetic(4500), "2022-02-12", "Case with too much long ownerName")
//                card code accepts negative number
//                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(15), "-10", "Jan", "2022-02-12", correctUserId, "Case with minus cardCode"),
//                owner name accepts numeric string
//                Arguments.of("Card Nick 123 Name", RandomStringUtils.randomNumeric(15), "123", RandomStringUtils.randomNumeric(10), "2022-02-12", userId, "Case with numbers instead ownerName"),
//                expiration date accepts empty string
//                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(15), "123", "Jan", "", userId, "Case with empty date"),
//                expiration date accepts alphabetic string
//                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(15), "123", "Jan", RandomStringUtils.randomAlphabetic(10), userId, "Case with random string instead date"),
//                expiration date accepts numeric string - no pattern for that
//                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(15), "123", "Jan", RandomStringUtils.randomNumeric(10), userId, "Case with random numbers instead date"),
//                when userId is negative, then created card has this field equals null
//                Arguments.of("Card Nick Name", RandomStringUtils.randomNumeric(15), "123", "Jan", "2022-02-99", Long.MIN_VALUE, "Case with wrong minus userID")
        );
    }
}
