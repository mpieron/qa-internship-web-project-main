package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.AddressEditAddPage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.AddressesCardsListPage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.HomePage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import java.util.List;
import java.util.StringJoiner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AddressSteps extends BaseSteps {

  public AddressSteps(WebDriver driver) {
    super(driver);
  }

  private enum AddressField {
    STREET,
    ADDITIONAL_STREET_INFO,
    CITY,
    REGION,
    POSTAL_CODE,
    NICKNAME,
  }

  @Step
  public String typeValueInto(AddressField addressField) {
    String valueToReturn;
    switch (addressField) {
      case STREET:
        valueToReturn = Utilities.generateStreetAddress();
        addressEditAddPage().typeInStreet(valueToReturn);
        break;
      case ADDITIONAL_STREET_INFO:
        valueToReturn = Utilities.generateAdditionalStreetInfoAddress();
        addressEditAddPage().typeInStreetAdditional(valueToReturn);
        break;
      case CITY:
        valueToReturn = Utilities.generateCityAddress();
        addressEditAddPage().typeInCityName(valueToReturn);
        break;
      case REGION:
        valueToReturn = Utilities.generateCountryAddress();
        addressEditAddPage().typeInRegionName(valueToReturn);
        break;
      case POSTAL_CODE:
        valueToReturn = Utilities.generatePostalCodeAddress();
        addressEditAddPage().typeInPostalCode(valueToReturn);
        break;
      case NICKNAME:
        valueToReturn = Utilities.generateNickname();
        addressEditAddPage().typeInAddressNickname(valueToReturn);
        break;
      default:
        throw new IllegalArgumentException(
          "Unsupported Address Add/Edit page field name: " + addressField
        );
    }
    return valueToReturn;
  }

  @Step
  public void clickAtAddressesTab() {
    homePage().clickAddressesBottom();
  }

  @Step
  public void clickAddAddressTab() {
    homePage().clickAddAddressBottom();
  }

  @Step
  public void clickAtSecondAddressHyperlink() {
    addressesListPage().clickAtSecondAddressHyperlink(true);
  }

  @Step
  public void clickSaveButton() {
    addressEditAddPage().clickSaveButton();
  }

  @Step
  public void clickDeleteOrResetButton() {
    addressEditAddPage().clickDeleteOrResetButton();
  }

  @Step
  public void clickToTheListOfAddresses() {
    addressEditAddPage().clickToTheListOfAddresses();
  }

  @Step
  public void verifyFirstAddressNicknameIsCorrect() {
    assertThat(addressesListPage().getFirstFromList(true).getText())
      .as("Default address nickname is not correct.")
      .contains(getData().nickname());
  }

  @Step
  public String fillAllFieldsInAddress() {
    StringJoiner addressInfo = new StringJoiner(" ");
    addressInfo.add(typeValueInto(AddressField.STREET));
    addressInfo.add(typeValueInto(AddressSteps.AddressField.ADDITIONAL_STREET_INFO));
    addressInfo.add(typeValueInto(AddressSteps.AddressField.CITY));
    addressInfo.add(typeValueInto(AddressSteps.AddressField.REGION));
    addressInfo.add(typeValueInto(AddressSteps.AddressField.POSTAL_CODE));
    addressInfo.add(typeValueInto(AddressSteps.AddressField.NICKNAME));
    return addressInfo.toString();
  }

  @Step
  public void verifyIfChangedAllFieldsCorrectly(String addressInfo) {
    String addressOnPage = addressesListPage().getSecondFromList(true).getText().substring(2);
    assertThat(addressOnPage)
      .as("Address wasn't changed correctly. Should be %s, but was %s", addressInfo, addressOnPage)
      .isEqualTo(addressInfo);
  }

  @Step
  public void verifyIfAddedAddressCorrectly(String addressInfo) {
    String addedAddress = addressesListPage().getLastAddress(true).getText().substring(2);
    assertThat(addedAddress)
      .as("Address wasn't added correctly. Should be %s, was %s", addedAddress, addressInfo)
      .isEqualTo(addressInfo);
  }

  @Step
  public void deleteAddedAddresses() {
    List<WebElement> allAddressesHyperlinksList = addressesListPage()
      .getAllAddressesHyperlinksList(true);

    for (int i = 1; i < allAddressesHyperlinksList.size(); i++) {
      allAddressesHyperlinksList.get(i).click();
      addressEditAddPage().clickDeleteOrResetButton();
      addressEditAddPage().clickToTheListOfAddresses();
    }
  }

  @Step
  public void verifyIfAddressWasDeleted() {
    List<WebElement> allAddressesHyperlinksList = addressesListPage()
      .getAllAddressesHyperlinksList(true);

    assertThat(allAddressesHyperlinksList.size()).as("Address wasn't deleted").isEqualTo(1);
  }

  @Step
  public void verifyIfAllFieldsOnAddPageAreEmpty() {
    assertThat(addressEditAddPage().getAllFields())
      .as("There is not empty field!")
      .allSatisfy(contents -> assertThat(contents.getText()).isEmpty());
  }

  private HomePage homePage() {
    return getPage(HomePage.class);
  }

  private AddressEditAddPage addressEditAddPage() {
    return getPage(AddressEditAddPage.class);
  }

  private AddressesCardsListPage addressesListPage() {
    return getPage(AddressesCardsListPage.class);
  }
}
