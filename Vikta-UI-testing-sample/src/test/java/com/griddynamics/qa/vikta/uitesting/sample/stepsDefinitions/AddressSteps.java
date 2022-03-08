package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.AddressEditAddPage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.AddressesListPage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.BasePage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.HomePage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.StringJoiner;

import static org.assertj.core.api.Assertions.assertThat;

public class AddressSteps extends BaseSteps{

    public AddressSteps(WebDriver driver) {
        super(driver);
    }

    public enum AddressField {
        STREET,
        ADDITIONAL_STREET_INFO,
        CITY,
        REGION,
        POSTAL_CODE,
        NICKNAME
    }

    @Step
    public String typeValueInto(AddressField addressField){
        Utilities utilities = new Utilities();
        String valueToReturn;
        switch (addressField){
            case STREET:
                valueToReturn = utilities.generateStreetAddress();
                addressEditAddPage().typeInStreet(valueToReturn);
                break;
            case ADDITIONAL_STREET_INFO:
                valueToReturn = utilities.generateAdditionalStreetInfoAddress();
                addressEditAddPage().typeInStreetAdditional(valueToReturn);
                break;
            case CITY:
                valueToReturn = utilities.generateCityAddress();
                addressEditAddPage().typeInCityName(valueToReturn);
                break;
            case REGION:
                valueToReturn = utilities.generateCountryAddress();
                addressEditAddPage().typeInRegionName(valueToReturn);
                break;
            case POSTAL_CODE:
                valueToReturn = utilities.generatePostalCodeAddress();
                addressEditAddPage().typeInPostalCode(valueToReturn);
                break;
            case NICKNAME:
                valueToReturn = utilities.generateNicknameAddress();
                addressEditAddPage().typeInAddressNickname(valueToReturn);
                break;
            default:
                throw new IllegalArgumentException("Unsupported Address Add/Edit page field name: " + addressField);
        }
        return valueToReturn;
    }

    @Step
    public void clickAtAddressesTab(){
        homePage().clickAddressesBottom();
    }

    @Step
    public void clickAddAddressTab(){
        homePage().clickAddAddressBottom();
    }

    @Step
    public void clickAtSecondAddressHyperlink(){
        addressesListPage().clickAtSecondAddressHyperlink();
    }

    @Step
    public void clickSaveButton(){
        addressEditAddPage().clickSaveButton();
    }

    @Step
    public void clickDeleteOrResetButton(){
        addressEditAddPage().clickDeleteOrResetButton();
    }

    @Step
    public void clickToTheListOfAddresses(){
        addressEditAddPage().clickToTheListOfAddresses();
    }

    @Step
    public void verifyAllFieldsInFirstAddressAreCorrect(){
        StringJoiner correctAddress = new StringJoiner(" ");
        correctAddress.add(getData().street());
        correctAddress.add(getData().additionalStreetInfo());
        correctAddress.add(getData().city());
        correctAddress.add(getData().region());
        correctAddress.add(getData().postalCode());
        correctAddress.add(getData().nickname());

        assertThat(addressesListPage().getFirstAddress().getText())
                .as("Default address is not correct")
                .contains(correctAddress.toString());
    }

    @Step
    public String fillAllFieldsInAddress(){
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
    public void verifyIfChangedAllFieldsCorrectly(String addressInfo){
        assertThat(addressesListPage().getSecondAddress().getText())
                .as("Address wasn't changed correctly")
                .contains(addressInfo);
    }

    @Step
    public void verifyIfAddedAddressCorrectly(String addressInfo){
        assertThat(addressesListPage().lastAddress.getText())
                .as("Address wasn't added correctly")
                .contains(addressInfo);
    }

    @Step
    public void deleteAddedAddresses(){
        List<WebElement> allAddressesHyperlinksList = addressesListPage().allAddressesHyperlinksList;

        for(int i=1; i<allAddressesHyperlinksList.size(); i++){
            allAddressesHyperlinksList.get(i).click();
            addressEditAddPage().clickDeleteOrResetButton();
            addressEditAddPage().clickToTheListOfAddresses();
        }
    }

    @Step
    public void verifyIfAddressWasDeleted(){
        List<WebElement> allAddressesHyperlinksList = addressesListPage().allAddressesHyperlinksList;

        assertThat(allAddressesHyperlinksList.size())
                .as("Address wasn't deleted")
                .isEqualTo(1);
    }

    @Step
    public void verifyIfAllFieldsOnAddPageAreEmpty(){
        assertThat(addressEditAddPage().getAllFields())
                .as("There is not empty field!")
                .allSatisfy(contents -> assertThat(contents.getText()).isEmpty());
    }

    private HomePage homePage() {
        return getPage(HomePage.class);
    }

    private BasePage basePage() {
        return getPage(BasePage.class);
    }

    private AddressEditAddPage addressEditAddPage() {
        return getPage(AddressEditAddPage.class);
    }

    private AddressesListPage addressesListPage() {
        return getPage(AddressesListPage.class);
    }

}
