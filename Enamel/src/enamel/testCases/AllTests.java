package enamel.testCases;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ testAuthoringViewerTest.class, testCard.class, testCTFP.class, testDataButton.class, testFTCP.class,
		testScenarioForm.class, testScenarioWriter.class, })
public class AllTests {
}
