package com.griddynamics.qa.vikta.uitesting.sample.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ShoppingCartTest extends BaseTest {

    @BeforeMethod(onlyForGroups = "loginAsUser")
    public void loginAsUser() {
        loginSteps.openLoginPage();
        loginSteps.loginAsRegularUser();
    }

    @BeforeMethod(onlyForGroups = "loginAsAdmin")
    public void loginAsAdmin() {
        loginSteps.openLoginPage();
        loginSteps.loginAsAdmin();
    }

    @BeforeMethod(onlyForGroups = "loginAsUserAndAddImageToShoppingCart")
    public void loginAsUserAndAddImageToShoppingCart() {
        loginAsUser();
        shoppingCartSteps.addFirstImageToShoppingCart();
    }

    @BeforeMethod(onlyForGroups = "loginAsAdminAndAddImageToShoppingCart")
    public void loginAsAdminAndAddImageToShoppingCart() {
        loginAsAdmin();
        shoppingCartSteps.addFirstImageToShoppingCart();
    }

    @AfterMethod(onlyForGroups = "cleanUp")
    public void clean() {
        shoppingCartSteps.goToShoppingCartPage();
        shoppingCartSteps.emptyShoppingCart();
    }

    @Test(groups = {"loginAsUser", "cleanUp"})
    public void userCanAddImageToShoppingCart() {
//        Add image to ShoppingCart
        shoppingCartSteps.addFirstImageToShoppingCart();

//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Verify id added image
        shoppingCartSteps.verifyIfAddedImageToShoppingCart();
    }

    @Test(groups = {"loginAsAdmin", "cleanUp"})
    public void adminCanAddImageToShoppingCart() {
//        Add image to ShoppingCart
        shoppingCartSteps.addFirstImageToShoppingCart();

//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Verify id added image
        shoppingCartSteps.verifyIfAddedImageToShoppingCart();
    }

    @Test(groups = "loginAsUserAndAddImageToShoppingCart")
    public void userCanEmptyShoppingCart() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Empty ShoppingCart
        shoppingCartSteps.emptyShoppingCart();

//        Verify if ShoppingCart is empty
        shoppingCartSteps.verifyIfShoppingCartIsEmpty();
    }

    @Test(groups = "loginAsAdminAndAddImageToShoppingCart")
    public void adminCanEmptyShoppingCart() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Empty ShoppingCart
        shoppingCartSteps.emptyShoppingCart();

//        Verify if basket is empty
        shoppingCartSteps.verifyIfShoppingCartIsEmpty();
    }

    @Test(groups = {"loginAsUserAndAddImageToShoppingCart", "cleanUp"})
    public void userCanNotBuyWithoutDelivery() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Click purchase button
        shoppingCartSteps.purchase();

//        Verify that image was not bought
        shoppingCartSteps.verifyIfImageCanNotBePurchasedWithoutChosenDelivery();
    }

    @Test(groups = {"loginAsAdminAndAddImageToShoppingCart", "cleanUp"})
    public void adminCanNotBuyWithoutDelivery() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Click purchase button
        shoppingCartSteps.purchase();

//        Verify that image was not bought
        shoppingCartSteps.verifyIfImageCanNotBePurchasedWithoutChosenDelivery();
    }

    @Test(groups = {"loginAsUserAndAddImageToShoppingCart", "cleanUp"})
    public void userCanNotBuyWithoutPaymentMethod() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Select delivery
        shoppingCartSteps.selectDeliver();

//        Click purchase button
        shoppingCartSteps.purchase();

//        Verify that image was not bought
        shoppingCartSteps.verifyIfImageCanNotBePurchasedWithoutChosenPaymentMethod();
    }

//     admin don't have any addresses and payment methods
    @Test(groups = {"loginAsAdminAndAddImageToShoppingCart", "cleanUp"})
    public void adminCanNotBuyWithoutPaymentMethod() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Select delivery
        shoppingCartSteps.selectDeliver();

//        Click purchase button
        shoppingCartSteps.purchase();

//        Verify that image was not bought
        shoppingCartSteps.verifyIfImageCanNotBePurchasedWithoutChosenPaymentMethod();
    }

    @Test(groups = "loginAsUserAndAddImageToShoppingCart")
    public void userCanBuyImage() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Select delivery
        shoppingCartSteps.selectDeliver();

//        Select payment
        shoppingCartSteps.selectPayment();

//        Click purchase button
        String totalPrice = shoppingCartSteps.purchase();

//        Verify purchase
        shoppingCartSteps.verifyPurchase(totalPrice);
    }

//    don't work if admin do not have address and payment method
    @Test(groups = {"loginAsAdminAndAddImageToShoppingCart", "cleanUp"})
    public void adminCanBuyImage() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Select delivery
        shoppingCartSteps.selectDeliver();

//        Select payment
        shoppingCartSteps.selectPayment();

//        Click purchase button
        String totalPrice = shoppingCartSteps.purchase();

//        Verify purchase
        shoppingCartSteps.verifyPurchase(totalPrice);
    }

    @Test(groups = {"loginAsUserAndAddImageToShoppingCart", "cleanUp"})
    public void userCanIncreaseNumberOfImages(){
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Get number of images plus one
        int expectedNumber = shoppingCartSteps.getFirstImageAmount() + 1;

//        Increase number of images
        shoppingCartSteps.increaseNumberOfFirstImage();

//        Verify if the number of images increased
        shoppingCartSteps.verifyIfImageAmountIncrease(expectedNumber);
    }

    @Test(groups = {"loginAsAdminAndAddImageToShoppingCart", "cleanUp"})
    public void adminCanIncreaseNumberOfImages() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Get number of images plus one
        int expectedNumber = shoppingCartSteps.getFirstImageAmount() + 1;

//        Increase number of images
        shoppingCartSteps.increaseNumberOfFirstImage();

//        Verify if the number of images increased
        shoppingCartSteps.verifyIfImageAmountIncrease(expectedNumber);
    }

    @Test(groups = {"loginAsUserAndAddImageToShoppingCart", "cleanUp"})
    public void userCanReduceNumberOfImages(){
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Get number of images minus one
        int expectedNumber = shoppingCartSteps.getFirstImageAmount() - 1;

//        Increase number of images
        shoppingCartSteps.reduceNumberOfFirstImage();

//        Verify if the number of images increased
        shoppingCartSteps.verifyIfImageAmountIncrease(expectedNumber);
    }

    @Test(groups = {"loginAsAdminAndAddImageToShoppingCart", "cleanUp"})
    public void adminCanReduceNumberOfImages() {
//        Go to ShoppingCart
        shoppingCartSteps.goToShoppingCartPage();

//        Get number of images minus one
        int expectedNumber = shoppingCartSteps.getFirstImageAmount() - 1;

//        Increase number of images
        shoppingCartSteps.reduceNumberOfFirstImage();

//        Verify if the number of images increased
        shoppingCartSteps.verifyIfImageAmountIncrease(expectedNumber);
    }

}
