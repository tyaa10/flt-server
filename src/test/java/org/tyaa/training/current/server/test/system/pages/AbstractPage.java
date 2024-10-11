package org.tyaa.training.current.server.test.system.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.tyaa.training.current.server.test.system.CustomWebElementFieldDecorator;
import org.tyaa.training.current.server.test.system.pages.annotations.PageModel;

import java.time.Duration;

@PageModel
public abstract class AbstractPage {

    protected WebDriver driver;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(
                webDriver -> ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
        PageFactory.initElements(new CustomWebElementFieldDecorator(driver), this);
    }
}
