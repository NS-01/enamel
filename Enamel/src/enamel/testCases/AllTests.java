package enamel.testCases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ testAuthoringView.class, testCard.class, testCTFP.class, testDataButton.class, testFTCP.class,
		testScenarioForm.class, testScenarioWriter.class, })
public class AllTests {
}
