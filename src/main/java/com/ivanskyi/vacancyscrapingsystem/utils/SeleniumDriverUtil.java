package com.ivanskyi.vacancyscrapingsystem.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class SeleniumDriverUtil {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumDriverUtil.class);
    private static final String THE_SELENIUM_DRIVER_WAS_INITIALIZED_LOG_MESSAGE = "The Selenium driver was initialized.";

    private static String chromeDriverPath = "/home/ol/Документи/chromedriver";

    public static WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        logger.info(THE_SELENIUM_DRIVER_WAS_INITIALIZED_LOG_MESSAGE);
        return driver;
    }

    public static WebDriver getDriverWithoutImageRendering() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.images", 2);
        prefs.put("permissions.default.stylesheet", 2);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--blink-settings=imagesEnabled=false");
        options.addArguments("--blink-settings=imagesEnabled=false");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        logger.info(THE_SELENIUM_DRIVER_WAS_INITIALIZED_LOG_MESSAGE);
        return driver;
    }
}
