package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UsersListPage extends ResourceListPage {

  private final String userTableId = "#tblUsrs";
  private final int hyperlinkColumnNumber = 2;

  @FindBy(id = "trUser_qq")
  public WebElement trUser_qq;

  @FindBy(id = "trUser_admin")
  public WebElement trUser_admin;

  public WebElement getLastUserFromList() {
    String selector = String.format("%s %s", userTableId, lastFromList);
    return body.findElement(By.cssSelector(selector));
  }

  public List<WebElement> getAllUsersHyperlinksList() {
    String selector = String.format(
      "%s %s",
      userTableId,
      getAllHyperlinksFromColumn(hyperlinkColumnNumber)
    );
    return body.findElements(By.cssSelector(selector));
  }

  public void removeLastUser() {
    String selector = String.format("%s > tbody > tr > td:last-child > a", userTableId);
    int size = body.findElements(By.cssSelector(selector)).size() - 1;
    body.findElements(By.cssSelector(selector)).get(size).click();
  }

  public WebElement getUser_qq() {
    return trUser_qq;
  }

  public WebElement getUser_admin() {
    return trUser_admin;
  }
}
