package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AddressesListPage extends ResourceListPage {

  private final String addressTableId = "#tblAddresses";

  public WebElement getFirstAddressFromList() {
    String selector = String.format("%s %s", addressTableId, getFirstFromList());
    return body.findElement(By.cssSelector(selector));
  }

  public WebElement getSecondAddressFromList() {
    String selector = String.format("%s %s", addressTableId, getSecondFromList());
    return body.findElement(By.cssSelector(selector));
  }

  public WebElement getLastAddressFromList() {
    String selector = String.format("%s %s", addressTableId, getLastFromList());
    return body.findElement(By.cssSelector(selector));
  }

  public List<WebElement> getAllAddressesHyperlinksList() {
    String selector = String.format("%s %s", addressTableId, getAllHyperlinksList());
    return body.findElements(By.cssSelector(selector));
  }

  public void clickAtSecondAddressHyperlink() {
    getAllAddressesHyperlinksList().get(1).click();
  }
}
