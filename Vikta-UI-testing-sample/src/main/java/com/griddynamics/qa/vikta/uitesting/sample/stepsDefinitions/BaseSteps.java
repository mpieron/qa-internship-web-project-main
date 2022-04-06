package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestDataConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestSetupConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.BasePage;
import io.qameta.allure.Step;
import java.util.Objects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Base class to contain common auxiliary methods for step definitions.
 */
@Component
public abstract class BaseSteps {

  @Autowired
  protected TestSetupConfiguration properties;
  @Autowired
  protected TestDataConfiguration testData;
  @Autowired
  protected DriverManager driverManager;
  private WebDriverWait wait;
  @Autowired
  private BasePage basePage;

  protected WebDriver getDriver(){
    return driverManager.get();
  }

  WebDriverWait getWait() {
    if (Objects.isNull(this.wait)) {
      this.wait = new WebDriverWait(getDriver(), properties.getWaitTimeout());
    }
    return wait;
  }

  @Step
  public void scroll(WebElement targetElement) {
    Actions scroll = new Actions(getDriver());
    scroll.moveToElement(targetElement);
    scroll.perform();
  }

  @Step
  void verifyCurrentPageIsHomePageForTheUser(String username, UserType userType) {
    getWait().until(ExpectedConditions.visibilityOf(basePage.getLoggedInName()));

    assertCurrentPageUrl(properties.getBaseUrl(), "Home page was expected to be the current one.");

    assertThat(basePage.getCurrentUserName())
        .as("Unexpected current user's name displayed. Expected: %s", username)
        .contains(username);

    assertThat(basePage.getLoggedRole()).as("Assigned wrong role").contains(userType.toString());
  }

  @Step
  void assertCurrentPageUrl(String expectedUrl, String messageOnFail) {
    assertThat(getDriver().getCurrentUrl()).as(messageOnFail).contains(expectedUrl);
  }

  public enum UserType {
    USER,
    ADMIN,
  }
}
