package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object of Home page
 */
public class HomePage extends BasePage {

    @FindBy(id = "tbTerm")
    private WebElement tbTerm;

    @FindBy(id = "tbRatingFrom")
    private WebElement tbRatingFrom;

    @FindBy(id = "tbRatingTo")
    private WebElement tbRatingTo;

    @FindBy(id = "tbPriceFrom")
    private WebElement tbPriceFrom;

    @FindBy(id = "tbPriceTo")
    private WebElement tbPriceTo;

    @FindBy(id = "btnSearch")
    private WebElement btnSearch;

    @FindBy(id = "btnResetSearchCriteria")
    private WebElement btnResetSearchCriteria;

    @FindBy(id = "divErrorsAndMessages")
    private WebElement divErrorsAndMessages;

    @FindBy(id = "divMsgOrErr")
    private WebElement divMsgOrErr;

    // I think there shouldn't be included categories separately, because they can be changed, so main should be enough
    @FindBy(id = "divCategoryNames")
    private WebElement divCategoryNames;

    // same as above
    @FindBy(className = "products-list")
    private WebElement productsList;
}
