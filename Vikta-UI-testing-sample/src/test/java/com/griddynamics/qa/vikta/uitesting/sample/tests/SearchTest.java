package com.griddynamics.qa.vikta.uitesting.sample.tests;

import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.HomePageSteps;
import org.testng.annotations.Test;

/**
 * Feature: Searching images, categories
 * I should be able to search category, image by term/tags/other criteria available on the home page
 */
public class SearchTest extends BaseTest{

    @Test
    public void canFindImageByTitle(){
        // Open Home Page
        homePageSteps.openHomePage();

        // Type title as term
        String title = homePageSteps.typeValueInto(HomePageSteps.FieldName.TITLE);

        //Click search bottom
        homePageSteps.clickSearchBottom();

        // Verify if found any image (should find at least one)
        homePageSteps.verifyFoundImages(title);
    }

    @Test
    public void canFindImageByTag(){
        // Open Home Page
        homePageSteps.openHomePage();

        // Type tag as term
        String tags = homePageSteps.typeValueInto(HomePageSteps.FieldName.TAG);

        //Click search bottom
        homePageSteps.clickSearchBottom();

        // Verify if found any image (should find at least one)
        homePageSteps.verifyFoundImages(tags);
    }


}