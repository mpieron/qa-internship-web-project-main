package com.griddynamics.qa.vikta.uitesting.sample.auxiliary;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestDataAndProperties;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

/**
 * Manages WebDriver instantiation etc.
 */
@Slf4j
@RequiredArgsConstructor
public final class DriverManager {

  private static final int THOUSAND = 1000;
  private final TestDataAndProperties properties;
  private ThreadLocal<WebDriver> threadWebDriver = new ThreadLocal<>();

  enum WebDriverType {
    FIREFOX,
    CHROME,
  }

  public void instantiateDriver() {
    log.info("About to init new web driver instance.");
    final WebDriver driver;
    val driverType = getDriverType();

    switch (driverType) {
      case FIREFOX:
        driver = createFirefoxDriver();
        break;
      case CHROME:
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        driver = createChromeDriver();
        break;
      default:
        throw new UnsupportedOperationException("Unsupported WebDriver type: " + driverType);
    }

    //driver.manage().window().maximize();

    Configuration.browser = driverType.name().toLowerCase();
    Configuration.startMaximized = true;
    Configuration.timeout = properties.waitTimeout() * THOUSAND;
    Configuration.pageLoadTimeout = properties.pageLoadTimeout() * THOUSAND;

    WebDriverRunner.setWebDriver(driver);
    threadWebDriver.set(driver);
  }

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

  /**
   * Driver type to use is defined by corresponding property value.
   * Defaults to Chrome if property is not set
   *
   * @return web driver type specified by the properties.
   */
  private WebDriverType getDriverType() {
    val driver = properties.browser();
    return Objects.isNull(driver)
      ? WebDriverType.CHROME
      : WebDriverType.valueOf(driver.toUpperCase());
  }

  private FirefoxDriver createFirefoxDriver() {
    // https://github.com/bonigarcia/webdrivermanager/
    final FirefoxOptions ops = new FirefoxOptions();
    //TODO: Configure as needed.
    ops.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
    io.github.bonigarcia.wdm.WebDriverManager.getInstance(WebDriverType.FIREFOX.name()).setup();

    return new FirefoxDriver(ops);
  }

  private ChromeDriver createChromeDriver() {
    final ChromeOptions ops = new ChromeOptions();
    ops.addArguments("--start-maximized");
    ops.addArguments("--dns-prefetch-disable");
    ops.addArguments("test-type");
    ops.addArguments("--headless");
    ops.setPageLoadStrategy(PageLoadStrategy.EAGER);
    ops.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
    io.github.bonigarcia.wdm.WebDriverManager.getInstance(WebDriverType.CHROME.name()).setup();

    return new ChromeDriver(ops);
  }
}
