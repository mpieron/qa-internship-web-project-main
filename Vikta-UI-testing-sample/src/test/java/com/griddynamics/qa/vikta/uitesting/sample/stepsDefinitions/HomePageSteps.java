package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.HomePage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

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
        returnValue = page().getExistingImageTitle();
        page().writeTerm(page().getExistingImageTitle());
        break;
      case TAG:
        returnValue = page().getExistingImageTag();
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
        returnValue = utilities.generatePrice();
        page().writePriceFrom(returnValue);
        break;
      case PRICETO:
        returnValue = utilities.generatePrice();
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
  public void verifyFoundImages(String term){
    assertThat(page().getImagesOnCurrentPage().size()).isGreaterThan(0);

//    to improve - checking if founded images contain term (title/tag)
//    assertThat(page().getImagesOnCurrentPage())
//            .allSatisfy(image -> assertThat(image.getText()).contains(term));
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
