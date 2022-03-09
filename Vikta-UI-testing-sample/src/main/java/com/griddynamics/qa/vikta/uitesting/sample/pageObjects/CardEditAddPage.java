package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class CardEditAddPage {

  @FindBy(id = "aBack")
  private WebElement aBack;

  @FindBy(id = "tbNumber")
  private WebElement tbNumber;

  @FindBy(id = "tbCode")
  private WebElement tbCode;

  @FindBy(id = "tbOwner")
  private WebElement tbOwner;

  @FindBy(id = "tbexpirationDate")
  private WebElement tbexpirationDate;

  @FindBy(id = "tbNickname")
  private WebElement tbNickname;

  @FindBy(id = "btnSave")
  private WebElement btnSave;

  @FindAll({ @FindBy(id = "btnDelete2"), @FindBy(id = "btnReset") })
  private WebElement btnDeleteOrReset;

  public void typeInCardNumber(String value) {
    typeIn(value, tbNumber);
  }

  public void typeInCardCode(String value) {
    typeIn(value, tbCode);
  }

  public void typeInOwner(String value) {
    typeIn(value, tbOwner);
  }

  public void typeInExpirationDate(String value) {
    typeIn(value, tbexpirationDate);
  }

  public void typeInNickname(String value) {
    typeIn(value, tbNickname);
  }

  private void typeIn(String value, WebElement targetElement) {
    targetElement.clear();
    targetElement.sendKeys(value);
  }

  public void clickToTheListOfCards() {
    aBack.click();
  }

  public void clickSaveButton() {
    btnSave.click();
  }

  public void clickDeleteOrResetButton() {
    btnDeleteOrReset.click();
  }

  public List<WebElement> getAllFields() {
    List<WebElement> allFields = new ArrayList<>();
    allFields.add(tbNumber);
    allFields.add(tbCode);
    allFields.add(tbOwner);
    allFields.add(tbexpirationDate);
    allFields.add(tbNickname);
    return allFields;
  }
}
