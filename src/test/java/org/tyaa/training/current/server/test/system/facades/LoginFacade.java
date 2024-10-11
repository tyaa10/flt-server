package org.tyaa.training.current.server.test.system.facades;

import org.tyaa.training.current.server.test.system.WebDriverFactory;
import org.tyaa.training.current.server.test.system.pages.LoginPage;
import org.tyaa.training.current.server.test.system.utils.FilePropertiesStore;
import org.tyaa.training.current.server.test.system.utils.interfaces.IPropertiesStore;

import java.lang.reflect.InvocationTargetException;

public class LoginFacade extends AbstractFacade {

    private static final IPropertiesStore properties = new FilePropertiesStore();

    public LoginFacade(WebDriverFactory webDriverFactory) {
        super(webDriverFactory);
    }

    public LoginFacade login(String login, String password)
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final LoginPage loginPage = new LoginPage(webDriverFactory.getDriver());
        loginPage.getUsernameTextInput().safeSendKeys(properties.getImplicitlyWaitSeconds(), login);
        loginPage.getPasswordTextInput().safeSendKeys(properties.getImplicitlyWaitSeconds(), password);
        loginPage.getSubmitButton().safeClick(properties.getImplicitlyWaitSeconds());
        return this;
    }
}
