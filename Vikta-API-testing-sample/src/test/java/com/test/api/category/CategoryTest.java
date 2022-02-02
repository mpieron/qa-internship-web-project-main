package com.test.api.category;

import com.test.api.dto.CategoryDTO;
import com.test.api.execution.BaseTest;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class CategoryTest extends BaseTest {

    private final List<Integer> idCategoryToClean = new ArrayList<>();
    private final String idQueryParam = "id";
    private static final Logger logger = LogManager.getLogger(CategoryTest.class);

    @AfterEach
    void cleanup(TestInfo testInfo) {
        if (testInfo.getTags().contains("needs-cleanup")) {
            for(int id : idCategoryToClean) {
                given().spec(defaultRequestSpec())
                        .queryParam(idQueryParam, id)
                        .when()
                        .delete(getTestEnvironment().getCategoryPath());
            }
        }
    }


    @Test
    public void canGetCategoryByCorrectId() {
        final int categoryId = 5;
        final String expectedTitle = "Street";

        assertThat(getExistingCategoryById(categoryId).getTitle())
                .as(String.format("Category title should be %s, but is %s",getExistingCategoryById(categoryId).getTitle(), expectedTitle))
                .isEqualTo(expectedTitle);
    }

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -20, (long) 0, Long.MAX_VALUE})
    public void canNotGetCategoryWithUnexpectedId(long id) {
        Response response = getResponseFromCategoryById((int)id);

        assertThat(response.getStatusCode()).isEqualTo(404);
        assertThat(response.getBody().asString())
                .withFailMessage("There shouldn't be such a category entity")
                .contains("No such Category entity");
    }

    @Test
    public void canListAllCategories(){
        CategoryDTO[] categories = given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getCategoriesPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .as(CategoryDTO[].class);

        assertThat(categories).as("Category list shouldn't be empty").isNotEmpty();
        assertThat(categories).as("Category don't have title field").allMatch(category -> !category.getTitle().isEmpty());
        assertThat(categories).as("Category don't have description field").allMatch(category -> !category.getDescription().isEmpty());
        assertThat(categories).as("Category don't have pathToCatImage field").allMatch(category -> !category.getPathToCatImage().isEmpty());
    }

    @Test
    public void canFindEntity(){
        final String termQueryParam = "term";
        Random random = new Random();

        List <CategoryDTO> listOfCategories = given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getCategoriesPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", CategoryDTO.class);

        assertThat(listOfCategories.size()).isGreaterThan(0);

        int size = random.nextInt(listOfCategories.size()) + 2;

        String term =  listOfCategories.stream()
                .map(CategoryDTO::getTitle)
                .limit(size)
                .map(category  -> category.replaceAll(" ", "~"))
                .collect(Collectors.joining("|"));

        List<CategoryDTO> resultList;
        resultList = given().spec(defaultRequestSpec())
                .queryParam(termQueryParam, term)
                .when()
                .log().all()
                .get(getTestEnvironment().getCategoriesSearchPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", CategoryDTO.class);

        assertThat(listOfCategories)
                .as("Category list is empty, can't check if can find entity")
                .isNotEmpty();
        assertThat(resultList)
                .as("Category search result list shouldn't be empty")
                .isNotEmpty();
        assertThat(resultList.size())
                .as(String.format("Should find %s, but find only %s Categories", size, resultList.size()))
                .isEqualTo(size);
    }

    @ParameterizedTest()
    @MethodSource("goodParametersForPost")
    @Tag("needs-cleanup")
    public void canPostNewCategory(String title, String description, String pathToCatImage, long imageItemIds, String message){
        logger.info(message);
        HashSet<Long> set = new HashSet<>();
        set.add(imageItemIds);

        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setTitle(title);
        newCategory.setDescription(description);
        newCategory.setPathToCatImage(pathToCatImage);
        newCategory.setImageItemIds(set);

        CategoryDTO createdCategory = given().spec(defaultRequestSpec())
                .body(newCategory)
                .log().all()
                .when()
                .post(getTestEnvironment().getCategoryPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(CategoryDTO.class);
        Response responseGet = getResponseFromCategoryById(createdCategory.getId());
        idCategoryToClean.add(createdCategory.getId());

        assertThat(createdCategory).usingRecursiveComparison()
                .ignoringFields(idQueryParam, "imageItemIds")
                .isEqualTo(newCategory);
        assertThat(responseGet.getStatusCode()).isEqualTo(200);
    }

    @ParameterizedTest()
    @MethodSource("wrongParametersForPost")
    public void canNotPostNewCategoryWithWrongParameters(String title, String description, String pathToCatImage, long imageItemIds, String message){
        logger.info(message);
        HashSet<Long> set = new HashSet<>();
        set.add(imageItemIds);

        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setTitle(title);
        newCategory.setDescription(description);
        newCategory.setPathToCatImage(pathToCatImage);
        newCategory.setImageItemIds(set);

        Response response = given().spec(defaultRequestSpec())
                        .body(newCategory)
                        .log().all()
                        .when()
                        .post(getTestEnvironment().getCategoryPath())
                        .then()
                        .log().all()
                        .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(500);
    }

    @Test
    public void canDeleteCategory(){
        Set<Long> set = new HashSet<>();
        set.add(1000L);

        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setDescription("This is new category");
        newCategory.setTitle("New Category");
        newCategory.setPathToCatImage("new");
        newCategory.setImageItemIds(set);

        CategoryDTO createdCategory = given().spec(defaultRequestSpec())
                .body(newCategory)
                .log().all()
                .when()
                .post(getTestEnvironment().getCategoryPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(CategoryDTO.class);

        Response responseDelete = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, createdCategory.getId())
                .log().all()
                .when()
                .delete(getTestEnvironment().getCategoryPath())
                .then()
                .log().all()
                .extract().response();

        Response responseGetDeleted = getResponseFromCategoryById(createdCategory.getId());

        assertThat(responseDelete.getStatusCode()).isEqualTo(200);
        assertThat(responseGetDeleted.getStatusCode())
                .as("Category should be deleted")
                .isEqualTo(404);
    }

    @Test
    public void canNotDeleteNonExistingCategory(){
        int id = 0;

        Response response = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .log().all()
                .when()
                .delete(getTestEnvironment().getCategoryPath())
                .then()
                .log().all()
                .extract().response();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    @Tag("needs-cleanup")
    public void canUpdateExistingCategory(){
        Set<Long> set = new HashSet<>();
        set.add(1000L);
        String newTitle = "Updated Category";

        CategoryDTO category = new CategoryDTO();
        category.setDescription("This is new category");
        category.setTitle("New Category");
        category.setPathToCatImage("new");
        category.setImageItemIds(set);

        CategoryDTO createdCategory = given().spec(defaultRequestSpec())
                .body(category)
                .log().all()
                .when()
                .post(getTestEnvironment().getCategoryPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().as(CategoryDTO.class);

        int id = createdCategory.getId();
        category.setTitle(newTitle);
        category.setId(id);

        Response response = given().spec(defaultRequestSpec())
                .body(category)
                .log().all()
                .when()
                .put(getTestEnvironment().getCategoryPath())
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract().response();

        idCategoryToClean.add(id);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(getExistingCategoryById(id).getTitle())
                .as(String.format("Title %s wasn't changed to %s", getExistingCategoryById(id).getTitle(), newTitle))
                .isEqualTo(newTitle);
    }

    /**
     * Method returns Category and expects that category exist
     *
     * @param id category identifier
     * @return Category object
     */
    private CategoryDTO getExistingCategoryById(int id) {

        return given().spec(defaultRequestSpec())
                .when().queryParam(idQueryParam, id)
                .get(getTestEnvironment().getCategoryPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .as(CategoryDTO.class);
    }

    /**
     * Method returns Response and expects that category exist
     *
     * @param id category identifier
     * @return Response object
     */
    private Response getResponseFromCategoryById(int id) {

        return given().spec(defaultRequestSpec())
                .when().queryParam(idQueryParam, id)
                .get(getTestEnvironment().getCategoryPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .response();
    }

    private static Stream<Arguments> goodParametersForPost() {
        return Stream.of(
                Arguments.of(RandomStringUtils.random(1),"This is new category", "new", 1000L, "Case with min value of title length"),
                Arguments.of(RandomStringUtils.random(729),"This is new category", "new", 1000L, "Case with max value of title length"),
                Arguments.of("New Category", RandomStringUtils.random(4096), "new", 1000L, "Case with max value of description length"),
                Arguments.of("New Category", RandomStringUtils.random(1000), "new", 1000L, "Case with correct value of description length"),
                Arguments.of("New Category", "This is new category", RandomStringUtils.random(1000), 1000L, "Case with correct value of pathToCatImage length"),
                Arguments.of("New Category", "This is new category", RandomStringUtils.random(4096), 1000L, "Case with max value of pathToCatImage length"),
                Arguments.of("New Category", "This is new category", "new", 0, "Case with all correct")
        );
    }

    private static Stream<Arguments> wrongParametersForPost() {
        return Stream.of(
                Arguments.of("", "This is new category", "new", 1000L, "Case with empty title"),
                Arguments.of(RandomStringUtils.random(730),"This is new category", "new", 1000L, "Case with too long title"),
                Arguments.of(RandomStringUtils.random(1000),"This is new category", "new", 1000L, "Case with too much long title"),
                Arguments.of("New Category",RandomStringUtils.random(4097), "new", 1000L, "Case with too long description"),
                Arguments.of("New Category",RandomStringUtils.random(5000), "new", 1000L, "Case with too much long description"),
                Arguments.of("New Category", "This is new category", "", 1000L, "Case with empty pathToCatImage"),
                Arguments.of("New Category","This is new category", RandomStringUtils.random(4097), 1000L, "Case with too long pathToCatImage"),
                Arguments.of("New Category","This is new category", RandomStringUtils.random(5000), 1000L, "Case with too much pathToCatImage")
        );
    }
}
