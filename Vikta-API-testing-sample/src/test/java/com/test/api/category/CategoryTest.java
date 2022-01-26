package com.test.api.category;

import com.test.api.dto.CategoryDTO;
import com.test.api.execution.BaseTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryTest extends BaseTest {

    private int idCategoryToClean = 0;
    private final String idQueryParam = "id";

    @AfterEach
    void cleanup(TestInfo testInfo) {
        if (testInfo.getTags().contains("needs-cleanup")) {
                given().spec(defaultRequestSpec())
                        .queryParam(idQueryParam , idCategoryToClean)
                        .when()
                        .delete(getTestEnvironment().getCategoryPath());
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

    @Test
    public void canNotGetCategoryWithUnexpectedId() {
        final  int categoryId = 0;

        Response response = getResponseFromCategoryById(categoryId);

        assertThat(String.format("Category id: %s is correct, excepted wrong" ,categoryId), response.getStatusCode(), equalTo(404));
        assertThat("There shouldn't be such a category" + categoryId, response.getBody().asString(), containsString("No such Category entity"));
    }

    @Test
    public void canNotGetCategoryWithUnexpectedDescription() {
        final int categoryId = 5;
        final String expectedDescription = "Hammm";
        String desc = getExistingCategoryById(categoryId).getDescription();

        assertThat(desc).isNotEqualTo(expectedDescription);
    }

    @Test
    public void canGetCategoryInAllCategoriesList() {
        final int categoryId = 7;
        final CategoryDTO expectedCategory = getExistingCategoryById(categoryId);

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
    public void canFindEntity(){
        final String termQueryParam = "term";
        final String searchPath = getTestEnvironment().getCategoryPath() + "/search";
        final String term = "City|Tech|service|admin|To4ka";

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

        assertThat("Couldn't find entities: " + term, city.size() > 0);
    }

    @Test
    @Tag("needs-cleanup")
    public void canPostNewCategory(){
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

        Response responseGet = getResponseFromCategoryById(createdCategory.getId());
        idCategoryToClean = createdCategory.getId();

        assertThat(createdCategory).usingRecursiveComparison()
                .ignoringFields(idQueryParam, "imageItemIds")
                .isEqualTo(newCategory);
        assertThat(responseGet.getStatusCode()).isEqualTo(200);
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

        idCategoryToClean = id;

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
}
