package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.AdminBasePage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.ImageEditAddPage;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.ImagesListPage;
import com.griddynamics.qa.vikta.uitesting.sample.utils.Utilities;
import io.qameta.allure.Step;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ImageSteps extends BaseSteps {

  public ImageSteps(WebDriver driver) {
    super(driver);
  }

  public enum NecessaryImageField {
    URL,
    TITLE,
  }

  @Step
  public String fillNecessaryImageField(NecessaryImageField imageField) {
    String valueToReturn;
    switch (imageField) {
      case URL:
        valueToReturn = Utilities.generateURL();
        imageEditAddPage().typeURL(valueToReturn);
        break;
      case TITLE:
        valueToReturn = Utilities.generateTitle();
        imageEditAddPage().typeTitle(valueToReturn);
        break;
      default:
        throw new IllegalArgumentException(
          "Unsupported Address Add/Edit page field name: " + imageField
        );
    }
    return valueToReturn;
  }

  @Step
  public void clickImages() {
    adminBasePage().clickImages();
  }

  @Step
  public void clickAddAnImage() {
    adminBasePage().clickAddImage();
  }

  @Step
  public String getFirstImageTitle() {
    return imagesListPage().getAllImagesHyperlinksList().get(0).getText();
  }

  @Step
  public void clickSearchButton() {
    imagesListPage().clickSearchButton();
  }

  @Step
  public void clickSaveButton() {
    imageEditAddPage().clickSaveButton();
  }

  @Step
  public void clickToTheListOfImageItems() {
    adminBasePage().clickImages();
  }

  @Step
  public void scroll(){
    WebElement lastImage = imagesListPage().getLastImageFromList();
    Actions scroll = new Actions(getDriver());
    scroll.moveToElement(lastImage);
    scroll.perform();
  }

  @Step
  public void searchImageByTitle(String title) {
    imagesListPage().typeSearchTerm(title);
    clickSearchButton();
  }

  @Step
  public void verifyIfFoundImageByTitle() {
    assertThat(imagesListPage().getAllImagesHyperlinksList().size())
      .as("Image not found by title")
      .isGreaterThanOrEqualTo(1);
  }

  @Step
  public void verifyLastImageUrlAndTitle(String url, String title) {
    scroll();

    String lastImage = imagesListPage().getLastImageFromList().getText();

    assertThat(lastImage)
      .as("Image \"%s\" has wrong url, should contains url %s", lastImage, url)
      .contains(url);

    assertThat(lastImage)
      .as("Image \"%s\" has wrong title, should contains %s", lastImage, title)
      .contains(title);
  }

  @Step
  public void deleteAddedImages() {
    scroll();
    imagesListPage().removeLastImage();
  }

  @Step
  public void getNumberOfImagesOnPage() {
    scroll();
    imagesListPage().getAllImagesHyperlinksList();
  }

  @Step
  public void verifyIfImageWasDeleted() {
    assertThat(imagesListPage().messageDeleteIsDisplayed())
      .as("Deletion message should be displayed")
      .isTrue();
  }

  @Step
  public void clickLastImage() {
    scroll();
    List<WebElement> imagesList = imagesListPage().getAllImagesHyperlinksList();

    imagesList.get(imagesList.size() - 1).click();
  }

  ImagesListPage imagesListPage() {
    return getPage(ImagesListPage.class);
  }

  ImageEditAddPage imageEditAddPage() {
    return getPage(ImageEditAddPage.class);
  }

  AdminBasePage adminBasePage() {
    return getPage(AdminBasePage.class);
  }
}
