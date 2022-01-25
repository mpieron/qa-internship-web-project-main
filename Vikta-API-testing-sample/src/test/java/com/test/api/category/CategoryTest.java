package com.test.api.category;

import com.test.api.dto.CategoryDTO;
import com.test.api.execution.BaseTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test for Category API
 */
public class CategoryTest extends BaseTest {

    /**
     * Test ob category title verification
     */
    @Test
    public void verifyGetCategory() {

        final int categoryId = 5;
        final String expectedTitle = "Street";

        assertThat("Category title is unexpected",
                getExistingCategoryById(categoryId).getTitle(),
                equalTo(expectedTitle));
    }

    /**
     * Test with wrong id given
     */
    @Test
    public void verifyGetCategoryWithUnexpectedId() {
        final  int categoryId = 0;

        Response response = getResponseFromCategoryById(categoryId);

        assertThat("Category id is correct, excepted wrong", response.getStatusCode(), equalTo(404));
        assertThat(response.getBody().asString(), containsString("No such Category entity"));
    }

    /**
     * Test with wrong description given
     */
    @Test
    public void verifyGetCategoryWithUnexpectedDescription() {
        final int categoryId = 5;
        final String expectedDescription = "Hammm";
        String desc = getExistingCategoryById(categoryId).getDescription();

        assertThat(desc).isNotEqualTo(expectedDescription);
    }

    /**
     * Test with category verification in list of categories
     */
    @Test
    public void checkCategoryInAllCategoriesList() {
        final int categoryId = 7;
        final CategoryDTO expectedCategory = getExistingCategoryById(categoryId);

        CategoryDTO[] categories = given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getCategoriesPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .as(CategoryDTO[].class);

        assertThat("Category wasn't found in all categories list",
                Arrays.asList(categories),
                hasItem(equalTo(expectedCategory)));
    }

    /**
     *  Test with entity search
     */
    // city is empty list
    @Test
    public void checkIfCanFindEntity(){
        final String termQueryParam = "term";
        final String term = "Wolf%20N8%20%2F%20Dynamic%20Marketing%20Officer";

        List<CategoryDTO> city;
        city = given().spec(defaultRequestSpec())
                .queryParam(termQueryParam, term)
                .when()
                .log().all()
                .get(getTestEnvironment().getCategoryPath() + "/search")
                .then()
                .log().all()
                .spec(defaultResponseSpec())
                .extract()
                .body().jsonPath().getList(".", CategoryDTO.class);

        assertThat(city).isNotEmpty();
    }

    /**
     *  Test with posting new category
     */
    @Test
    public void tryPostNewCategory(){
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

        Response responseGet = getResponseFromCategoryById((int)createdCategory.getId());

        assertThat(createdCategory).usingRecursiveComparison()
                .ignoringFields("id", "imageItemIds")
                .isEqualTo(newCategory);
        assertThat(responseGet.getStatusCode()).isEqualTo(200);
    }

    /**
     *
     * Test deleting category
     */
    @Test
    public void checkIfCanDeleteCategory(){
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
                .queryParam("id", createdCategory.getId())
                .log().all()
                .when()
                .delete(getTestEnvironment().getCategoryPath())
                .then()
                .log().all()
                .extract().response();

        Response responseGetDeleted = getResponseFromCategoryById((int)createdCategory.getId());

        assertThat(responseDelete.getStatusCode()).isEqualTo(200);
        assertThat(responseGetDeleted.getStatusCode()).isEqualTo(404);
    }

    /**
     * Test check deleting non-existing Category
     */
    @Test
    public void verifyDeletingNonExistingCategory(){
        int id = 0;
        String idQueryParam = "id";

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

    /**
     *  Test updating category
     */
    @Test
    public void verifyUpdatingExistingCategory(){
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

        id = (int)createdCategory.getId();
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

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(getExistingCategoryById(id).getTitle())
                .isEqualTo(newTitle);
    }

    /**
     * Method returns Category and expects that category exist
     *
     * @param id category identifier
     * @return Category object
     */
    private CategoryDTO getExistingCategoryById(int id) {
        final String idQueryParam = "id";

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
        final String idQueryParam = "id";

        return given().spec(defaultRequestSpec())
                .when().queryParam(idQueryParam, id)
                .get(getTestEnvironment().getCategoryPath())
                .then()
                .spec(defaultResponseSpec())
                .extract()
                .response();
    }
}
