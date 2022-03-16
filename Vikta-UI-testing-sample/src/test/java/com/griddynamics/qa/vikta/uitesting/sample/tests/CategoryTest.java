package com.griddynamics.qa.vikta.uitesting.sample.tests;

import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.CategorySteps;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CategoryTest extends BaseTest {

  @BeforeMethod(onlyForGroups = "onlyLogin")
  public void loginAsAdmin() {
    loginSteps.openLoginPage();
    loginSteps.loginAsAdmin();
  }

  @BeforeMethod(onlyForGroups = "needNewCategory")
  public void createNewCategory() {
    loginAsAdmin();
    categorySteps.clickAddACategory();

    categorySteps.fillNecessaryCategoryField(CategorySteps.NecessaryCategoryField.TITLE);
    categorySteps.fillNecessaryCategoryField(CategorySteps.NecessaryCategoryField.PATH_TO_CATEGORY_IMAGE);

    categorySteps.clickSaveButton();
  }

  @AfterMethod(onlyForGroups = "cleanUp")
  public void clean() {
    categorySteps.clickCategories();
    categorySteps.deleteAddedCategories();
  }

//    Searching for categories by term doesn't work for any category field
  @Test(groups = "onlyLogin")
  public void canFindCategoryByTitle() {
//        Click Categories
    categorySteps.clickCategories();

//        Get first category title
    String title = categorySteps.getFirstCategoryTitle();

//        Search category by title
    categorySteps.searchCategoryByTitle(title);

//        Verify if found category
    categorySteps.verifyIfFoundCategoryByTitle();
  }

  @Test(groups = {"onlyLogin", "cleanUp"})
  public void canAddCategory() {
//        Click Add a Category
    categorySteps.clickAddACategory();

//        Fill fields
    String title = categorySteps.fillNecessaryCategoryField(CategorySteps.NecessaryCategoryField.TITLE);
    String path = categorySteps.fillNecessaryCategoryField(CategorySteps.NecessaryCategoryField.PATH_TO_CATEGORY_IMAGE);

//        Save category
    categorySteps.clickSaveButton();

//        Go to the list of categories
    categorySteps.clickToTheListOfCategoriesItems();

//        Verify if added category
    categorySteps.verifyLastCategoryTitleAndPath(title, path);
  }

  @Test(groups = { "needNewCategory", "cleanUp" })
  public void canEditCategory() {
//        Click Categories
    categorySteps.clickCategories();

//        Click last category
    categorySteps.clickLastCategory();

//        Change category title and path
    String title = categorySteps.fillNecessaryCategoryField(CategorySteps.NecessaryCategoryField.TITLE);
    String path = categorySteps.fillNecessaryCategoryField(CategorySteps.NecessaryCategoryField.PATH_TO_CATEGORY_IMAGE);

//        Save changes
    categorySteps.clickSaveButton();

//        Go to the list of categories
    categorySteps.clickToTheListOfCategoriesItems();

//        Verify if category was changed
    categorySteps.verifyLastCategoryTitleAndPath(title, path);
  }

  @Test(groups = "needNewCategory")
  public void canDeleteCategory() {
//        Click Categories
    categorySteps.clickCategories();

//        Delete last category
    categorySteps.deleteAddedCategories();

//        Verify if category was deleted
    categorySteps.verifyIfCategoryWasDeleted();
  }
}
