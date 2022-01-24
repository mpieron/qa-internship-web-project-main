package com.test.api.category;

import com.test.api.dto.CategoryDTO;
import com.test.api.execution.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
     * Test with wrong description given
     */
    @Test
    public void verifyGetCategoryWithUnexpectedDescription() {
        final int categoryId = 5;
        final String expectedDescription = "Hammm";

        assertThat("Category description is unexpected",
                getExistingCategoryById(categoryId).getDescription(),
                equalTo(expectedDescription));
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
}
