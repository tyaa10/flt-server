package org.tyaa.training.current.server.test.system.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.tyaa.training.current.server.test.system.elements.Button;
import org.tyaa.training.current.server.test.system.elements.TextBlock;
import org.tyaa.training.current.server.test.system.elements.TextInput;
import org.tyaa.training.current.server.test.system.pages.annotations.PageModel;

@Getter
@PageModel(name = "вход")
public class LoginPage extends BasePage {

    @FindBy(id = "username")
    private TextInput usernameTextInput;

    @FindBy(id = "password")
    private TextInput passwordTextInput;

    @FindBy(css = "#sign-in_form > button")
    private Button submitButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }
}
