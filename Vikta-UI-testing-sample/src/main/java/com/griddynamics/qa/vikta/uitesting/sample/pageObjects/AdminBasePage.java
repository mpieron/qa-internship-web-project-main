package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AdminBasePage {

  @FindBy(id = "aImages")
  private WebElement aImages;

  @FindBy(id = "aAddImage")
  private WebElement aAddImage;

  @FindBy(id = "aCategories")
  private WebElement aCategories;

  @FindBy(id = "aAddCategory")
  private WebElement aAddCategory;

  @FindBy(id = "aUsers")
  private WebElement aUsers;

  @FindBy(id = "aAddUser")
  private WebElement aAddUser;

  @FindBy(id = "aUsersStuffMenuHeader")
  private WebElement aUsersStuffMenuHeader;

  @FindBy(id = "aUsrCards")
  private WebElement aUsrCards;

  @FindBy(id = "aUsrAddCard")
  private WebElement aUsrAddCard;

  public void clickImages() {
    aImages.click();
  }

  public void clickAddImage() {
    aAddImage.click();
  }

  public void clickCategories() {
    aCategories.click();
  }

  public void clickAddCategory() {
    aAddCategory.click();
  }

  public void clickUsers() {
    aUsers.click();
  }

  public void clickCreateUser() {
    aAddUser.click();
  }

  public void clickMyCards() {
    aUsersStuffMenuHeader.click();
    aUsrCards.click();
  }

  public void clickAddCard() {
    aUsersStuffMenuHeader.click();
    aUsrAddCard.click();
  }
}
