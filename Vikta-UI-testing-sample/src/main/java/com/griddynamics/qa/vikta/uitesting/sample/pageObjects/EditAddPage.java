package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public abstract class EditAddPage {

    @FindBy(id = "aBack")
    protected WebElement aBack;

    @FindBy(id = "btnSave")
    protected WebElement btnSave;

    @FindAll({ @FindBy(id = "btnDelete"), @FindBy(id = "btnReset") })
    private WebElement btnDeleteOrReset;

    @FindBy(css = ".content > div > form > h2")
    private WebElement actionHeader;

    @FindBy(id = "divErrorsAndMessages")
    protected WebElement divErrorsAndMessages;

    public void clickSaveButton(){
        btnSave.click();
    }

    public void clickToTheListOfItems(){
        aBack.click();
    }

    public void clickDeleteOrResetButton(){
        btnDeleteOrReset.click();
    }

    public void typeIn(WebElement targetElement, String value) {
        targetElement.clear();
        targetElement.sendKeys(value);
    }

    public String getActionHeader() {
        return actionHeader.getText();
    }

    public boolean tableWithErrorsIsDisplayed() {
        return divErrorsAndMessages.isDisplayed();
    }
}
