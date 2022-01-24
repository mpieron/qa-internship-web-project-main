package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * "Parent" Page Object for almost all of the rest of the pages (except Login).
 *
 * More to read:
 * https://selenium.dev/documentation/en/guidelines_and_recommendations/page_object_models/
 * https://martinfowler.com/bliki/PageObject.html
 * https://www.baeldung.com/selenium-webdriver-page-object
 * https://www.pluralsight.com/guides/getting-started-with-page-object-pattern-for-your-selenium-tests
 *
 */
public class BasePage {

  //TODO: Add more header/ footer elements.

  @FindBy(id = "sploggedInName")
  private WebElement sploggedInName;

  @FindBy(id = "aLogoutTop")
  private WebElement aLogoutTop;

  public String getCurrentUserName() {
    return sploggedInName.getText();
  }

  public WebElement getLoggedInName() {
    return sploggedInName;
  }

  public void clickLogout() {
    aLogoutTop.click();
  }
}
