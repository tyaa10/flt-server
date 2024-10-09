package org.tyaa.training.current.server.test.webdriver.utils.interfaces;

import java.util.Map;

/**
 * Абстракция хранилища свойств
 * */
public interface IPropertiesStore {

    Map<String, String> getSupportedBrowsers();
    Map.Entry<String, String> getDefaultBrowser() throws Exception;
    String getOs();
    Integer getImplicitlyWaitSeconds();
    String getDevUrl();
}
