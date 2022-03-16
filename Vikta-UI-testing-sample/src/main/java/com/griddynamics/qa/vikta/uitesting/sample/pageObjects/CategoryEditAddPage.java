package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CategoryEditAddPage extends EditAddPage {

  @FindBy(id = "tbTitle")
  private WebElement tbTitle;

  @FindBy(id = "tbPathToCatImage")
  private WebElement tbPathToCatImage;

  public void typeTitle(String value) {
    typeIn(tbTitle, value);
  }

  public void typePathToCatImage(String value) {
    typeIn(tbPathToCatImage, value);
  }
}
