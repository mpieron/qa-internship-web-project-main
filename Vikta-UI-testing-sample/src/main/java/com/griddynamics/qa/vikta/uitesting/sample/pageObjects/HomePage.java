package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

    public void writeTerm(String term){
        typeIn(term, tbTerm);
    }

    public void writeRatingFrom(String rating){
        typeIn(rating, tbRatingFrom);
    }

    public void writeRatingTo(String rating){
        typeIn( rating, tbRatingTo);
    }

    public void writePriceFrom(String price){
        typeIn(price, tbPriceFrom);
    }

    public void writePriceTo(String price){
        typeIn(price, tbPriceTo);
    }

    private void typeIn(String value, WebElement targetElement) {
        targetElement.clear();
        targetElement.sendKeys(value);
    }

    public List<WebElement> getImagesOnCurrentPage(){
        return productsList.findElements(By.className("product-card"));
    }

    public String getExistingImageTitle(){
        List<WebElement> productList = productsList.findElements(By.className("product-card__title"));
        return productList.get(new Random().nextInt(productList.size())).getText();
    }

    public String getExistingImageTag(){
        List<WebElement> tagList = productsList.findElements(By.className("product-card__text"));
        return Arrays.stream(tagList.get(new Random().nextInt(tagList.size())).getText()
                .split(", "))
                .limit(3)
                .collect(Collectors.toList())
                .toString()
                .replaceAll(", ", "|")
                .replaceAll("[\\[\\]]","")
                .replaceAll(" ", "~");
    }

    public void clickSearchBottom(){
        btnSearch.click();
    }

    // nothing happens, even when click on the page
    public void clickResetBottom(){
        btnResetSearchCriteria.click();
    }

}
