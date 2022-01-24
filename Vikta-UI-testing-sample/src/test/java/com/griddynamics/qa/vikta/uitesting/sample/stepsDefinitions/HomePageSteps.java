package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.HomePage;
import org.openqa.selenium.WebDriver;

/**
 * Home page related step Definitions
 */
public class HomePageSteps extends BaseSteps {

  public HomePageSteps(WebDriver driver) {
    super(driver);
  }

  //TODO: Add more steps.

  //TODO: Think about generics etc instead of this.
  private HomePage page() {
    return getPage(HomePage.class);
  }
}
