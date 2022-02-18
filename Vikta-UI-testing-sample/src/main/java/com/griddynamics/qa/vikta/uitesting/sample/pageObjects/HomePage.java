package com.griddynamics.qa.vikta.uitesting.sample.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.*;
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

    @FindBy(id = "tSelectedCategoryTitle")
    private WebElement tSelectedCategoryTitle;

    @FindBy(id = "divErrorsAndMessages")
    private WebElement divErrorsAndMessages;

    @FindBy(id = "divMsgOrErr")
    private WebElement divMsgOrErr;

    // I think there shouldn't be included categories separately, because they can be changed, so main should be enough
    @FindBy(id = "divCategoryNames")
    private WebElement divCategoryNames;

    // same as above
    @FindBy(css = ".products-list")
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

    public WebElement getSelectedCategoryTitle(){
        return tSelectedCategoryTitle;
    }

    public List<String> getImagesTagsFromCurrentPage(){

        return  productsList.findElements(By.cssSelector(".product-card > .product-card__description > .product-card__text:last-child"))
                .stream()
                .map(WebElement::getText)
//                .map(tag -> tag.split(" "))
//                .flatMap(Arrays::stream)
                .map(tag -> tag.replaceAll("[\\[\\]]",""))
                .collect(Collectors.toList());
    }

    public List<WebElement> getImagesTitlesFromCurrentPage(){
        return productsList.findElements(By.className("product-card__title"));
    }

    public String getExistingImageTags(){
//        String tags = productsList.findElements(By.className(".product-card > .product-card__description > .product-card__text:last-child")).get(0).getText();
//        return   Arrays.stream(tags
//                .split(", "))
//                .limit(3)
//                .collect(Collectors.toList())
//                .toString()
//                .replaceAll(", ", "|")
//                .replaceAll("[\\[\\]]","")
//                .replaceAll(" ", "~");

        return "Europe|Cat";
    }

    public List<String> getImagePricesFromCurrentPage(){
        return productsList.findElements(By.className("product-card__value"))
                .stream()
                .map(WebElement::getText)
                .map(tag -> tag.replaceAll("[\\[\\]]",""))
                .collect(Collectors.toList());
    }

    public void clickSearchBottom(){
        btnSearch.click();
    }

    // nothing happens, even when click on the page
    public void clickResetBottom(){
        btnResetSearchCriteria.click();
    }
}
