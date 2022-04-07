package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.*;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BaseStepsConfiguration {

  private final DriverManager driverManager;

  @Bean
  BasePage basePage() {
    return getPage(BasePage.class);
  }

  @Bean
  HomePage homePage() {
    return getPage(HomePage.class);
  }

  @Bean
  LoginPage loginPage() {
    return getPage(LoginPage.class);
  }

  @Bean
  RegistrationPage registrationPage() {
    return getPage(RegistrationPage.class);
  }

  @Bean
  AddressEditAddPage addressEditAddPage() {
    return getPage(AddressEditAddPage.class);
  }

  @Bean
  AddressesListPage addressesListPage() {
    return getPage(AddressesListPage.class);
  }

  @Bean
  CardEditAddPage cardEditAddPage() {
    return getPage(CardEditAddPage.class);
  }

  @Bean
  CardsListPage cardsListPage() {
    return getPage(CardsListPage.class);
  }

  @Bean
  CategoriesListPage categoryListPage() {
    return getPage(CategoriesListPage.class);
  }

  @Bean
   CategoryEditAddPage categoryEditAddPage() {
    return getPage(CategoryEditAddPage.class);
  }

  @Bean
   AdminBasePage adminBasePage() {
    return getPage(AdminBasePage.class);
  }

  @Bean
  ImagesListPage imagesListPage() {
    return getPage(ImagesListPage.class);
  }

  @Bean
  ImageEditAddPage imageEditAddPage() {
    return getPage(ImageEditAddPage.class);
  }

  @Bean
  ShoppingCartPage shoppingCartPage() {
    return getPage(ShoppingCartPage.class);
  }

  @Bean
  ImageDetailsPage imageDetailsPage() {
    return getPage(ImageDetailsPage.class);
  }

  @Bean
  UsersListPage usersListPage() {
    return getPage(UsersListPage.class);
  }

  @Bean
  UserEditAddPage userEditAddPage() {
    return getPage(UserEditAddPage.class);
  }

  <P> P getPage(Class<P> pageClass) {
    return PageFactory.initElements(driverManager.get(), pageClass);
  }

}
