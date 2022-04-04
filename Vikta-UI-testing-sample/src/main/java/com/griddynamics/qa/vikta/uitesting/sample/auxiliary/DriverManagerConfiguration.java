package com.griddynamics.qa.vikta.uitesting.sample.auxiliary;


import com.codeborne.selenide.WebDriverRunner;
import com.griddynamics.qa.vikta.uitesting.sample.config.TestSetupConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
@Slf4j
@RequiredArgsConstructor
@Configuration
public class DriverManagerConfiguration {

    private static final int THOUSAND = 1000;
    private final TestSetupConfiguration properties;

    @Bean
    public ThreadLocal<WebDriver> threadWebDriver(){
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

        com.codeborne.selenide.Configuration.browser = driverType.name().toLowerCase();
        com.codeborne.selenide.Configuration.timeout = properties.getWaitTimeout() * THOUSAND;
        com.codeborne.selenide.Configuration.pageLoadTimeout = properties.getPageLoadTimeout() * THOUSAND;

        WebDriverRunner.setWebDriver(driver);
        ThreadLocal<WebDriver> threadWebDriver = new ThreadLocal<>();
        threadWebDriver.set(driver);
        return threadWebDriver;
    }

    enum WebDriverType {
        FIREFOX,
        CHROME,
    }

    private FirefoxDriver createFirefoxDriver() {
        // https://github.com/bonigarcia/webdrivermanager/
        final FirefoxOptions ops = new FirefoxOptions();
        //TODO: Configure as needed.
        ops.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
        io.github.bonigarcia.wdm.WebDriverManager.getInstance(WebDriverType.FIREFOX.name()).setup();

        return new FirefoxDriver(ops);
    }

    /**
     * Driver type to use is defined by corresponding property value.
     * Defaults to Chrome if property is not set
     *
     * @return web driver type specified by the properties.
     */
    private WebDriverType getDriverType() {
        val driver = properties.getBrowser();
        return Objects.isNull(driver)
                ? WebDriverType.CHROME
                : WebDriverType.valueOf(driver.toUpperCase());
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
