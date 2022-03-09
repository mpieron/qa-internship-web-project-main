package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class AddressEditAddPage {

  @FindBy(id = "aBack")
  private WebElement aBack;

  @FindBy(id = "tbStreet")
  private WebElement tbStreet;

  @FindBy(id = "tbStreetAdditional")
  private WebElement tbStreetAdditional;

  @FindBy(id = "tbCityName")
  private WebElement tbCityName;

  @FindBy(id = "tbRegionName")
  private WebElement tbRegionName;

  @FindBy(id = "tbPostalCode")
  private WebElement tbPostalCode;

  @FindBy(id = "tbAddressNickname")
  private WebElement tbAddressNickname;

  @FindBy(id = "btnSave")
  private WebElement btnSave;

  @FindAll({ @FindBy(id = "btnDelete"), @FindBy(id = "btnReset") })
  private WebElement btnDeleteOrReset;

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
