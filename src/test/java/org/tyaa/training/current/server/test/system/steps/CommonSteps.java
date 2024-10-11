package org.tyaa.training.current.server.test.system.steps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Тогда;
import org.tyaa.training.current.server.test.system.WebDriverFactory;
import org.tyaa.training.current.server.test.system.facades.AbstractFacade;
import org.tyaa.training.current.server.test.system.facades.CommonFacade;
import org.tyaa.training.current.server.test.system.utils.FilePropertiesStore;
import org.tyaa.training.current.server.test.system.utils.interfaces.IPropertiesStore;

import java.lang.reflect.InvocationTargetException;

public class CommonSteps {

    private static final IPropertiesStore properties = new FilePropertiesStore();

    @Дано("WEB-приложение, открытое в браузере {string}")
    public void openWebApplication(String browser) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new CommonFacade(WebDriverFactory.getInstance(browser)).open(properties.getDevUrl());
    }

    @Тогда("В окне браузера {string} отображается страница {string}")
    public void checkPage(String browser, String page) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new AbstractFacade(WebDriverFactory.getInstance(browser)){}.getPageInstanceByName(page);
    }
}
