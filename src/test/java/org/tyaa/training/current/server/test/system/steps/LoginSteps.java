package org.tyaa.training.current.server.test.system.steps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import org.tyaa.training.current.server.test.system.WebDriverFactory;
import org.tyaa.training.current.server.test.system.facades.LoginFacade;

import java.lang.reflect.InvocationTargetException;

public class LoginSteps {

    @Когда("Пользователь выполняет попытку входа с логином {string} и паролем {string} в браузере {string}")
    public void login(String login, String password, String browser)
            throws InvocationTargetException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException {
        new LoginFacade(WebDriverFactory.getInstance(browser)).login(login, password);
    }
}
