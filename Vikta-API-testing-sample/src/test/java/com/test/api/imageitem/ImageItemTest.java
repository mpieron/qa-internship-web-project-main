package com.test.api.imageitem;

import com.test.api.dto.ImageItemDTO;
import com.test.api.execution.BaseTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ImageItemTest extends BaseTest {

    private final String idQueryParam = "id";

    @Test
    public void canGetImageItemByExistingId(){
        List<ImageItemDTO> imageItemsList = getImageItemsList();

        final long id = imageItemsList.get(new Random().nextInt(imageItemsList.size())).getId();
        Response response = getResponseFromGetImageItemById(id);
        ImageItemDTO imageItem = getImageItemById(id);

        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(imageItem.getId())
                .as(String.format("Should be ImageItem with id %s, but was with %s", id, imageItem.getId()))
                .isEqualTo(id);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -1, 0})
    public void canNotGetImageItemByWrongId(long id){
        Response response = getResponseFromGetImageItemById(id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void canListAllImageItems(){
        List<ImageItemDTO> imageItemsList = getImageItemsList();
        Set<Long> categoryIds = new HashSet<>();
        List<String> tags = new ArrayList<>();
        Double price = 1.0;

        ImageItemDTO emptyImageItem = new ImageItemDTO();
        emptyImageItem.setPathToImage("");
        emptyImageItem.setTitle("");
        emptyImageItem.setCategoryIds(categoryIds);
        emptyImageItem.setTags(tags);
        emptyImageItem.setAuthor("");
        emptyImageItem.setDescription("");
        emptyImageItem.setPrice(price);
        emptyImageItem.setRating(0);

        assertThat(imageItemsList).as("ImageItem list shouldn't be empty").isNotEmpty();

        assertThat(imageItemsList).as("ImageItem fields shouldn't be empty")
                .allSatisfy(imageItem -> assertThat(imageItem)
                        .usingRecursiveComparison().ignoringFields("description", "price", "rating")
                        .isNotEqualTo(emptyImageItem));

        assertThat(imageItemsList).as("Rating should be in range [0,5]")
                .allSatisfy(imageItem -> assertThat(imageItem.getRating())
                        .isBetween(0,5));

        assertThat(imageItemsList).as("Price shouldn't be negative")
                .allSatisfy(imageItem -> assertThat(imageItem.getPrice())
                        .isPositive());
    }

    @Test
    public void canSearchImageItemByTerm() {
        final String termQueryParam = "term";
        Random random = new Random();
        List<ImageItemDTO> imageItemsList = getImageItemsList();

        assertThat(imageItemsList)
                .as("ImageItem list is empty, can't check if can find ImageItem")
                .isNotEmpty();

        int size = random.nextInt(imageItemsList.size()) + 1;

        String term = imageItemsList.stream()
                .map(ImageItemDTO::getTags)
                .limit(size)
                .map(imageItem -> imageItem.toString()
                        .substring(1,imageItem.size())
                        .replaceAll(" ", "~"))
                .collect(Collectors.joining("|"));

        List<ImageItemDTO> resultList = given().spec(defaultRequestSpec())
                .queryParam(termQueryParam, term)
                .when()
                .log().all()
                .get(getTestEnvironment().getImageitemSearchPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().body().jsonPath().getList(".", ImageItemDTO.class);

        assertThat(resultList)
                .as("ImageItem search result list shouldn't be empty")
                .isNotEmpty();

        assertThat(resultList.size())
                .as("Should find more imageItems")
                .isGreaterThan(size);
    }

    // not always find everything, but probably there's something wrong with implementation
    // when executing the same parameters in swagger, then sometimes finds, sometimes returns empty body
    @ParameterizedTest
    @MethodSource("goodParametersForSearchAdvanced")
    public void canSearchAdvancedImageItemByTerm(String queryParam) {
        List<ImageItemDTO> imageItemsList = getImageItemsList();
        String path = String.format("%s?%s",getTestEnvironment().getImageitemSearchAdvancedPath(), queryParam);

        assertThat(imageItemsList)
                .as("ImageItem list is empty, can't check if can find ImageItem")
                .isNotEmpty();

        List<ImageItemDTO> resultList = given().spec(defaultRequestSpec())
                .when()
                .log().all()
                .get(path)
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().body().jsonPath().getList(".", ImageItemDTO.class);

        assertThat(resultList)
                .as("ImageItem search result list shouldn't be empty")
                .isNotEmpty();
    }

        /**
         * Methods returns ImageItemDTO object and expects that ImageItem exist
         *
         * @param id ImageItem identifier
         * @return ImageItemDTO object
         */
    private ImageItemDTO getImageItemById(long id) {
        return given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .get(getTestEnvironment().getImageitemPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().as(ImageItemDTO.class);
    }

    /**
     * Methods returns response from get request and expects that
     * ImageItem with given id exist
     *
     * @param id ImageItem identifier
     * @return Response object
     */
    private Response getResponseFromGetImageItemById(long id) {
        return given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .get(getTestEnvironment().getImageitemPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().response();
    }
    /**
     * Methods returns List of ImageItems
     *
     * @return List<ImageItemDTO>
     */
    private List<ImageItemDTO> getImageItemsList() {
        return given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getImageitemListPath())
                .then()
                .extract().jsonPath().getList(".", ImageItemDTO.class);
    }

    private static Stream<Arguments> goodParametersForSearchAdvanced() {
        return Stream.of(
                Arguments.of("priceMax=100.24&priceMin=1.23&ratingMax=4"),
                Arguments.of("priceMin=1.23&ratingMax=3"),
                Arguments.of("ratingMax=1"),
                Arguments.of("ratingMax=5&ratingMin=2")
        );
    }
}
