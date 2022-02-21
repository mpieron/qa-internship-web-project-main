package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.HomePage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Home page related step Definitions
 */
public class HomePageSteps extends BaseSteps {

  public HomePageSteps(WebDriver driver) {
    super(driver);
  }

  public enum FieldName {
    TITLE,
    TAG,
    RATINGFROM,
    RATINGTO,
    PRICEFROM,
    PRICETO
  }

  @Step
  public void openHomePage() {
    getDriver().get(getData().baseUrl());
  }

  @Step
  public String typeValueInto(HomePageSteps.FieldName fieldName) {
    Utilities utilities = new Utilities();
    String returnValue;
    switch (fieldName) {
      case TITLE:
        List<WebElement> productList = page().getImagesTitlesFromCurrentPage();
        returnValue = productList.get(new Random().nextInt(productList.size())).getText();
        page().writeTerm(returnValue);
        break;
      case TAG:
        returnValue = page().getExistingImageTags();
        page().writeTerm(returnValue);
        break;
      case RATINGFROM:
        returnValue = utilities.generateRating();
        page().writeRatingFrom(returnValue);
        break;
      case RATINGTO:
        returnValue = utilities.generateRating();
        page().writeRatingTo(returnValue);
        break;
      case PRICEFROM:
        returnValue = utilities.generatePriceFrom();
        page().writePriceFrom(returnValue);
        break;
      case PRICETO:
        returnValue = utilities.generatePriceTo();
        page().writePriceTo(returnValue);
        break;
      default:
        throw new IllegalArgumentException(
                "Unsupported search record name: " + fieldName
        );
    }
    return returnValue;
  }

  @Step
  public void verifyImagesFoundByTitle(String title){
    HomePage currentPage = getPage(HomePage.class);
    getWait().until(ExpectedConditions.visibilityOf(currentPage.getSelectedCategoryTitle()));

    assertThat(page().getImagesTitlesFromCurrentPage().size()).isGreaterThan(0);

    assertThat(page().getImagesTitlesFromCurrentPage())
            .as("Found image item that doesn't contain searched title")
            .allSatisfy(image -> assertThat(image.getText()).contains(title));
  }

//  when we search for photos by tag, we also consider those that contain given word in the tag, which is a sentence or two words combined with "/".
//  Haven't found a way to compare correctly yet
  @Step
  public void verifyImagesFoundByTags(String tag){
    HomePage currentPage = getPage(HomePage.class);
    getWait().until(ExpectedConditions.visibilityOf(currentPage.getSelectedCategoryTitle()));

    String cleanTag = tag.replaceAll("\\+", " ");

    assertThat(page().getImagesTagsFromCurrentPage().size()).isGreaterThan(0);

    assertThat(page().getImagesTagsFromCurrentPage().entrySet())
            .as("Found image item that doesn't contain searched tags")
            .allSatisfy(image -> assertThat(image.getValue()).contains(cleanTag));
  }

  @Step
  public void verifyImagesFoundByPriceFrom(String price){
    HomePage currentPage = getPage(HomePage.class);
    getWait().until(ExpectedConditions.visibilityOf(currentPage.getSelectedCategoryTitle()));

    assertThat(page().getImagePricesFromCurrentPage())
            .as("Found image item that's price is lower that search")
            .allSatisfy(imagePrice -> assertThat(Double.parseDouble(imagePrice)).isGreaterThanOrEqualTo(Double.parseDouble(price)));
  }

  @Step
  public void verifyImagesFoundByPriceTo(String price){
    HomePage currentPage = getPage(HomePage.class);
    getWait().until(ExpectedConditions.visibilityOf(currentPage.getSelectedCategoryTitle()));

    assertThat(page().getImagePricesFromCurrentPage())
            .as("Found image item that's price is greater that search")
            .allSatisfy(imagePrice -> assertThat(Double.parseDouble(imagePrice)).isLessThanOrEqualTo(Double.parseDouble(price)));
  }

  @Step
  public void clickSearchBottom(){
    page().clickSearchBottom();
  }

  @Step
  public void clickResetBottom(){
    page().clickResetBottom();
  }

  private HomePage page() {
    return getPage(HomePage.class);
  }
}
