package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AddressesCardsListPage extends BasePage {

  @FindBy(css = "body")
  private WebElement body;

  public WebElement getFirstFromList(boolean isAddressPage) {
    String selector = String.format(
      "#%s > tbody > tr:first-child",
      isAddressPage ? "tblAddresses" : "tblCards"
    );
    return body.findElement(By.cssSelector(selector));
  }

  public WebElement getSecondFromList(boolean isAddressPage) {
    String selector = String.format(
      "#%s > tbody > tr:nth-child(2)",
      isAddressPage ? "tblAddresses" : "tblCards"
    );
    return body.findElement(By.cssSelector(selector));
  }

  public WebElement getLastAddress(boolean isAddressPage) {
    String selector = String.format(
      "#%s > tbody > tr:last-child",
      isAddressPage ? "tblAddresses" : "tblCards"
    );
    return body.findElement(By.cssSelector(selector));
  }

  public List<WebElement> getAllAddressesHyperlinksList(boolean isAddressPage) {
    String selector = String.format(
      "#%s > tbody > tr > td:nth-child(2) > a",
      isAddressPage ? "tblAddresses" : "tblCards"
    );
    return body.findElements(By.cssSelector(selector));
  }

  public void clickAtSecondAddressHyperlink(boolean isAddressPage) {
    //    String selector = String.format("#%s > tbody:nth-child(2) > td:nth-child(2) > a",  isAddressPage ? "tblAddresses" : "tblCards");
    //    body.findElement(By.cssSelector(selector)).click();
    getAllAddressesHyperlinksList(isAddressPage).get(1).click();
  }
}
