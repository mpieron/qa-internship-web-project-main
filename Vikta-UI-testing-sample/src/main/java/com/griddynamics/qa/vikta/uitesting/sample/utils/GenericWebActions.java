package com.griddynamics.qa.vikta.uitesting.sample.utils;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestSetupConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.BasePage;
import io.qameta.allure.Step;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@RequiredArgsConstructor
public class GenericWebActions {

    @Autowired
    private TestSetupConfiguration properties;
    @Autowired
    private DriverManager driverManager;
    private WebDriverWait wait;
    @Autowired
    private BasePage basePage;

    protected WebDriver getDriver(){
        return driverManager.get();
    }

    public WebDriverWait getWait() {
        if (Objects.isNull(this.wait)) {
            this.wait = new WebDriverWait(getDriver(), properties.getWaitTimeout());
        }
        return wait;
    }

    public TestSetupConfiguration getProperties(){
        return properties;
    }

    @Step
    public void scroll(WebElement targetElement) {
        Actions scroll = new Actions(getDriver());
        scroll.moveToElement(targetElement);
        scroll.perform();
    }

    @Step
    public void verifyCurrentPageIsHomePageForTheUser(String username, UserType userType) {
        getWait().until(ExpectedConditions.visibilityOf(basePage.getLoggedInName()));

        assertCurrentPageUrl(properties.getBaseUrl(), "Home page was expected to be the current one.");

        assertThat(basePage.getCurrentUserName())
                .as("Unexpected current user's name displayed. Expected: %s", username)
                .contains(username);

        assertThat(basePage.getLoggedRole()).as("Assigned wrong role").contains(userType.toString());
    }

    @Step
    public void assertCurrentPageUrl(String expectedUrl, String messageOnFail) {
        assertThat(getDriver().getCurrentUrl()).as(messageOnFail).contains(expectedUrl);
    }

    public enum UserType {
        USER,
        ADMIN,
    }
}
