package org.tyaa.training.current.server.test.system.facades;

import org.openqa.selenium.WebDriver;
import org.tyaa.training.current.server.test.system.WebDriverFactory;
import org.tyaa.training.current.server.test.system.pages.AbstractPage;
import org.tyaa.training.current.server.test.system.pages.annotations.PageModel;
import org.tyaa.training.current.server.test.system.utils.ReflectionActions;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public abstract class AbstractFacade {

    protected final WebDriverFactory webDriverFactory;

    public AbstractFacade(WebDriverFactory webDriverFactory) {
        this.webDriverFactory = webDriverFactory;
    }

    public AbstractPage getPageInstance()
            throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException
    {
        return new AbstractPage(webDriverFactory.getDriver()) {};
    }

    public AbstractFacade close() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        webDriverFactory.getDriver().close();
        return this;
    }

    public AbstractPage getPageInstanceByName(String name)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException, ClassNotFoundException
    {
        Set<Class> classes = ReflectionActions.getAllClassesFromPackage("org.tyaa.training.current.server.test.system.pages");
        return (AbstractPage) classes.stream()
                .filter(page -> page.isAnnotationPresent(PageModel.class))
                .filter(page -> {
                    try {
                        return PageModel.class.getDeclaredMethod("name")
                                .invoke(page.getAnnotation(PageModel.class), null).equals(name);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }).findFirst()
                .get()
                .getConstructor(WebDriver.class)
                .newInstance(webDriverFactory.getDriver());
    }
}
