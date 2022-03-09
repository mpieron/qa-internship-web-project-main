package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AddressesListPage extends BasePage {

  @FindBy(css = "#tblAddresses > tbody > tr:first-child > td:nth-child(2) > a")
  private WebElement firstAddressHyperlink;

  @FindBy(css = "#tblAddresses > tbody > tr:nth-child(2) > td:nth-child(2) > a")
  private WebElement secondAddressHyperlink;

  @FindBy(css = "#tblAddresses > tbody > tr:first-child")
  private WebElement firstAddress;

  @FindBy(css = "#tblAddresses > tbody > tr:nth-child(2)")
  private WebElement secondAddress;

  @FindBy(css = "#tblAddresses > tbody > tr:last-child")
  private WebElement lastAddress;

  @FindBy(css = "#tblAddresses > tbody > tr > td:nth-child(2) > a")
  private List<WebElement> allAddressesHyperlinksList;

  public WebElement getFirstAddress() {
    return firstAddress;
  }

  public WebElement getSecondAddress() {
    return secondAddress;
  }

  public WebElement getLastAddress() {
    return lastAddress;
  }

  public List<WebElement> getAllAddressesHyperlinksList() {
    return allAddressesHyperlinksList;
  }

  public void clickAtSecondAddressHyperlink() {
    getAllAddressesHyperlinksList().get(1).click();
  }
}
