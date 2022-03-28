package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * "Parent" Page Object for almost all of the rest of the pages (except Login).
 *
 * More to read:
 * https://selenium.dev/documentation/en/guidelines_and_recommendations/page_object_models/
 * https://martinfowler.com/bliki/PageObject.html
 * https://www.baeldung.com/selenium-webdriver-page-object
 * https://www.pluralsight.com/guides/getting-started-with-page-object-pattern-for-your-selenium-tests
 *
 */
public class BasePage {

  @FindBy(id = "aHome")
  private WebElement aHome;

  @FindBy(id = "aAddresses")
  private WebElement aAddresses;

  @FindBy(id = "aAddAddress")
  private WebElement aAddAddress;

  @FindBy(id = "aCards")
  private WebElement aCards;

  @FindBy(id = "aAddCard")
  private WebElement aAddCard;

  @FindBy(id = "aEditProfile")
  private WebElement aEditProfile;

  @FindBy(id = "aToCartTop")
  private WebElement aToCartTop;

  @FindBy(className = "navbar")
  private List<WebElement> rightNavbar;

  @FindBy(id = "spCartTopMsg")
  private WebElement spCartTopMsg;

  @FindBy(id = "sploggedInName")
  private WebElement sploggedInName;

  @FindBy(id = "aLogoutTop")
  private WebElement aLogoutTop;

  @FindBy(id = "aLogoutBottom")
  private WebElement aLogoutBottom;

  public String getCurrentUserName() {
    return sploggedInName.getText();
  }

  public WebElement getLoggedInName() {
    return sploggedInName;
  }

  public String getLoggedRole() {
    return sploggedInName.findElements(new By.ByXPath("../span[2]")).get(0).getText();
  }

  public void clickLogoutTopBottom() {
    aLogoutTop.click();
  }

  public void clickHomeBottom() {
    aHome.click();
  }

  public void clickAddressesBottom() {
    aAddresses.click();
  }

  public void clickAddAddressBottom() {
    aAddAddress.click();
  }

  public void clickCardsBottom() {
    aCards.click();
  }

  public void clickAddCardBottom() {
    aAddCard.click();
  }

  public void clickEditProfileBottom() {
    aEditProfile.click();
  }

  public void clickLogoutFooterBottom() {
    aLogoutBottom.click();
  }

  public void goToShoppingCart(){ aToCartTop.click();}

  public boolean shoppingCartIconIsDisplayed(){
    return rightNavbar.size() == 2; }

  public String getNumberOfItemsInShoppingCart(){
    return spCartTopMsg.getText();
  }

  public WebElement getShoppingCart(){
    return aToCartTop;
  }
}
