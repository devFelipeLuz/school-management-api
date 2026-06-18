package br.com.backend.e2e.config;

import org.openqa.selenium.WebDriver;

public class BaseE2ETest {

    private final WebDriver driver;

    public BaseE2ETest(WebDriver driver) {
        this.driver = driver;
    }
}
