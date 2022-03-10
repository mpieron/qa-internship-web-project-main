package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ResourceListPage {

  @FindBy(css = "body")
  protected WebElement body;

  public String getFirstFromList() {
    return "> tbody > tr:first-child";
  }

  public String getSecondFromList() {
    return "> tbody > tr:nth-child(2)";
  }

  public String getLastFromList() {
    return "> tbody > tr:last-child";
  }

  public String getAllHyperlinksList() {
    return "> tbody > tr > td:nth-child(2) > a";
  }
}
