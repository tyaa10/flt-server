package org.tyaa.training.current.server.test.system.facades;

import org.tyaa.training.current.server.test.system.WebDriverFactory;

import java.lang.reflect.InvocationTargetException;

public class CommonFacade extends AbstractFacade {

    public CommonFacade(WebDriverFactory webDriverFactory) {
        super(webDriverFactory);
    }

    public CommonFacade open(String urlString) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        webDriverFactory.getDriver().get(urlString);
        webDriverFactory.getDriver().manage().window().maximize();
        return this;
    }
}
