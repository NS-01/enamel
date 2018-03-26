package enamel.testCases;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.*;

import enamel.AuthoringViewer;
import enamel.Card;

public class testAuthoringView {

	private AuthoringViewer aw;
	private ArrayList<Card> array;
	private Card card;

	/*
	 * Initial Setup
	 */
	@Before
	public void setUp() {
		card = new Card(0, "", "");
		array = new ArrayList<Card>();
		array.add(card);
		aw = new AuthoringViewer(0, 0, array, null, null);
	}

	/*
	 * Test if the GUI is created
	 */
	@Test
	public void testGUICreation() {
		assertNotNull(aw);
	}

	/*
	 * Ensure the GUI is capable of having multiple iterations of itself
	 */
	@Test
	public void testMultipleGUIs() {
		AuthoringViewer fw = new AuthoringViewer(0, 0, array, null, null);
		assertNotSame(aw, fw);
	}
}