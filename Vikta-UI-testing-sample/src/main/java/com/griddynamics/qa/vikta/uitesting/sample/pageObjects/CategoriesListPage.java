package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CategoriesListPage extends ResourceListPage {

  private final String categoryTableId = "#tblCats";
  private final int hyperlinkColumnNumber = 3;

  public WebElement getFirstCategoryFromList() {
    String selector = String.format("%s %s", categoryTableId, firstFromList);
    return body.findElement(By.cssSelector(selector));
  }

  public WebElement getSecondCategoryFromList() {
    String selector = String.format("%s %s", categoryTableId, secondFromList);
    return body.findElement(By.cssSelector(selector));
  }

  public WebElement getLastCategoryFromList() {
    String selector = String.format("%s %s", categoryTableId, lastFromList);
    return body.findElement(By.cssSelector(selector));
  }

  public List<WebElement> getAllCategoriesHyperlinksList() {
    String selector = String.format("%s %s", categoryTableId, getAllHyperlinksFromColumn(hyperlinkColumnNumber));
    return body.findElements(By.cssSelector(selector));
  }

  public void removeLastCategory() {
    String selector = String.format("%s > tbody > tr > td:last-child > a", categoryTableId);
    int size = body.findElements(By.cssSelector(selector)).size() - 1;
    body.findElements(By.cssSelector(selector)).get(size).click();
  }
}
