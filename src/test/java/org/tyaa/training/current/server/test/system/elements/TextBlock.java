package org.tyaa.training.current.server.test.system.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TextBlock extends AbstractElement {

    public TextBlock(WebDriver driver, WebElement element) {
        super(driver, element);
    }

    public String safeGetText(Duration timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }
}
