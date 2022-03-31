package com.griddynamics.qa.vikta.uitesting.sample.tests;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.AddressSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.CardSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.CategorySteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.HomePageSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.ImageSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.LoginSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.RegistrationSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.ShoppingCartSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.UserManagementSteps;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

@SpringBootTest
public class BaseTest extends AbstractTestNGSpringContextTests {

  @Autowired
  LoginSteps loginSteps;
  @Autowired
  RegistrationSteps registrationSteps;
  @Autowired
  HomePageSteps homePageSteps;
  @Autowired
  AddressSteps addressSteps;
  @Autowired
  CardSteps cardSteps;
  @Autowired
  ImageSteps imageSteps;
  @Autowired
  CategorySteps categorySteps;
  @Autowired
  UserManagementSteps userManagementSteps;
  @Autowired
  ShoppingCartSteps shoppingCartSteps;
  @Autowired
  private DriverManager driverManager;

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
