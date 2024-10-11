package org.tyaa.training.current.server.test.system.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.tyaa.training.current.server.test.system.elements.TextBlock;

@Getter
public class BasePage extends AbstractPage {

    @FindBy(css = "h1")
    private TextBlock titleTextBlock;

    @FindBy(css = "h2")
    private TextBlock subTitleTextBlock;

    public BasePage(WebDriver driver) {
        super(driver);
    }
}
