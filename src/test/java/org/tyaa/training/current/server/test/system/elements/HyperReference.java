package org.tyaa.training.current.server.test.system.elements;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Setter
@Getter
public class HyperReference extends AbstractElement {

    private String title;

    public HyperReference(WebDriver driver, WebElement element) {
        super(driver, element);
    }
}
