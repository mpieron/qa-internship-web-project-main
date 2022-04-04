package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.config.TestDataConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestSetupConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.BasePage;
import io.qameta.allure.Step;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Base class to contain common auxiliary methods for step definitions.
 */
@Component
@RequiredArgsConstructor
public abstract class BaseSteps {

  protected final TestSetupConfiguration properties;
  protected final TestDataConfiguration testData;
  @Autowired
  protected final WebDriver driver;
  private WebDriverWait wait;

  WebDriverWait getWait() {
    if (Objects.isNull(this.wait)) {
      this.wait = new WebDriverWait(driver, properties.getWaitTimeout());
    }
    return wait;
  }

  <P> P getPage(Class<P> pageClass) {
    return PageFactory.initElements(driver, pageClass);
  }

  @Step
  public void scroll(WebElement targetElement) {
    Actions scroll = new Actions(driver);
    scroll.moveToElement(targetElement);
    scroll.perform();
  }

  @Step
  void verifyCurrentPageIsHomePageForTheUser(String username, UserType userType) {
    BasePage currentPage = getPage(BasePage.class);
    getWait().until(ExpectedConditions.visibilityOf(currentPage.getLoggedInName()));

    assertCurrentPageUrl(properties.getBaseUrl(), "Home page was expected to be the current one.");

    assertThat(currentPage.getCurrentUserName())
        .as("Unexpected current user's name displayed. Expected: %s", username)
        .contains(username);

    assertThat(currentPage.getLoggedRole()).as("Assigned wrong role").contains(userType.toString());
  }

  @Step
  void assertCurrentPageUrl(String expectedUrl, String messageOnFail) {
    assertThat(driver.getCurrentUrl()).as(messageOnFail).contains(expectedUrl);
  }

  public enum UserType {
    USER,
    ADMIN,
  }
}
