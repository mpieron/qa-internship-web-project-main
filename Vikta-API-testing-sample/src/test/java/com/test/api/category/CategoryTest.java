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
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryTest extends BaseTest {

    private final List<Integer> idCategoryToClean = new ArrayList<>();
    private final String idQueryParam = "id";

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

        assertThat("Category title should be %s, but is %s",
                getExistingCategoryById(categoryId).getTitle(),
                equalTo(expectedTitle));
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -20, 0, Integer.MAX_VALUE})
    public void canNotGetCategoryWithUnexpectedId(int id) {
        Response response = getResponseFromCategoryById(id);

        assertThat(String.format("Category id: %s is correct, expected wrong" ,id), response.getStatusCode(), equalTo(404));
        assertThat("There shouldn't be such a category" + id, response.getBody().asString(), containsString("No such Category entity"));
    }

    // This test was here, I don't think it makes sense. Maybe checking if  description is correct, but I think it isn't necessary
    @ParameterizedTest
    @ValueSource(strings = {"Hamm", "1", "city"})
    public void canNotGetCategoryWithUnexpectedDescription(String expectedDescription) {
        final int categoryId = 5;
        String desc = getExistingCategoryById(categoryId).getDescription();

        assertThat(desc).isNotEqualTo(expectedDescription);
    }

    @ParameterizedTest
    @ValueSource(ints = {1,5,7})
    public void canGetCategoryInAllCategoriesList(int categoryId) {
        CategoryDTO expectedCategory = getExistingCategoryById(categoryId);

        CategoryDTO[] categories = given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getCategoriesPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .as(CategoryDTO[].class);

        assertThat(String.format("Category %s wasn't found in all categories list", categoryId),
                Arrays.asList(categories),
                hasItem(equalTo(expectedCategory)));
    }

    @Test
    public void canListAllCategories(){
        String pathList = getTestEnvironment().getCategoryPath() + "/list";

        CategoryDTO[] categories = given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getCategoriesPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .as(CategoryDTO[].class);

        CategoryDTO[] listOfCategories = given().spec(defaultRequestSpec())
                .when()
                .get(pathList)
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .as(CategoryDTO[].class);

        assertThat(listOfCategories).isEqualTo(categories);
    }

    @Test
    public void canFindEntity(){
        final String pathList = getTestEnvironment().getCategoryPath() + "/list";
        final String searchPath = getTestEnvironment().getCategoryPath() + "/search";
        final String termQueryParam = "term";
        final int size = 3;
        Random random = new Random();

        List <CategoryDTO> listOfCategories = given().spec(defaultRequestSpec())
                .when()
                .get(pathList)
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", CategoryDTO.class);

        StringBuilder termBuilder = new StringBuilder();
        for(int i=0; i<size; i++){
            termBuilder.append(listOfCategories.get(random.nextInt(listOfCategories.size())).getTitle());
            if(i<size-1)
                termBuilder.append("|");
        }
        String term = termBuilder.toString().replaceAll(" ", "~");

        List<CategoryDTO> city;
        city = given().spec(defaultRequestSpec())
                .queryParam(termQueryParam, term)
                .when()
                .log().all()
                .get(searchPath)
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", CategoryDTO.class);
        System.out.println(term);
        for(CategoryDTO cat : city)
            System.out.println(cat.getTitle());

        assertThat(String.format("Couldn't find all entities. Found only %d", city.size()), city.size() == size);
    }

    @ParameterizedTest(name = "{index} => title={0}, description={1}, pathToCatImage={2}, imageItemIds={3}")
    @MethodSource("goodParametersForPost")
    @Tag("needs-cleanup")
    public void canPostNewCategory(String title, String description, String pathToCatImage, long imageItemIds){
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

    @ParameterizedTest(name = "{index} => title={0}, description={1}, pathToCatImage={2}, imageItemIds={3}")
    @MethodSource("wrongParametersForPost")
    public void canNotPostNewCategoryWithWrongParameters(String title, String description, String pathToCatImage, long imageItemIds){
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
        assertThat(String.format("Category with id %s should be deleted", createdCategory.getId()), responseGetDeleted.getStatusCode() == 404);
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
        int id;

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

        id = createdCategory.getId();
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
        assertThat("Title %s wasn't changed to %s", getExistingCategoryById(id).getTitle().equals(newTitle));
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
                Arguments.of(RandomStringUtils.random(1),"This is new category", "new", 1000L),
                Arguments.of(RandomStringUtils.random(729),"This is new category", "new", 1000L),
                Arguments.of("New Category",RandomStringUtils.random(4096), "new", 1000L),
                Arguments.of("New Category",RandomStringUtils.random(1000), "new", 1000L),
                Arguments.of("New Category","This is new category", RandomStringUtils.random(1000), 1000L),
                Arguments.of("New Category","This is new category", RandomStringUtils.random(4096), 1000L),
                Arguments.of("New Category", "This is new category", "new", 0)
        );
    }

    private static Stream<Arguments> wrongParametersForPost() {
        return Stream.of(
                Arguments.of("", "This is new category", "new", 1000L),
                Arguments.of(RandomStringUtils.random(730),"This is new category", "new", 1000L),
                Arguments.of(RandomStringUtils.random(1000),"This is new category", "new", 1000L),
                Arguments.of("New Category",RandomStringUtils.random(4097), "new", 1000L),
                Arguments.of("New Category",RandomStringUtils.random(5000), "new", 1000L),
                Arguments.of("New Category", "This is new category", "", 1000L),
                Arguments.of("New Category","This is new category", RandomStringUtils.random(4097), 1000L),
                Arguments.of("New Category","This is new category", RandomStringUtils.random(5000), 1000L)
        );
    }
}
