package org.tyaa.training.current.server.test.system.cucumber;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@IncludeEngines("cucumber")
@SpringBootTest
public class CucumberTest {
}
