package com.griddynamics.qa.vikta.uitesting.sample.tests;

import org.testng.annotations.*;

public class AddressTest extends BaseTest{

    @BeforeMethod
    public void login(){
        loginSteps.openLoginPage();
        loginSteps.loginAsRegularUser();
    }

    @BeforeGroups("needAddress")
    public void generateNewAddress(){
        addressSteps.clickAddAddressTab();
        addressSteps.fillAllFieldsInAddress();
        addressSteps.clickSaveButton();
    }

    @AfterGroups("needCleanUp")
    public void clean(){
        addressSteps.clickAtAddressesTab();
        addressSteps.deleteAddedAddresses();
    }

//    Looks like first address is also generated each time
    @Test
    public void checkThatAllFieldsInFirstAddressAreCorrect(){
//        Click at Addresses tab
        addressSteps.clickAtAddressesTab();

//        Verify default address is correct
        addressSteps.verifyAllFieldsInFirstAddressAreCorrect();
    }

    @Test(groups = {"needAddress", "needCleanUp"})
    public void canEditAddress(){
//        Click at Addresses tab
        addressSteps.clickAtAddressesTab();

//        Click at second address hyperlink
        addressSteps.clickAtSecondAddressHyperlink();

//        Change all fields
        String addressInfo = addressSteps.fillAllFieldsInAddress();

//        Save changes
        addressSteps.clickSaveButton();

//        Go to addresses list
        addressSteps.clickToTheListOfAddresses();

//        Check if address was changed
        addressSteps.verifyIfChangedAllFieldsCorrectly(addressInfo);
    }

    @Test(groups = "needCleanUp")
    public void canAddAddress(){
//        Click at Add Address tab
        addressSteps.clickAddAddressTab();

//        Fill all fields
        String addressInfo = addressSteps.fillAllFieldsInAddress();

//        Save new address
        addressSteps.clickSaveButton();

//        Go to addresses list
        addressSteps.clickToTheListOfAddresses();

//        Check if address was added correctly
        addressSteps.verifyIfAddedAddressCorrectly(addressInfo);
    }

    @Test(groups = "needAddress")
    public void canDeleteAddress(){
//        Click at Addresses tab
        addressSteps.clickAtAddressesTab();

//        Click at second address hyperlink
        addressSteps.clickAtSecondAddressHyperlink();

//        Delete second address
        addressSteps.clickDeleteOrResetButton();

//        Go to addresses list
        addressSteps.clickToTheListOfAddresses();

//        Check if address was deleted
        addressSteps.verifyIfAddressWasDeleted();
    }

    @Test
    public void canClearFieldsWhileAddingNewAddress(){
//        Click at Add Address tab
        addressSteps.clickAddAddressTab();

//        Fill all fields
        addressSteps.fillAllFieldsInAddress();

//        Click clear button
        addressSteps.clickDeleteOrResetButton();

//        Check if all fields are empty
        addressSteps.verifyIfAllFieldsOnAddPageAreEmpty();
    }
}
