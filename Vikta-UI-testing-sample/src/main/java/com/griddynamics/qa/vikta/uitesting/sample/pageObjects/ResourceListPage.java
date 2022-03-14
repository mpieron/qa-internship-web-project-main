package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class ResourceListPage {

  @FindBy(css = "body")
  protected WebElement body;

  protected final String firstFromList = "> tbody > tr:first-child";

  protected final String secondFromList = "> tbody > tr:nth-child(2)";

  protected final String lastFromList = "> tbody > tr:last-child";

  protected String getAllHyperlinksList(int numberOfColumn) {
    return String.format("> tbody > tr > td:nth-child(%d) > a", numberOfColumn);
  }
}
