package com.griddynamics.qa.vikta.uitesting.sample.tests;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestDataConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestSetupConfiguration;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.AddressSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.CardSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.CategorySteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.HomePageSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.ImageSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.LoginSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.RegistrationSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.ShoppingCartSteps;
import com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions.UserManagementSteps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BaseTestConfiguration {

  @Autowired
  private final DriverManager driverManager;

  private final TestSetupConfiguration properties;

  private final TestDataConfiguration testData;

  @Bean
  public LoginSteps loginSteps(){
    return new LoginSteps(properties, testData, driverManager.get());
  }

  @Bean
  public RegistrationSteps registrationSteps(){
    return new RegistrationSteps(properties, testData, driverManager.get());
  }

  @Bean
  public HomePageSteps homePageSteps(){
    return new HomePageSteps(properties, testData, driverManager.get());
  }

  @Bean
  public AddressSteps addressSteps(){
    return new AddressSteps(properties, testData, driverManager.get());
  }

  @Bean
  public CardSteps cardSteps(){
    return new CardSteps(properties, testData, driverManager.get());
  }

  @Bean
  public ImageSteps imageSteps(){
    return new ImageSteps(properties, testData, driverManager.get());
  }

  @Bean
  public CategorySteps categorySteps(){
    return new CategorySteps(properties, testData, driverManager.get());
  }

  @Bean
  public UserManagementSteps userManagementSteps(){
    return new UserManagementSteps(properties, testData, driverManager.get());
  }

  @Bean
  public ShoppingCartSteps shoppingCartSteps(){
    return new ShoppingCartSteps(properties, testData, driverManager.get());
  }
}
