package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ShoppingCartPage extends BasePage{

    private String productCardValue = ".product-card__value";

    @FindBy(id = "spCartTotal")
    private WebElement spCartTotal;

    @FindBy(id = "slctAddress")
    private WebElement slctAddress;

    @FindBy(id = "slctPayment")
    private WebElement slctPayment;

    @FindBy(id = "btnPurchase")
    private WebElement btnPurchase;

    @FindBy(id = "btnEmpty")
    private WebElement btnEmpty;

    @FindBy(css = "body")
    protected WebElement body;

    @FindBy(id = "divUponPurchase")
    protected WebElement divUponPurchase;

    @FindBy(id = "spPurchaseTotal")
    protected WebElement spPurchaseTotal;

    @FindBy(id= "spToBeDeliveredTo")
    protected WebElement spToBeDeliveredTo;

    @FindBy(id = "spPaidBy")
    protected WebElement spPaidBy;

    private WebElement getProductCardMinusBottom(){ return body.findElement(By.name("btnDec")); }

    private WebElement getProductCardPlusBottom(){ return body.findElement(By.name("btnInc")); }

    public void clickPlusNumberOfFirstItem(){ getProductCardPlusBottom().click();}

    public void clickMinusNumberOfFirstItem(){ getProductCardMinusBottom().click();}

    public int getFirstImageAmount(){
        return Integer.parseInt((body.findElement(By.xpath("/html/body/main/div[2]/div/table/tbody/tr[2]/td/div/div/div/nav/div/span[3]"))).getText());
    }

    public void clickEmptyButton(){ btnEmpty.click(); }

    public void clickPurchaseButton(){
        btnPurchase.click();
    }

    public boolean slctAddressColorIsRed(){
        return slctAddress.getAttribute("style").equals("color: red;");
    }

    public boolean slctPaymentColorIsRed(){
        return slctPayment.getAttribute("style").equals("color: red;");
    }

    public void selectDelivery(){
        Select select = new Select(slctAddress);
        select.selectByIndex(1);
    }
    public void selectPayment(){
        Select select = new Select(body.findElement(By.id("slctPayment")));
        select.selectByIndex(1);
    }

    public boolean divUponPurchaseIsDisplayed(){
        return divUponPurchase.isDisplayed();
    }

    public String getSpPurchaseTotal(){ return spPurchaseTotal.getText(); }

    public String getSpCartTotal(){ return spCartTotal.getText(); }

    public String getPurchaseDeliveredTo(){ return spToBeDeliveredTo.getText(); }

    public String getPurchasePaidBy(){ return spPaidBy.getText(); }

    public String getSlctAddress(){
        return slctAddress.getText();
    }

    public String getSlctPayment(){
        return slctPayment.getText();
    }
}
