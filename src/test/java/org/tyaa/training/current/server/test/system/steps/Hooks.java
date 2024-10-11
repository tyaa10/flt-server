package org.tyaa.training.current.server.test.system.steps;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.Step;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestStep;
import org.tyaa.training.current.server.test.system.WebDriverFactory;
import org.tyaa.training.current.server.test.system.facades.AbstractFacade;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.tyaa.training.current.server.test.system.utils.ReflectionActions.extractField;

public class Hooks {

    @After
    public void afterScenario(Scenario scenario) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        TestCaseState testCaseState = (TestCaseState)extractField(scenario,"delegate");
        TestCase testCase = (TestCase)extractField(testCaseState,"testCase");
        List<TestStep> testSteps = (List<TestStep>) extractField(testCase,"testSteps");
        Step step = (Step)extractField(testSteps.get(0),"step");
        System.out.println("The first step's text: " + step.getText());
        String browser = step.getText().contains("'chrome'") ? "chrome" : "gecko";
        AbstractFacade abstractFacade = new AbstractFacade(WebDriverFactory.getInstance(browser)) {};
        abstractFacade.close();
    }
}
