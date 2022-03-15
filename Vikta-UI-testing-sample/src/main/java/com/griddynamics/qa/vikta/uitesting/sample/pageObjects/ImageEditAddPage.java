package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ImageEditAddPage extends EditAddPage {

  @FindBy(id = "tbUEL")
  private WebElement tbUEL;

  @FindBy(id = "tbTitle")
  private WebElement tbTitle;

  public void typeURL(String value) {
    typeIn(tbUEL, value);
  }

  public void typeTitle(String value) {
    typeIn(tbTitle, value);
  }
}
