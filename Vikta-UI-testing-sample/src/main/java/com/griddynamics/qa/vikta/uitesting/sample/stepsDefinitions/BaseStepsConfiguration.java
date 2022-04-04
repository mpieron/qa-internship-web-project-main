package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import com.griddynamics.qa.vikta.uitesting.sample.pageObjects.HomePage;
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
  public WebDriver driver() {
    return driverManager.get();
  }

  @Bean
  public HomePage homePage() {
    return getPage(HomePage.class);
  }

  <P> P getPage(Class<P> pageClass) {
    return PageFactory.initElements(driverManager.get(), pageClass);
  }

}
