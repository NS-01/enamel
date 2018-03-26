package enamel.testCases;

import static org.junit.Assert.*;
import org.junit.*;

import enamel.ScenarioForm;

/**
 * 
 * @author Nisha, Jeremy, Tyler Test class to test Scenario Form GUI Further to
 *         feedback from professor, it was decided to do the GUI testing
 *         manually as it's beyond the scope of this course
 *
 */
public class testScenarioForm {

	private ScenarioForm sf;

	@Before
	public void setUp() throws Exception {
		sf = new ScenarioForm();
	}

	/*
	 * Test if the GUI is created
	 */
	@Test
	public void testGUICreation() {
		assertNotNull(sf);
	}

	/*
	 * Ensure the GUI is capable of having multiple iterations of itself
	 */
	@Test
	public void testMultipleGUIs() {
		ScenarioForm temp = new ScenarioForm();
		assertNotSame(sf, temp);

	}
}