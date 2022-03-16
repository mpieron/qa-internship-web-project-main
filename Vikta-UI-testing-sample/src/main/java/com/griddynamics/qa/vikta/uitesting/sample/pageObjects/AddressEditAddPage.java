package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AddressEditAddPage extends EditAddPage{

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

  public void typeInStreet(String value) {
    typeIn(tbStreet, value);
  }

  public void typeInStreetAdditional(String value) {
    typeIn(tbStreetAdditional, value);
  }

  public void typeInCityName(String value) {
    typeIn(tbCityName, value);
  }

  public void typeInRegionName(String value) {
    typeIn(tbRegionName, value);
  }

  public void typeInPostalCode(String value) {
    typeIn(tbPostalCode, value);
  }

  public void typeInAddressNickname(String value) {
    typeIn(tbAddressNickname, value);
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
}
