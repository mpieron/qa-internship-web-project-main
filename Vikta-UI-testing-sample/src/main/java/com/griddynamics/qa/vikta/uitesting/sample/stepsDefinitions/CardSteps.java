package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.CardEditAddPage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.CardsListPage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.HomePage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import java.util.List;
import java.util.StringJoiner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CardSteps extends BaseSteps {

  public CardSteps(WebDriver driver) {
    super(driver);
  }

  private enum CardField {
    CARD_NUMBER,
    CARD_CODE,
    OWNER,
    EXPIRATION_DATE,
    NICKNAME,
  }

  @Step
  public String typeValueInto(CardSteps.CardField cardField) {
    String valueToReturn;
    switch (cardField) {
      case CARD_NUMBER:
        valueToReturn = Utilities.generateCardNumber();
        cardEditAddPage().typeInCardNumber(valueToReturn);
        break;
      case CARD_CODE:
        valueToReturn = Utilities.generateCardCode();
        cardEditAddPage().typeInCardCode(valueToReturn);
        break;
      case OWNER:
        valueToReturn = Utilities.generateCardOwner();
        cardEditAddPage().typeInOwner(valueToReturn);
        break;
      case EXPIRATION_DATE:
        valueToReturn = Utilities.generateExpirationDate();
        cardEditAddPage().typeInExpirationDate(valueToReturn);
        break;
      case NICKNAME:
        valueToReturn = Utilities.generateNickname();
        cardEditAddPage().typeInNickname(valueToReturn);
        break;
      default:
        throw new IllegalArgumentException(
          "Unsupported Address Add/Edit page field name: " + cardField
        );
    }
    return valueToReturn;
  }

  @Step
  public void clickAtCardTab() {
    homePage().clickCardsBottom();
  }

  @Step
  public void clickAddCardTab() {
    homePage().clickAddCardBottom();
  }

  @Step
  public void clickAtSecondCardHyperlink() {
    cardsListPage().clickAtSecondCardHyperlink();
  }

  @Step
  public void clickSaveButton() {
    cardEditAddPage().clickSaveButton();
  }

  @Step
  public void clickDeleteOrResetButton() {
    cardEditAddPage().clickDeleteOrResetButton();
  }

  @Step
  public void clickToTheListOfCards() {
    cardEditAddPage().clickToTheListOfItems();
  }

  @Step
  public String fillAllFieldsInCard() {
    StringJoiner addressInfo = new StringJoiner(" ");
    addressInfo.add(typeValueInto(CardField.CARD_NUMBER));
    addressInfo.add(typeValueInto(CardField.CARD_CODE));
    addressInfo.add(typeValueInto(CardField.OWNER));
    addressInfo.add(typeValueInto(CardField.EXPIRATION_DATE));
    addressInfo.add(typeValueInto(CardSteps.CardField.NICKNAME));
    return addressInfo.toString();
  }

  @Step
  public void deleteAddedCards() {
    List<WebElement> allCardsHyperlinksList = cardsListPage().getAllCardsHyperlinksList();

    for (int i = 1; i < allCardsHyperlinksList.size(); i++) {
      allCardsHyperlinksList.get(i).click();
      cardEditAddPage().clickDeleteOrResetButton();
      cardEditAddPage().clickToTheListOfItems();
    }
  }

  @Step
  public void verifyFirstCardFieldsAreCorrect() {
    assertThat(cardsListPage().getFirstCardFromList().getText())
      .as("Default card field is not correct.")
      .contains(testData.getCardCode(), testData.getCardTag());
  }

  @Step
  public void verifyIfChangedAllFieldsCorrectly(String cardInfo) {
    String cardOnPage = cardsListPage().getSecondACardFromList().getText().substring(2);
    assertThat(cardOnPage)
      .as("Card wasn't changed correctly. Should be %s, but was %s", cardInfo, cardOnPage)
      .isEqualTo(cardInfo);
  }

  @Step
  public void verifyIfAddedCardCorrectly(String cardInfo) {
    String cardAddress = cardsListPage().getLastCardFromList().getText().substring(2);
    assertThat(cardAddress)
      .as("Card wasn't added correctly. Should be %s, was %s", cardAddress, cardInfo)
      .isEqualTo(cardInfo);
  }

  @Step
  public void verifyIfCardWasDeleted() {
    List<WebElement> allCardsHyperlinksList = cardsListPage().getAllCardsHyperlinksList();

    assertThat(allCardsHyperlinksList.size()).as("Card wasn't deleted").isEqualTo(1);
  }

  @Step
  public void verifyIfAllFieldsOnAddPageAreEmpty() {
    assertThat(cardEditAddPage().getAllFields())
      .as("There is not empty field!")
      .allSatisfy(contents -> assertThat(contents.getText()).isEmpty());
  }

  private HomePage homePage() {
    return getPage(HomePage.class);
  }

  private CardEditAddPage cardEditAddPage() {
    return getPage(CardEditAddPage.class);
  }

  private CardsListPage cardsListPage() {
    return getPage(CardsListPage.class);
  }
}
