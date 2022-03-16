package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.*;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CategorySteps extends BaseSteps {

  public CategorySteps(WebDriver driver) {
    super(driver);
  }

  public enum NecessaryCategoryField {
    TITLE,
    PATH_TO_CATEGORY_IMAGE,
  }

  @Step
  public String fillNecessaryCategoryField(CategorySteps.NecessaryCategoryField categoryField) {
    String valueToReturn;
    switch (categoryField) {
      case TITLE:
        valueToReturn = Utilities.generateTitle();
        categoryEditAddPage().typeTitle(valueToReturn);
        break;
      case PATH_TO_CATEGORY_IMAGE:
        valueToReturn = Utilities.generateURL();
        categoryEditAddPage().typePathToCatImage(valueToReturn);
        break;
      default:
        throw new IllegalArgumentException("Unsupported Address Add/Edit page field name: " + categoryField);
    }
    return valueToReturn;
  }

  @Step
  public void clickCategories() {
    adminBasePage().clickCategories();
  }

  @Step
  public void clickAddACategory() {
    adminBasePage().clickAddCategory();
  }

  @Step
  public String getFirstCategoryTitle() {
    return categoryListPage().getAllCategoriesHyperlinksList().get(0).getText();
  }

  @Step
  public void clickSearchButton() {
    categoryListPage().clickSearchButton();
  }

  @Step
  public void clickSaveButton() {
    categoryEditAddPage().clickSaveButton();
  }

  @Step
  public void clickToTheListOfCategoriesItems() {
    adminBasePage().clickCategories();
  }

  @Step
  public void clickLastCategory() {
    scroll(categoryListPage().getLastCategoryFromList());
    List<WebElement> categoriesList = categoryListPage().getAllCategoriesHyperlinksList();

    categoriesList.get(categoriesList.size() - 1).click();
  }

  @Step
  public void searchCategoryByTitle(String title) {
    categoryListPage().typeSearchTerm(title);
    clickSearchButton();
  }

  @Step
  public void deleteAddedCategories() {
    scroll(categoryListPage().getLastCategoryFromList());
    categoryListPage().removeLastCategory();
  }

  @Step
  public void verifyIfFoundCategoryByTitle() {
    assertThat(categoryListPage().getAllCategoriesHyperlinksList().size())
      .as("Category not found by title")
      .isGreaterThan(0);
  }

  @Step
  public void verifyLastCategoryTitleAndPath(String title, String path) {
    scroll(categoryListPage().getLastCategoryFromList());
    String lastCategory = categoryListPage().getLastCategoryFromList().getText();

    assertThat(lastCategory)
      .as("Category \"%s\" has wrong title, should contains %s", lastCategory, title)
      .contains(title);

    assertThat(lastCategory)
      .as("Category \"%s\" has wrong path should contains %s", lastCategory, path)
      .contains(path);
  }

  @Step
  public void verifyIfCategoryWasDeleted() {
    assertThat(categoryListPage().messageDeleteIsDisplayed())
      .as("Deletion message should be displayed")
      .isTrue();
  }

  CategoriesListPage categoryListPage() {
    return getPage(CategoriesListPage.class);
  }

  CategoryEditAddPage categoryEditAddPage() {
    return getPage(CategoryEditAddPage.class);
  }

  AdminBasePage adminBasePage() {
    return getPage(AdminBasePage.class);
  }
}
