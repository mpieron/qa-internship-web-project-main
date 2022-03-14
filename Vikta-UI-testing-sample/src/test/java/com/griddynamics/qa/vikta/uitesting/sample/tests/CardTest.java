package com.griddynamics.qa.vikta.uitesting.sample.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CardTest extends BaseTest {

  @BeforeMethod
  public void login() {
    loginSteps.openLoginPage();
    loginSteps.loginAsRegularUser();
  }

  @BeforeMethod(onlyForGroups = "needCard")
  public void loginAndGenerateNewAddress() {
    cardSteps.clickAddCardTab();
    cardSteps.fillAllFieldsInCard();
    cardSteps.clickSaveButton();
  }

  @AfterMethod(onlyForGroups = "needCleanUp")
  public void clean() {
    cardSteps.clickAtCardTab();
    cardSteps.deleteAddedCards();
  }

  @Test
  public void checkThatFirstCardFieldsAreCorrect() {
    //        Click at Card tab
    cardSteps.clickAtCardTab();

    //        Verify default address is correct
    cardSteps.verifyFirstCardFieldsAreCorrect();
  }

  @Test(groups = { "needCard", "needCleanUp" })
  public void canEditCard() {
    //        Click at Cards tab
    cardSteps.clickAtCardTab();

    //        Click at second card hyperlink
    cardSteps.clickAtSecondCardHyperlink();

    //        Change all fields
    String cardInfo = cardSteps.fillAllFieldsInCard();

    //        Save changes
    cardSteps.clickSaveButton();

    //        Go to cards list
    cardSteps.clickToTheListOfCards();

    //        Check if card was changed
    cardSteps.verifyIfChangedAllFieldsCorrectly(cardInfo);
  }

  @Test(groups = "needCleanUp")
  public void canAddCard() {
    //        Click at Add Card tab
    cardSteps.clickAddCardTab();

    //        Fill all fields
    String cardInfo = cardSteps.fillAllFieldsInCard();

    //        Save new card
    cardSteps.clickSaveButton();

    //        Go to cards list
    cardSteps.clickToTheListOfCards();

    //        Check if card was added correctly
    cardSteps.verifyIfAddedCardCorrectly(cardInfo);
  }

  @Test(groups = "needCard")
  public void canDeleteCard() {
    //        Click at Card tab
    cardSteps.clickAtCardTab();

    //        Click at second card hyperlink
    cardSteps.clickAtSecondCardHyperlink();

    //        Delete second card
    cardSteps.clickDeleteOrResetButton();

    //        Go to cards list
    cardSteps.clickToTheListOfCards();

    //        Check if card was deleted
    cardSteps.verifyIfCardWasDeleted();
  }

  @Test
  public void canClearFieldsWhileAddingNewCard() {
    //        Click at Add Address tab
    cardSteps.clickAddCardTab();

    //        Fill all fields
    cardSteps.fillAllFieldsInCard();

    //        Click clear button
    cardSteps.clickDeleteOrResetButton();

    //        Check if all fields are empty
    cardSteps.verifyIfAllFieldsOnAddPageAreEmpty();
  }
}
