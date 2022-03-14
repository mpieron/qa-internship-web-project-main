package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CardsListPage extends ResourceListPage {

  private final String cardsTableId = "#tblCards";
  private final int hyperlinkColumnNumber = 2;

  public WebElement getFirstCardFromList() {
    String selector = String.format("%s %s", cardsTableId, firstFromList);
    return body.findElement(By.cssSelector(selector));
  }

  public WebElement getSecondACardFromList() {
    String selector = String.format("%s %s", cardsTableId, secondFromList);
    return body.findElement(By.cssSelector(selector));
  }

  public WebElement getLastCardFromList() {
    String selector = String.format("%s %s", cardsTableId, lastFromList);
    return body.findElement(By.cssSelector(selector));
  }

  public List<WebElement> getAllCardsHyperlinksList() {
    String selector = String.format("%s %s", cardsTableId, getAllHyperlinksList(hyperlinkColumnNumber));
    return body.findElements(By.cssSelector(selector));
  }

  public void clickAtSecondCardHyperlink() {
    getAllCardsHyperlinksList().get(1).click();
  }
}
