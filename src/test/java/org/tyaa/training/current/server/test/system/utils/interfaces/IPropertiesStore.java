package org.tyaa.training.current.server.test.system.utils.interfaces;

import java.time.Duration;
import java.util.Map;

/**
 * Абстракция хранилища свойств
 * */
public interface IPropertiesStore {

    Map<String, String> getSupportedBrowsers();
    Map.Entry<String, String> getDefaultBrowser() throws Exception;
    String getOs();
    Duration getImplicitlyWaitSeconds();
    String getDevUrl();
}
