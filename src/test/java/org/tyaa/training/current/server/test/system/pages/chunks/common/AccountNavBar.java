package org.tyaa.training.current.server.test.system.pages.chunks.common;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.tyaa.training.current.server.test.system.elements.HyperReference;
import org.tyaa.training.current.server.test.system.elements.TextBlock;
import org.tyaa.training.current.server.test.system.pages.BasePage;

@Getter
public class AccountNavBar extends BasePage {

    @FindBy(css = "div.account_nav_bar > span:nth-child(1)")
    private TextBlock promptTextBlock;

    @FindBy(css = "#sign-out")
    private HyperReference signOutHyperReference;

    public AccountNavBar(WebDriver driver) {
        super(driver);
    }
}