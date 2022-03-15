package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

public class CardEditAddPage extends EditAddPage{

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

  @FindAll({ @FindBy(id = "btnDelete2"), @FindBy(id = "btnReset") })
  private WebElement btnDeleteOrReset;

  public void typeInCardNumber(String value) {
    typeIn(tbNumber, value);
  }

  public void typeInCardCode(String value) {typeIn(tbCode, value);}

  public void typeInOwner(String value) {
    typeIn(tbOwner, value);
  }

  public void typeInExpirationDate(String value) {
    typeIn(tbexpirationDate, value);
  }

  public void typeInNickname(String value) {
    typeIn(tbNickname, value);
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
