package com.griddynamics.qa.vikta.uitesting.sample.stepsDefinitions;

import com.griddynamics.qa.vikta.uitesting.sample.auxiliary.DriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BaseStepsConfiguration {

  @Autowired
  private final DriverManager driverManager;

  @Bean
  public WebDriver driver() {
    return driverManager.get();
  }

}
