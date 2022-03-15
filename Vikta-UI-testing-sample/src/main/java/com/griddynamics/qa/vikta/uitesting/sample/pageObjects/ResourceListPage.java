package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public abstract class ResourceListPage {

  @FindBy(css = "body")
  protected WebElement body;

  @FindBy(id = "tbTerm")
  protected WebElement tbTerm;

  @FindBy(id = "btnSearch")
  protected WebElement btnSearch;

  protected final String firstFromList = "> tbody > tr:first-child";

  protected final String secondFromList = "> tbody > tr:nth-child(2)";

  protected final String lastFromList = "> tbody > tr:last-child";

  protected String getAllHyperlinksFromColumn(int numberOfColumn) {
    return String.format("> tbody > tr > td:nth-child(%d) > a", numberOfColumn);
  }

  public void typeSearchTerm(String term){
    tbTerm.clear();
    tbTerm.sendKeys(term);
  }

  public void clickSearchButton(){
    btnSearch.click();
  }
}