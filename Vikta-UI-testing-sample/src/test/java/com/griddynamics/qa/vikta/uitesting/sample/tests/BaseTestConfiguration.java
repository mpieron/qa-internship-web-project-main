package com.griddynamics.qa.vikta.uitesting.sample.tests;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BaseTestConfiguration {

  @Bean
  public LoginSteps loginSteps() {
    return new LoginSteps();
  }

  @Bean
  public RegistrationSteps registrationSteps() {
    return new RegistrationSteps();
  }

  @Bean
  public HomePageSteps homePageSteps() {
    return new HomePageSteps();
  }

  @Bean
  public AddressSteps addressSteps() {
    return new AddressSteps();
  }

  @Bean
  public CardSteps cardSteps() {
    return new CardSteps();
  }

  @Bean
  public ImageSteps imageSteps() {
    return new ImageSteps();
  }

  @Bean
  public CategorySteps categorySteps() {
    return new CategorySteps();
  }

  @Bean
  public UserManagementSteps userManagementSteps() {
    return new UserManagementSteps();
  }

  @Bean
  public ShoppingCartSteps shoppingCartSteps() {
    return new ShoppingCartSteps();
  }
}
