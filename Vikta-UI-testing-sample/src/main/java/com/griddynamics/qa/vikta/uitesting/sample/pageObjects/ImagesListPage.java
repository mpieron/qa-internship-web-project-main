package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ImagesListPage extends ResourceListPage {

  private final String imageTableId = "#tblImgs";
  private final int hyperlinkColumnNumber = 3;

  public WebElement getLastImageFromList() {
    String selector = String.format("%s %s", imageTableId, lastFromList);
    return body.findElement(By.cssSelector(selector));
  }

  public List<WebElement> getAllImagesHyperlinksList() {
    String selector = String.format(
      "%s %s",
      imageTableId,
      getAllHyperlinksFromColumn(hyperlinkColumnNumber)
    );
    return body.findElements(By.cssSelector(selector));
  }

  public void removeLastImage() {
    String selector = String.format("%s > tbody > tr > td:last-child > a", imageTableId);
    int size = body.findElements(By.cssSelector(selector)).size() - 1;
    body.findElements(By.cssSelector(selector)).get(size).click();
  }
}
