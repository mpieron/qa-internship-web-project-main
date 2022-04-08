package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.Assertions.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.HomePage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.ImageDetailsPage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.ShoppingCartPage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.GenericWebActions;
import io.qameta.allure.Step;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;

public class ShoppingCartSteps extends GenericWebActions {

  @Autowired
  private HomePage homePage;
  @Autowired
  private ShoppingCartPage shoppingCartPage;
  @Autowired
  private ImageDetailsPage imageDetailsPage;

  @Step
  public void addFirstImageToShoppingCart() {
    homePage.clickFirstImageDetailsButton();
    imageDetailsPage.clickAddToCart();
  }

  @Step
  public void goToShoppingCartPage() {
    shoppingCartPage.goToShoppingCart();
  }

  @Step
  public void emptyShoppingCart() {
    shoppingCartPage.clickEmptyButton();
  }

  @Step
  public String purchase() {
    String total = shoppingCartPage.getSpCartTotal();
    shoppingCartPage.clickPurchaseButton();
    return total;
  }

  @Step
  public void increaseNumberOfFirstImage() {
    shoppingCartPage.clickPlusNumberOfFirstItem();
  }

  @Step
  public void reduceNumberOfFirstImage() {
    shoppingCartPage.clickMinusNumberOfFirstItem();
  }

  @Step
  public int getFirstImageAmount() {
    return shoppingCartPage.getFirstImageAmount();
  }

  @Step
  public void selectDeliver() {
    shoppingCartPage.selectDelivery();
  }

  @Step
  public void selectPayment() {
    shoppingCartPage.selectPayment();
  }

  @Step
  public void verifyIfAddedImageToShoppingCart() {
    getWait().until(ExpectedConditions.visibilityOf(shoppingCartPage.getShoppingCart()));
    assertThat(shoppingCartPage.getShoppingCart().isDisplayed())
        .as("Basket icon should be displayed")
        .isTrue();

    assertThat(shoppingCartPage.getNumberOfItemsInShoppingCart())
        .as("Image should be added to basket")
        .isEqualTo("1");
  }

  @Step
  public void verifyIfShoppingCartIsEmpty() {
    assertThat(shoppingCartPage.shoppingCartIconIsDisplayed())
        .as("Basket icon should not be displayed")
        .isFalse();
  }

  @Step
  public void verifyIfImageCanNotBePurchasedWithoutChosenDelivery() {
    assertThat(shoppingCartPage.slctAddressColorIsRed())
        .as("\"Delivery to\" should change color to red")
        .isTrue();
  }

  @Step
  public void verifyIfImageCanNotBePurchasedWithoutChosenPaymentMethod() {
    assertThat(shoppingCartPage.slctPaymentColorIsRed())
        .as("\"To be paid by\" should change color to red")
        .isTrue();
  }

  @Step
  public void verifyPurchase(String totalPrice) {
    assertThat(shoppingCartPage.divUponPurchaseIsDisplayed())
        .as("Purchase should be displayed")
        .isTrue();

    assertThat(shoppingCartPage.getSpPurchaseTotal())
        .as("Purchase total should be %s but is %s", totalPrice,
            shoppingCartPage.getSpPurchaseTotal())
        .contains(totalPrice);

    assertThat(shoppingCartPage.getPurchaseDeliveredTo())
        .as("Delivery should be to %s, but was to %s",
            shoppingCartPage.getSlctAddress().split("...")[1],
            shoppingCartPage.getPurchaseDeliveredTo())
        .contains(shoppingCartPage.getSlctAddress().split("...")[1]);

    assertThat(shoppingCartPage.getPurchasePaidBy())
        .as("Payment should be by %s, but was by %s",
            shoppingCartPage.getSlctPayment().split("...")[1],
            shoppingCartPage.getPurchasePaidBy())
        .contains(shoppingCartPage.getSlctPayment().split("...")[1]);
  }

  @Step
  public void verifyIfImageAmountIncrease(int expectedNumber) {
    goToShoppingCartPage();
    assertThat(getFirstImageAmount())
        .as("Should be %d, but was %d images", expectedNumber, getFirstImageAmount())
        .isEqualTo(expectedNumber);
  }
}
