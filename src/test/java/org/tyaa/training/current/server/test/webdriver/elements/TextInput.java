package org.tyaa.training.current.server.test.webdriver.elements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;

public class TextInput extends AbstractElement {

    public TextInput(WebDriver driver, WebElement element) {
        super(driver, element);
    }

    public void safeSendKeys(Duration timeOutInSeconds, CharSequence... charSequences) {
        safeAction(() -> {
            element.clear();
            element.sendKeys(charSequences);
        }, timeOutInSeconds);
    }

    public void safeSendKeysAndWaitForUpdate(
            WebElement elementToWaitForUpdate,
            Duration timeOutInSeconds,
            CharSequence... charSequences
    ) {
        performAndWaitForUpdate(
                driver,
                () -> {
                    element.clear();
                    element.sendKeys(charSequences);
                },
                elementToWaitForUpdate,
                timeOutInSeconds
        );
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        element.clear();
        element.sendKeys(charSequences);
    }
}
