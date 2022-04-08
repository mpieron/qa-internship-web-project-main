package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.HomePage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.GenericWebActions;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Home page related step Definitions
 */
public class HomePageSteps extends GenericWebActions {

  @Autowired
  private HomePage homePage;
  @Autowired
  private DriverManager driverManager;

  protected WebDriver getDriver(){
    return driverManager.get();
  }


  @Step
  public void openHomePage() {
    getDriver().get(properties.getBaseUrl());
  }

  @Step
  public String typeValueInto(HomePageSteps.FieldName fieldName) {
    String returnValue;
    switch (fieldName) {
      case TITLE:
        List<WebElement> productList = homePage.getImagesTitlesFromCurrentPage();
        returnValue = productList.get(new Random().nextInt(productList.size())).getText();
        homePage.writeTerm(returnValue);
        break;
      case TAG:
        returnValue = homePage.getExistingImageTags();
        homePage.writeTerm(returnValue);
        break;
      case RATINGFROM:
        returnValue = Utilities.generateRating();
        homePage.writeRatingFrom(returnValue);
        break;
      case RATINGTO:
        returnValue = Utilities.generateRating();
        homePage.writeRatingTo(returnValue);
        break;
      case PRICEFROM:
        returnValue = Utilities.generatePriceFrom();
        homePage.writePriceFrom(returnValue);
        break;
      case PRICETO:
        returnValue = Utilities.generatePriceTo();
        homePage.writePriceTo(returnValue);
        break;
      default:
        throw new IllegalArgumentException("Unsupported search record name: " + fieldName);
    }
    return returnValue;
  }

  @Step
  public void verifyImagesFoundByTitle(String title) {
    getWait().until(ExpectedConditions.visibilityOf(homePage.getSelectedCategoryTitle()));

    assertThat(homePage.getImagesTitlesFromCurrentPage().size()).isGreaterThan(0);

    assertThat(homePage.getImagesTitlesFromCurrentPage())
        .as("Found image item that doesn't contain searched title")
        .allSatisfy(image -> assertThat(image.getText()).contains(title));
  }

  @Step
  public void verifyImagesFoundByTags() {
    getWait().until(ExpectedConditions.visibilityOf(homePage.getSelectedCategoryTitle()));

    assertThat(homePage.getImagesTitlesFromCurrentPage().size()).isGreaterThan(0);
  }

  @Step
  public void verifyImagesFoundByPriceFrom(String price) {
    getWait().until(ExpectedConditions.visibilityOf(homePage.getSelectedCategoryTitle()));

    assertThat(homePage.getImagePricesFromCurrentPage())
        .as("Found image item that's price is lower that search")
        .allSatisfy(
            imagePrice ->
                assertThat(Double.parseDouble(imagePrice))
                    .isGreaterThanOrEqualTo(Double.parseDouble(price))
        );
  }

  @Step
  public void verifyImagesFoundByPriceTo(String price) {
    getWait().until(ExpectedConditions.visibilityOf(homePage.getSelectedCategoryTitle()));

    assertThat(homePage.getImagePricesFromCurrentPage())
        .as("Found image item that's price is greater that search")
        .allSatisfy(
            imagePrice ->
                assertThat(Double.parseDouble(imagePrice)).isLessThanOrEqualTo(
                    Double.parseDouble(price))
        );
  }

  @Step
  public void verifySearchedCategory(String categoryName) {
    getWait().until(ExpectedConditions.visibilityOf(homePage.getSelectedCategoryTitle()));

    assertThat(homePage.getSelectedCategoryTitle().getText())
        .as("Wrong category found")
        .contains(categoryName);
  }

  @Step
  public String clickAndReturnCategory() {
    return homePage.clickAndReturnCategory();
  }

  @Step
  public void clickSearchBottom() {
    homePage.clickSearchBottom();
  }

  public enum FieldName {
    TITLE,
    TAG,
    RATINGFROM,
    RATINGTO,
    PRICEFROM,
    PRICETO,
  }
}
