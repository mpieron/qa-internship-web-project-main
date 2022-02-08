package com.test.api.imageitem;

import com.test.api.dto.CategoryDTO;
import org.json.simple.JSONObject;
import com.test.api.dto.ImageItemDTO;
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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ImageItemTest extends BaseTest {

    private final String idQueryParam = "id";
    private final List<Long> idImageItemsToClean = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(ImageItemTest.class);

    @AfterEach
    void cleanup(TestInfo testInfo) {
        if (testInfo.getTags().contains("needs-cleanup")) {
            for(long id : idImageItemsToClean) {
                given().spec(defaultRequestSpec())
                        .queryParam(idQueryParam, id)
                        .when()
                        .delete(getTestEnvironment().getImageitemPath());
            }
        }
    }


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
                        .isGreaterThanOrEqualTo(0));
    }

    @Test
    public void canSearchImageItemByTerm() {
        final String termQueryParam = "term";
        List<ImageItemDTO> imageItemsList = getImageItemsList();

        assertThat(imageItemsList)
                .as("ImageItem list is empty, can't check if can find ImageItem")
                .isNotEmpty();

        List<String> termList = imageItemsList.stream()
                .map(ImageItemDTO::getTags)
                .flatMap(Collection::stream)
                .limit(3)
                .collect(Collectors.toList());

        String term = termList.toString()
                .replaceAll(", ", "|")
                .replaceAll("[\\[\\]]","")
                .replaceAll(" ", "~");

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
                .as("Result list contains image item that doesn't contain searched tags")
                .allSatisfy(imageItemDTO -> assertThat(imageItemDTO.getTags()).containsAnyElementsOf(termList));
    }

    // not always find everything, but probably there's something wrong with implementation
    // when executing the same parameters in swagger, then sometimes finds, sometimes returns empty body
    @ParameterizedTest
    @MethodSource("goodParametersForSearchAdvanced")
    public void canSearchAdvancedImageItemByTerm(String queryParam) {
        List<ImageItemDTO> imageItemsList = getImageItemsList();

        assertThat(imageItemsList)
                .as("ImageItem list is empty, can't check if can find ImageItem")
                .isNotEmpty();

        List<ImageItemDTO> resultList = given().spec(defaultRequestSpec())
                .queryParam(queryParam)
                .when()
                .log().all()
                .get(getTestEnvironment().getImageitemSearchAdvancedPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().body().jsonPath().getList(".", ImageItemDTO.class);

        assertThat(resultList)
                .as("ImageItem search result list shouldn't be empty")
                .isNotEmpty();
    }

    @ParameterizedTest()
    @MethodSource("goodParametersForPost")
    @Tag("needs-cleanup")
    public void canPostNewImageItem(String pathToImage, String title, String author, int rating, String description, List<String> tags, Set<Long> categoryIds, Double price, String message){
        logger.info(message);

        ImageItemDTO newImageItem = new ImageItemDTO();
        newImageItem.setPathToImage(pathToImage);
        newImageItem.setTitle(title);
        newImageItem.setAuthor(author);
        newImageItem.setRating(rating);
        newImageItem.setDescription(description);
        newImageItem.setTags(tags);
        newImageItem.setCategoryIds(categoryIds);
        newImageItem.setPrice(price);

        ImageItemDTO createdImageItem = given().spec(defaultRequestSpec())
                .body(newImageItem)
                .log().all()
                .when()
                .post(getTestEnvironment().getImageitemPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(ImageItemDTO.class);

        Response responseGet = getResponseFromGetImageItemById(createdImageItem.getId());
        idImageItemsToClean.add(createdImageItem.getId());

        assertThat(createdImageItem).usingRecursiveComparison()
                .ignoringFields(idQueryParam)
                .isEqualTo(newImageItem);

        assertThat(responseGet.getStatusCode()).isEqualTo(200);

    }

    @ParameterizedTest()
    @MethodSource("wrongParametersForPost")
    public void canNotPostNewImageItemWithWrongParameters(String pathToImage, String title, String author, int rating, String description, List<String> tags, Set<Long> categoryIds, Double price, String message) {
        logger.info(message);

        ImageItemDTO newImageItem = new ImageItemDTO();
        newImageItem.setPathToImage(pathToImage);
        newImageItem.setTitle(title);
        newImageItem.setAuthor(author);
        newImageItem.setRating(rating);
        newImageItem.setDescription(description);
        newImageItem.setTags(tags);
        newImageItem.setCategoryIds(categoryIds);
        newImageItem.setPrice(price);

        Response response = given().spec(defaultRequestSpec())
                .body(newImageItem)
                .log().all()
                .when()
                .post(getTestEnvironment().getImageitemPath())
                .then()
                .log().all()
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Test
    @Tag("needs-cleanup")
    public void canUpdateExistingImageItem(){
        ImageItemDTO newImageItem = new ImageItemDTO();
        newImageItem.setPathToImage("path");
        newImageItem.setTitle("title");
        newImageItem.setAuthor("author");
        newImageItem.setRating(0);
        newImageItem.setDescription("description");
        newImageItem.setTags(new ArrayList<>());
        newImageItem.setCategoryIds(new HashSet<>());
        newImageItem.setPrice(1.0);

        ImageItemDTO createdImageItem = given().spec(defaultRequestSpec())
                .body(newImageItem)
                .log().all()
                .when()
                .post(getTestEnvironment().getImageitemPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(ImageItemDTO.class);

        long imageItemId = createdImageItem.getId();
        idImageItemsToClean.add(imageItemId);
        String newDescription = "New Description";

        newImageItem.setDescription(newDescription);
        newImageItem.setId(imageItemId);

        Response updatedImageItemResponse = given().spec(defaultRequestSpec())
                .body(newImageItem)
                .log().all()
                .when()
                .put(getTestEnvironment().getImageitemPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        ImageItemDTO updatedImageItem = getImageItemById(imageItemId);

        assertThat(updatedImageItemResponse.getStatusCode()).isEqualTo(200);
        assertThat(updatedImageItem.getDescription()).as("Image item was not updated")
                .isEqualTo(newDescription);
    }

    @Test
    public void canNotUpdateNotExistingImageItem() {
        final long id = 0;

        ImageItemDTO newImageItem = new ImageItemDTO();
        newImageItem.setPathToImage("path");
        newImageItem.setTitle("title");
        newImageItem.setAuthor("author");
        newImageItem.setRating(0);
        newImageItem.setDescription("description");
        newImageItem.setTags(new ArrayList<>());
        newImageItem.setCategoryIds(new HashSet<>());
        newImageItem.setPrice(1.0);
        newImageItem.setId(id);

        Response updatedImageItemResponse = given().spec(defaultRequestSpec())
                .body(newImageItem)
                .log().all()
                .when()
                .put(getTestEnvironment().getImageitemPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        assertThat(updatedImageItemResponse.getStatusCode()).isEqualTo(404);
    }

    @Test
    @Tag("needs-cleanup")
    public void canLinkImageItemToCategory(){
        ImageItemDTO newImageItem = new ImageItemDTO();
        newImageItem.setPathToImage("path");
        newImageItem.setTitle("title");
        newImageItem.setAuthor("author");
        newImageItem.setRating(0);
        newImageItem.setDescription("description");
        newImageItem.setTags(new ArrayList<>());
        newImageItem.setCategoryIds(new HashSet<>());
        newImageItem.setPrice(1.0);

        ImageItemDTO createdImageItem = given().spec(defaultRequestSpec())
                .body(newImageItem)
                .log().all()
                .when()
                .post(getTestEnvironment().getImageitemPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(ImageItemDTO.class);

        long imageItemId = createdImageItem.getId();
        long categoryId = getIdExistingCategory();
        idImageItemsToClean.add(imageItemId);

        JSONObject body = new JSONObject();
        body.put("categoryId", categoryId);
        body.put("itemId", imageItemId);

        Response response = given().spec(defaultRequestSpec())
                .body(body)
                .log().all()
                .when()
                .put(getTestEnvironment().getImageitemLinkPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(getImageItemById(imageItemId).getCategoryIds()).as("Don't link image item to category")
                .contains(categoryId);
    }

    @Test
    @Tag("needs-cleanup")
    public void canUnLinkImageItemFromCategory(){
        final long categoryId = getIdExistingCategory();
        Set<Long> categoryIds = new HashSet<>();
        categoryIds.add(categoryId);

        ImageItemDTO newImageItem = new ImageItemDTO();
        newImageItem.setPathToImage("path");
        newImageItem.setTitle("title");
        newImageItem.setAuthor("author");
        newImageItem.setRating(0);
        newImageItem.setDescription("description");
        newImageItem.setTags(new ArrayList<>());
        newImageItem.setCategoryIds(categoryIds);
        newImageItem.setPrice(1.0);

        ImageItemDTO createdImageItem = given().spec(defaultRequestSpec())
                .body(newImageItem)
                .log().all()
                .when()
                .post(getTestEnvironment().getImageitemPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(ImageItemDTO.class);

        long imageItemId = createdImageItem.getId();
        idImageItemsToClean.add(imageItemId);

        JSONObject body = new JSONObject();
        body.put("categoryId", categoryId);
        body.put("itemId", imageItemId);

        Response response = given().spec(defaultRequestSpec())
                .body(body)
                .log().all()
                .when()
                .put(getTestEnvironment().getImageitemUnlinkPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(200);

        assertThat(getImageItemById(imageItemId).getCategoryIds()).as("Don't unlink image item from category")
                .doesNotContain(categoryId);
    }

    @Test
    public void canDeleteExistingImageItem(){
        ImageItemDTO newImageItem = new ImageItemDTO();
        newImageItem.setPathToImage("path");
        newImageItem.setTitle("title");
        newImageItem.setAuthor("author");
        newImageItem.setRating(0);
        newImageItem.setDescription("description");
        newImageItem.setTags(new ArrayList<>());
        newImageItem.setCategoryIds(new HashSet<>());
        newImageItem.setPrice(1.0);

        ImageItemDTO createdImageItem = given().spec(defaultRequestSpec())
                .body(newImageItem)
                .log().all()
                .when()
                .post(getTestEnvironment().getImageitemPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(ImageItemDTO.class);

        long id = createdImageItem.getId();

        Response responseDelete = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .when()
                .log().all()
                .delete(getTestEnvironment().getImageitemPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        Response responseGetDeleted = getResponseFromGetImageItemById(id);

        assertThat(responseDelete.getStatusCode()).isEqualTo(200);

        assertThat(responseGetDeleted.getStatusCode())
                .as("ImageItem should be deleted")
                .isEqualTo(404);
    }

    @Test
    public void canNotDeleteNonExistingImageItem(){
        int id = 0;

        Response response = getResponseFromGetImageItemById(id);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    /**
     * Methods returns id existing category
     *
     * @return long id
     */
    private long getIdExistingCategory() {
        List<CategoryDTO> categoriesList = given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getCategoriesPath())
                .then()
                .spec(defaultResponseSpec())
                .extract().jsonPath().getList(".", CategoryDTO.class);

        return categoriesList.get(new Random().nextInt(categoriesList.size())).getId();
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

    private static Stream<Arguments> goodParametersForPost() {
        Set<Long> categoryIds = new HashSet<>();
        categoryIds.add(12L);
        List<String> tags = new ArrayList<>();
        tags.add("tag");
        return Stream.of(
                Arguments.of(RandomStringUtils.randomAlphabetic(1), "Title", "Author", 1, "Description", tags, categoryIds, 1.0, "Case with min value of pathToImage"),
                Arguments.of(RandomStringUtils.randomAlphabetic(4096), "Title", "Author", 1, "Description", tags, categoryIds, 1.0, "Case with max value of pathToImage"),
                Arguments.of("path", RandomStringUtils.randomAlphabetic(1), "Author", 1, "Description", tags, categoryIds, 1.0, "Case with min value of title"),
                Arguments.of("path", RandomStringUtils.randomAlphabetic(729), "Author", 1, "Description", tags, categoryIds, 1.0, "Case with max value of title"),
                Arguments.of("path", "Title", RandomStringUtils.randomAlphabetic(1), 1, "Description", tags, categoryIds, 1.0, "Case with min value of author"),
                Arguments.of("path", "Title", RandomStringUtils.randomAlphabetic(500), 1, "Description", tags, categoryIds, 1.0, "Case with big value of author"),
                Arguments.of("path", "Title", "Author", 0, "Description", tags, categoryIds, 1.0, "Case with min value of rating"),
                Arguments.of("path", "Title", "Author", 5, "Description", tags, categoryIds, 1.0, "Case with max value of rating"),
                Arguments.of("path", "Title", "Author", 1, "", tags, categoryIds, 1.0, "Case with empty description"),
                Arguments.of("path", "Title", "Author", 1, RandomStringUtils.randomAlphabetic(4096), tags, categoryIds, 1.0, "Case with max value of description"),
                Arguments.of("path", "Title", "Author", 1, "Description", new ArrayList<>(), categoryIds, 1.0, "Case with empty tags"),
                Arguments.of("path", "Title", "Author", 1, "Description", tags, new HashSet<>(), 1.0, "Case with empty categoryIds"),
                Arguments.of("path", "Title", "Author", 1, "Description", tags, categoryIds, Double.MAX_VALUE, "Case with max value of price")
        );
    }

    private static Stream<Arguments> wrongParametersForPost() {
        Set<Long> categoryIds = new HashSet<>();
        List<String> tags = new ArrayList<>();
        return Stream.of(
                Arguments.of("", "Title", "Author", 1, "Description", tags, categoryIds, 1.0, "Case with empty pathToImage"),
                Arguments.of(RandomStringUtils.randomAlphabetic(4100), "Title", "Author", 1, "Description", tags, categoryIds, 1.0, "Case with too long pathToImage"),
                Arguments.of("path", "", "Author", 1, "Description", tags, categoryIds, 1.0, "Case with empty title"),
                Arguments.of("path", RandomStringUtils.randomAlphabetic(800), "Author", 1, "Description", tags, categoryIds, 1.0, "Case with too long title"),
                Arguments.of("path", "Title", "", 1, "Description", tags, categoryIds, 1.0, "Case with empty author"),
                Arguments.of("path", "Title", RandomStringUtils.randomAlphabetic(500000), 1, "Description", tags, categoryIds, 1.0, "Case with really big value of author"),
                Arguments.of("path", "Title", "Author", -5, "Description", tags, categoryIds, 1.0, "Case with negative rating"),
                Arguments.of("path", "Title", "Author", 6, "Description", tags, categoryIds, 1.0, "Case with too big rating"),
                Arguments.of("path", "Title", "Author", 1, RandomStringUtils.randomAlphabetic(4100), tags, categoryIds, 1.0, "Case with too long description"),
                Arguments.of("path", "Title", "Author", 1, "Description", tags, categoryIds, -10.3, "Case with negative price")
        );
    }
}
