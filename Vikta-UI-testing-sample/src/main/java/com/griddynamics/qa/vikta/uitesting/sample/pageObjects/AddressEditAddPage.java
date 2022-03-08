package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class AddressEditAddPage {

    @FindBy(id = "aBack")
    public WebElement aBack;

    @FindBy(id = "tbStreet")
    public WebElement tbStreet;

    @FindBy(id = "tbStreetAdditional")
    public WebElement tbStreetAdditional;

    @FindBy(id = "tbCityName")
    public WebElement tbCityName;

    @FindBy(id = "tbRegionName")
    public WebElement tbRegionName;

    @FindBy(id = "tbPostalCode")
    public WebElement tbPostalCode;

    @FindBy(id = "tbAddressNickname")
    public WebElement tbAddressNickname;

    @FindBy(id = "btnSave")
    public WebElement btnSave;

    @FindAll({
            @FindBy(id = "btnDelete"),
            @FindBy(id = "btnReset")
    })
    public WebElement btnDeleteOrReset;

    public void typeInStreet(String value) {
        typeIn(value, tbStreet);
    }

    public void typeInStreetAdditional(String value) {
        typeIn(value, tbStreetAdditional);
    }

    public void typeInCityName(String value) {
        typeIn(value, tbCityName);
    }

    public void typeInRegionName(String value) {
        typeIn(value, tbRegionName);
    }

    public void typeInPostalCode(String value) {
        typeIn(value, tbPostalCode);
    }

    public void typeInAddressNickname(String value) {
        typeIn(value, tbAddressNickname);
    }

    private void typeIn(String value, WebElement targetElement) {
        targetElement.clear();
        targetElement.sendKeys(value);
    }

    public List<WebElement> getAllFields() {
        List<WebElement> allFields = new ArrayList<>();
        allFields.add(tbStreet);
        allFields.add(tbStreetAdditional);
        allFields.add(tbCityName);
        allFields.add(tbRegionName);
        allFields.add(tbPostalCode);
        allFields.add(tbAddressNickname);
        return allFields;
    }

    public void clickToTheListOfAddresses() {
        aBack.click();
    }

    public void clickSaveButton() {
        btnSave.click();
    }

    public void clickDeleteOrResetButton() {
        btnDeleteOrReset.click();
    }
}
