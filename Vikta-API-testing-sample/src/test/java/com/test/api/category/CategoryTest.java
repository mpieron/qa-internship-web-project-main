package com.test.api.category;

import com.test.api.config.TestEnvironment;
import com.test.api.dto.CategoryDTO;
import com.test.api.execution.BaseTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
                .spec(defaultResponseJsonSpec())
                .extract()
                .as(CategoryDTO[].class);

        assertThat("Category wasn't found in all categories list",
                Arrays.asList(categories),
                hasItem(equalTo(expectedCategory)));
    }



    /**
     *  Test with entity search
     */
    // it's weird, when extract.as(CategoryDTO[].class) return empty array, at http://localhost:5054/swagger-ui.html it works
    // when extract.response the status code equals 200
    @Test
    public void checkIfCanFindEntity(){
        final String termQueryParam = "term";
        final String term = "Wolf%20N8%20%2F%20Dynamic%20Marketing%20Officer";

        CategoryDTO[] city = given().spec(defaultRequestSpec())
                .queryParam(termQueryParam, term)
                .when()
                .log().all()
                .get(getTestEnvironment().getCategoryPath() + "/search")
                .then()
                .log().all()
                .spec(defaultResponseJsonSpec())
                .extract()
                .as(CategoryDTO[].class);

        assertThat(city[0].getTitle()).isEqualTo("City");
    }

    /**
     *  Test with posting new category
     */
    // Intellij returns status code = 500
    // "Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Error while committing the transaction"
    // When using queryParam instead of CategoryDTO objects, returns code = 400 - same in postman

    @Test
    public void tryPostNewCategory(){
        CategoryDTO newCategory = new CategoryDTO();
        newCategory.setDescription("This is new category");
        newCategory.setTitle("New Category");

        Response response = getResponseFromPostNewCategory(newCategory);

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    /**
     *
     * Test deleting category
     */
    // there should be created new category, posted and then deleted, but post is not working (previous test, code = 400)
    @Test
    public void checkIfCanDeleteCategory(){
        int id = 100;
        String idQueryParam = "id";

        Response responseDelete = given().spec(defaultRequestSpec())
                .queryParam(idQueryParam, id)
                .log().all()
                .when()
                .delete(getTestEnvironment().getCategoryPath())
                .then()
                .log().all()
                .extract().response();

        Response responseGetDeleted = getResponseFromCategoryById(id);

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
                .spec(defaultResponseJsonSpec())
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
                .spec(defaultResponseJsonSpec())
                .extract()
                .response();
    }

    /**
     * Method accepts Category amd returns Response
     *
     * @param newCategory category to create
     * @return Response object
     */
    private Response getResponseFromPostNewCategory(CategoryDTO newCategory){

        return given().spec(defaultRequestSpec())
                .body(newCategory)
                .when()
                .log().all()
                .post(getTestEnvironment().getCategoryPath())
                .then()
                .log().all()
                .spec(defaultResponseTxtSpec())
                .extract()
                .response();
    }

    // only for own use, to remove later
    private void printAllCategories(){
        CategoryDTO[] categories = given().spec(defaultRequestSpec())
                .when()
                .get(getTestEnvironment().getCategoriesPath())
                .then()
                .spec(defaultResponseJsonSpec())
                .extract()
                .as(CategoryDTO[].class);

        for(CategoryDTO cat : categories)
            System.out.println(cat);
    }
}
