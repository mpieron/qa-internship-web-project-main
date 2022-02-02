package com.test.api.payment;

import com.test.api.dto.PaymentCardDTO;
import com.test.api.execution.BaseTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class PaymentCardTest extends BaseTest {
    private final String idQueryParam = "id";

    @Test
    public void canGetCategoryByCorrectId() {
        List<PaymentCardDTO> paymentCardsList = given().spec(defaultRequestSpec())
                .when()
                .log().all()
                .get(getTestEnvironment().getPaymentCardListPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", PaymentCardDTO.class);

        final long id = paymentCardsList.get(new Random().nextInt(paymentCardsList.size())).getId();
        Response response = getResponseFromExistingPaymentCardById(id);
        PaymentCardDTO paymentCard = getPaymentCardById(id);

        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(paymentCard.getId())
                .as(String.format("Should be PaymentCard with id %s, but was with %s", id, paymentCard.getId()))
                .isEqualTo(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -20, 0})
    public void canNotGetCategoryByWrongId(long id) {
        Response response = getResponseFromExistingPaymentCardById(id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void canListAllPaymentCards(){

        List<PaymentCardDTO> paymentCardsList = getPaymentCardList();

        PaymentCardDTO paymentCardWithEmptyFields = new PaymentCardDTO();
        paymentCardWithEmptyFields.setCardCode("");
        paymentCardWithEmptyFields.setCardNumber("");
        paymentCardWithEmptyFields.setCardNickName("");
        paymentCardWithEmptyFields.setExpirationDate("");
        paymentCardWithEmptyFields.setOwnerName("");

        assertThat(paymentCardsList).as("PaymentCard list shouldn't be empty").isNotEmpty();

        assertThat(paymentCardsList).as("PaymentCard fields shouldn't be null")
                .allSatisfy(user -> assertThat(user)
                        .hasNoNullFieldsOrProperties());

        assertThat(paymentCardsList).as("PaymentCard fields shouldn't be empty")
                .allSatisfy(user -> assertThat(user)
                        .usingRecursiveComparison().isNotEqualTo(paymentCardWithEmptyFields));
    }

    // cardNickName is the term
    @Test
    public void canSearchPaymentCardByTerm(){
        final String termQueryParam = "term";
        Random random = new Random();
        List<PaymentCardDTO> paymentCardsList = getPaymentCardList();

        assertThat(paymentCardsList)
                .as("PaymentCard list is empty, can't check if can find PaymentCard")
                .isNotEmpty();

        int size = random.nextInt(paymentCardsList.size()) + 1;

        String term =  paymentCardsList.stream()
                .map(PaymentCardDTO::getCardNickName)
                .limit(size)
                .map(user  -> user.replaceAll(" ", "~"))
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

    /**
     * Methods returns response from get request and expects that
     * PaymentCard with given id exist
     *
     * @param id paymentcard identifier
     * @return Response object
     */
    private Response getResponseFromExistingPaymentCardById(long id){
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
    private PaymentCardDTO getPaymentCardById(long id){
        return given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .get(getTestEnvironment().getPaymentCardPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().as(PaymentCardDTO.class);
    }

    private List<PaymentCardDTO> getPaymentCardList(){
        return given().spec(defaultRequestSpec())
                .when()
                .log().all()
                .get(getTestEnvironment().getPaymentCardListPath())
                .then().
                log().all()
                .extract().jsonPath().getList(".", PaymentCardDTO.class);
    }
}
