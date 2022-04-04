package com.griddynamics.qa.vikta.uitesting.sample.auxiliary;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import java.util.Objects;

import com.griddynamics.qa.vikta.uitesting.sample.config.TestSetupConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manages WebDriver instantiation etc.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public final class DriverManager {

  @Autowired
  private final ThreadLocal<WebDriver> threadWebDriver;
//  private final TestSetupConfiguration properties;
//
//  public DriverManager(TestSetupConfiguration properties){
//    this.properties = properties;
//  }

  public void quite() {
    if (Objects.nonNull(threadWebDriver.get())) {
      log.info("Shutting down the driver.");
      threadWebDriver.get().quit();
    }
  }

  public WebDriver get() {
    return threadWebDriver.get();
  }

  public byte[] takeScreenshot() {
    return ((TakesScreenshot) get()).getScreenshotAs(OutputType.BYTES);
  }
}
