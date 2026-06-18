package br.com.backend.e2e.config;

import br.com.backend.e2e.factory.Browser;
import br.com.backend.e2e.factory.BrowserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseE2ETest {

    protected WebDriver driver;

    protected WebDriverWait wait;

    @BeforeEach
    void setup() {
        String browserName = System.getProperty("browser", "firefox");

        Browser browser = Browser.valueOf(browserName.toUpperCase());

        driver = BrowserFactory.create(browser);

        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
