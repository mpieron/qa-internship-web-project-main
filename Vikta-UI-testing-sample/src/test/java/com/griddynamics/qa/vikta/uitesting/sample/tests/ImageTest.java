package com.griddynamics.qa.vikta.uitesting.sample.tests;

import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.ImageSteps;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ImageTest extends BaseTest {

  @BeforeMethod(onlyForGroups = "onlyLogin")
  public void loginAsAdmin() {
    loginSteps.openLoginPage();
    loginSteps.loginAsAdmin();
  }

  @BeforeMethod(onlyForGroups = "needNewImage")
  public void loginAndCreateNewImage() {
    loginAsAdmin();
    imageSteps.clickAddAnImage();

    imageSteps.fillNecessaryImageField(ImageSteps.NecessaryImageField.URL);
    imageSteps.fillNecessaryImageField(ImageSteps.NecessaryImageField.TITLE);

    imageSteps.clickSaveButton();
  }

  @AfterMethod(onlyForGroups = "cleanUp")
  public void clean() {
    imageSteps.clickImages();
    imageSteps.deleteAddedImages();
  }

  //    we can only search image by title or tag, but it won't return items with exact same term (can, but can also with part of it)
  @Test(groups = "onlyLogin")
  public void canFindImageByTitle() {
    //        Click Images
    imageSteps.clickImages();
    //        Get first image title
    String title = imageSteps.getFirstImageTitle();
    //        Search image by title
    imageSteps.searchImageByTitle(title);
    //        Verify if found image
    imageSteps.verifyIfFoundImageByTitle();
  }

  @Test(groups = { "onlyLogin", "cleanUp" })
  public void canAddImage() {
    //        Click Add an Image
    imageSteps.clickAddAnImage();

    //        Fill fields
    String url = imageSteps.fillNecessaryImageField(ImageSteps.NecessaryImageField.URL);
    String title = imageSteps.fillNecessaryImageField(ImageSteps.NecessaryImageField.TITLE);

    //        Save Image
    imageSteps.clickSaveButton();

    //        Go to the list of image items
    imageSteps.clickToTheListOfImageItems();

    //        Verify if added image
    imageSteps.verifyLastImageUrlAndTitle(url, title);
  }

  @Test(groups = { "needNewImage", "cleanUp" })
  public void canEditImage() {
    //        Click Images
    imageSteps.clickImages();

    //        Click last Image
    imageSteps.clickLastImage();

    //        Change Image url and title
    String url = imageSteps.fillNecessaryImageField(ImageSteps.NecessaryImageField.URL);
    String title = imageSteps.fillNecessaryImageField(ImageSteps.NecessaryImageField.TITLE);

    //        Save changes
    imageSteps.clickSaveButton();

    //        Go to the list of image items
    imageSteps.clickToTheListOfImageItems();

    //        Verify if image was changed
    imageSteps.verifyLastImageUrlAndTitle(url, title);
  }

  //    Deletion message is displayed, but image is still in the list
  @Test(groups = "needNewImage")
  public void canDeleteImage() {
    //        Click Images
    imageSteps.clickImages();

    //        Delete last image
    imageSteps.deleteAddedImages();

    //        Verify if image was deleted
    imageSteps.verifyIfImageWasDeleted();
  }
}
