package com.griddynamics.qa.vikta.uitesting.sample.tests;

import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.HomePageSteps;
import org.testng.annotations.Test;

/**
 * Feature: Searching images, categories
 * I should be able to search category, image by term/tags/other criteria available on the home page
 */
public class SearchTest extends BaseTest {

  @Test
  public void canFindImageByTitle() {
    // Open Home Page
    homePageSteps.openHomePage();

    // Type title as term
    String title = homePageSteps.typeValueInto(HomePageSteps.FieldName.TITLE);

    //Click search bottom
    homePageSteps.clickSearchBottom();

    // Verify if found any image (should find at least one)
    homePageSteps.verifyImagesFoundByTitle(title);
  }

  @Test
  public void canFindImageByTag() {
    // Open Home Page
    homePageSteps.openHomePage();

    // Type tag as term
    homePageSteps.typeValueInto(HomePageSteps.FieldName.TAG);

    //Click search bottom
    homePageSteps.clickSearchBottom();

    // Verify if found any image (should find at least one)
    homePageSteps.verifyImagesFoundByTags();
  }

  //  not always finds, depends on vikta
  @Test
  public void canFindImageByPriceFrom() {
    // Open Home Page
    homePageSteps.openHomePage();

    // Type min price to search criteria
    String price = homePageSteps.typeValueInto(HomePageSteps.FieldName.PRICEFROM);

    //Click search bottom
    homePageSteps.clickSearchBottom();

    // Verify if founded images has good price
    homePageSteps.verifyImagesFoundByPriceFrom(price);
  }

  //  not always finds, depends on vikta
  @Test
  public void canFindImageByPriceTo() {
    // Open Home Page
    homePageSteps.openHomePage();

    // Type max price to search criteria
    String price = homePageSteps.typeValueInto(HomePageSteps.FieldName.PRICETO);

    //Click search bottom
    homePageSteps.clickSearchBottom();

    // Verify if founded images has good price
    homePageSteps.verifyImagesFoundByPriceTo(price);
  }

  @Test
  public void canSearchCategory() {
    // Open Home Page
    homePageSteps.openHomePage();

    // Click category
    String categoryName = homePageSteps.clickAndReturnCategory();

    // Verify if founded good category
    homePageSteps.verifySearchedCategory(categoryName);
  }
}
