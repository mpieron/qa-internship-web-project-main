package com.griddynamics.qa.vikta.uitesting.sample.tests;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.config.DataProvider;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.*;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class BaseTest {

  // TODO: Think about some IoC/DI here.
  private DriverManager driverManager;

  LoginSteps loginSteps;
  RegistrationSteps registrationSteps;
  HomePageSteps homePageSteps;
  AddressSteps addressSteps;
  CardSteps cardSteps;
  ImageSteps imageSteps;

  BaseTest() {
    driverManager = new DriverManager(DataProvider.get());
  }

  @BeforeClass
  void setupClass() {
    driverManager.instantiateDriver();

    loginSteps = new LoginSteps(driverManager.get());
    registrationSteps = new RegistrationSteps(driverManager.get());
    homePageSteps = new HomePageSteps(driverManager.get());
    addressSteps = new AddressSteps(driverManager.get());
    cardSteps = new CardSteps(driverManager.get());
    imageSteps = new ImageSteps(driverManager.get());
  }

  @AfterMethod
  public void makeScreenshotOnFailure(ITestResult testResult) {
    if (testResult.getStatus() == ITestResult.FAILURE) {
      try {
        Allure.addAttachment(testResult.getName(), new ByteArrayInputStream(takeScreenShot()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private byte[] takeScreenShot() throws IOException {
    return ((TakesScreenshot) driverManager.get()).getScreenshotAs(OutputType.BYTES);
  }

  @AfterClass
  void tearDownClass() {
    driverManager.quite();
  }
}
