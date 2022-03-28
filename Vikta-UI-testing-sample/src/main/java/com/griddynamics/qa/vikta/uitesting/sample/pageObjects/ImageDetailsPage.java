package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ImageDetailsPage extends BasePage{

    @FindBy(id = "aAddToCart")
    private WebElement aAddToCart;

    public void  clickAddToCart(){ aAddToCart.click();}
}
