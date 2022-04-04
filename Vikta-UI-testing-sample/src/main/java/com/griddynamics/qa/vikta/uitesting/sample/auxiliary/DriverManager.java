package com.griddynamics.qa.vikta.uitesting.sample.auxiliary;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.springframework.stereotype.Component;

/**
 * Manages WebDriver instantiation etc.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public final class DriverManager {

  private final ThreadLocal<WebDriver> threadWebDriver;

  public void quite() {
    if (Objects.nonNull(threadWebDriver.get())) {
      log.info("Shutting down the driver.");
      threadWebDriver.get().quit();
    }
  }

  public WebDriver get() {
    return threadWebDriver.get();
  }
}
