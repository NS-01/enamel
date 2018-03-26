package enamel.testCases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.*;

import enamel.Card;
import enamel.FileToCardsParser;

public class testFTCP {

	String currPath;

	@Before
	public void setUp() throws IOException {
		currPath = new java.io.File("./FactoryScenarios/test.txt").getCanonicalPath();
	}

	@Test
	public void testCtor() {
		FileToCardsParser f = new FileToCardsParser();
		assertEquals(new ArrayList<Card>(), f.getCards());
		assertEquals("", f.getInitial());
		assertEquals("", f.getEnding());
	}
	
	@Test
	public void testCheckNumLines() throws IOException {
		FileWriter write = new FileWriter(currPath, false);
		PrintWriter print = new PrintWriter(write);
		String text = "Cell 1\r\n" + "Button 4\r\n" + "\r\n" + "Orientation questions. Questions with two pins\r\n"
				+ "/~disp-cell-clear:0\r\n" + "/~disp-cell-pins:0 10100000\r\n" + "Here's the first question:\r\n"
				+ "What pins are up right now? Are they the pins 1 and 3, or are they the pins 4 and 6?\r\n"
				+ "Press the button 1 for pins 1 and 3, or press button 2 for pins 4 and 6.\r\n"
				+ "/~skip-button:0 ONEE\r\n" + "/~skip-button:1 TWOO\r\n" + "/~user-input\r\n" + "/~ONEE\r\n"
				+ "/~sound:correct.wav\r\n"
				+ "That's correct! The pins being displayed are 1 and 3, which are the top and bottom pins on the left side of the cell.\r\n"
				+ "/~skip:NEXTT\r\n" + "/~TWOO\r\n" + "/~sound:wrong.wav\r\n"
				+ "I'm sorry! That's incorrect. The pins being displayed are 1 and 3, which are the top and bottom pins on the left side of the cell, \r\n"
				+ "and not 4 and 6,\r\n" + "/~disp-cell-clear:0\r\n" + "/~disp-cell-pins:0 00010100\r\n"
				+ "which are on the right side of the cell.\r\n" + "/~skip:NEXTT";
		print.printf("%s" + "%n", text);
		print.close();
		FileToCardsParser f = new FileToCardsParser();
		f.checkNumLines(currPath);
		assertEquals(24, f.getNumLines()); // basic

		write = new FileWriter(currPath, false);
		print = new PrintWriter(write);
		text = "Cell 1\r\n" + "Button 4\r\n" + "\r\n" + "Orientation questions. Questions with two pins\r\n"
				+ "/~disp-cell-clear:0\r\n" + "/~disp-cell-pins:0 10100000\r\n" + "Here's the first question:\r\n"
				+ "What pins are up right now? Are they the pins 1 and 3, or are they the pins 4 and 6?\r\n"
				+ "Press the button 1 for pins 1 and 3, or press button 2 for pins 4 and 6.\r\n"
				+ "/~skip-button:0 ONEE\r\n" + "/~skip-button:1 TWOO\r\n" + "/~user-input\r\n" + "/~ONEE\r\n"
				+ "/~sound:correct.wav\r\n"
				+ "That's correct! The pins being displayed are 1 and 3, which are the top and bottom pins on the left side of the cell.\r\n"
				+ "/~skip:NEXTT\r\n" + "/~TWOO";
		print.printf("%s" + "%n", text);
		print.close();
		f = new FileToCardsParser();
		f.checkNumLines(currPath);
		assertEquals(17, f.getNumLines()); // basic

		write = new FileWriter(currPath, false);
		print = new PrintWriter(write);
		text = "";
		print.printf("%s" + "%n", text);
		print.close();
		f = new FileToCardsParser();
		f.checkNumLines(currPath);
		assertEquals(1, f.getNumLines()); // empty

		write = new FileWriter(currPath, false);
		print = new PrintWriter(write);
		text = null;
		print.printf("%s" + "%n", text);
		print.close();
		f = new FileToCardsParser();
		f.checkNumLines(currPath);
		assertEquals(1, f.getNumLines()); // null
	}

	@Test
	public void testCheckButtonsAndCells() throws IOException {
		FileWriter write = new FileWriter(currPath, false);
		PrintWriter print = new PrintWriter(write);
		String text = "Cell 1\r\n" + "Button 4";
		print.printf("%s" + "%n", text);
		print.close();
		FileToCardsParser f = new FileToCardsParser();
		f.checkButtonsAndCells();
		assertEquals(1, f.getCells()); // basic
		assertEquals(4, f.getButtons());

		write = new FileWriter(currPath, false);
		print = new PrintWriter(write);
		text = "Cell 5\r\n" + "Button 7\r\n" + "\r\n" + "Orientation questions. Questions with two pins\r\n"
				+ "/~disp-cell-clear:0\r\n" + "/~disp-cell-pins:0 10100000\r\n" + "Here's the first question:\r\n"
				+ "What pins are up right now? Are they the pins 1 and 3, or are they the pins 4 and 6?\r\n"
				+ "Press the button 1 for pins 1 and 3, or press button 2 for pins 4 and 6.\r\n"
				+ "/~skip-button:0 ONEE\r\n" + "/~skip-button:1 TWOO\r\n" + "/~user-input\r\n" + "/~ONEE\r\n"
				+ "/~sound:correct.wav\r\n"
				+ "That's correct! The pins being displayed are 1 and 3, which are the top and bottom pins on the left side of the cell.\r\n"
				+ "/~skip:NEXTT\r\n" + "/~TWOO";
		print.printf("%s" + "%n", text);
		print.close();
		f = new FileToCardsParser();
		f.checkButtonsAndCells();
		assertEquals(5, f.getCells()); // basic longer
		assertEquals(7, f.getButtons());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckButtonsAndCellsException() throws IOException {
		FileWriter write = new FileWriter(currPath, false);
		PrintWriter print = new PrintWriter(write);
		String text = "";
		print.printf("%s" + "%n", text);
		print.close();
		FileToCardsParser f = new FileToCardsParser();
		f.checkButtonsAndCells();
		fail("Should throw exception");// empty
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckButtonsAndCellsException2() throws IOException {
		FileWriter write = new FileWriter(currPath, false);
		PrintWriter print = new PrintWriter(write);
		String text = null;
		print.printf("%s" + "%n", text);
		print.close();
		FileToCardsParser f = new FileToCardsParser();
		f.checkButtonsAndCells();
		fail("Should throw exception");// null
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCheckButtonsAndCellsException3() throws IOException {
		FileWriter write = new FileWriter(currPath, false);
		PrintWriter print = new PrintWriter(write);
		String text = "Cell a\n Button d";
		print.printf("%s" + "%n", text);
		print.close();
		FileToCardsParser f = new FileToCardsParser();
		f.checkButtonsAndCells();
		fail("Should throw exception");// letter
	}

	@Test
	public void testCheckLast() {
		FileToCardsParser f = new FileToCardsParser();

		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_1.txt");
		assertFalse(f.getLastRemoved()); // should be false for scenario 1

		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_2.txt");
		assertTrue(f.getLastRemoved()); // should be true for 2 and 3

		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_3.txt");
		assertTrue(f.getLastRemoved());
	}

	@Test
	public void testInitial() {
		FileToCardsParser f = new FileToCardsParser();

		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_1.txt");
		assertEquals("Directional orientation", f.getInitial());

		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_2.txt");
		assertEquals("Orientation questions. Questions with two pins", f.getInitial());

		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_3.txt");
		assertEquals("Simple alphabet questions", f.getInitial());
	}

	@Test
	public void testEnding() throws IOException {
		FileToCardsParser f = new FileToCardsParser();

		FileWriter write = new FileWriter(currPath, false);
		PrintWriter print = new PrintWriter(write);
		String text = "Cell 1\r\n" + "Button 4";
		print.printf("%s" + "%n", text);
		print.close();

		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/test.txt");
		String test1 = f.getEnding();
		System.out.println("asdf " + test1);
		assertEquals("", f.getEnding()); // test doesn't have ending

		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_2.txt");
		assertEquals("That's all for now! Thank you for playing two pin questions!\n" + "Have a nice day!",
				f.getEnding());

		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_3.txt");
		assertEquals("That's the end!", f.getEnding());

	}

	@Test
	public void testGetCards() {
		FileToCardsParser f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_2.txt");
		ArrayList<Card> cards = f.getCards();

		assertEquals(
				"Here's the first question:\n"
						+ "What pins are up right now? Are they the pins 1 and 3, or are they the pins 4 and 6?\n"
						+ "Press the button 1 for pins 1 and 3, or press button 2 for pins 4 and 6.",
				cards.get(0).getText());
		assertEquals(
				"/~sound:correct.wav\nThat's correct! The pins being displayed are 1 and 3, which are the top and bottom pins on the left side of the cell.",
				cards.get(0).getButtonList().get(0).getText());
		assertEquals(
				"/~sound:wrong.wav\nI'm sorry! That's incorrect. The pins being displayed are 1 and 3, which are the top and bottom pins on the left side of the cell, \n"
						+ "and not 4 and 6,\n" + "\n" + "/Pins on 0: 00010100\n"
						+ "which are on the right side of the cell.",
				cards.get(0).getButtonList().get(1).getText());
		assertEquals(true, cards.get(0).getCells().get(0).getPinState(0));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(1));
		assertEquals(true, cards.get(0).getCells().get(0).getPinState(2));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(3));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(4));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(5));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(6));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(7));// testing card 0 (Scenario_2.txt)

		assertEquals(
				"Here's the third question:\n"
						+ "What pins are up right now? Are they the pins 1 and 4, or are they the pins 3 and 6?\n"
						+ "Press the button 1 for pins 1 and 4, or press button 2 for pins 3 and 6.",
				cards.get(2).getText());
		assertEquals("/~sound:correct.wav\nThat's correct! The pins being displayed are 3 and 6, which are the two bottom pins.",
				cards.get(2).getButtonList().get(1).getText());
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(0));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(1));
		assertEquals(true, cards.get(2).getCells().get(0).getPinState(2));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(3));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(4));
		assertEquals(true, cards.get(2).getCells().get(0).getPinState(5));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(6));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(7)); // testing card 2 (Scenario_2.txt)

		// Scenario_1.txt
		f = new FileToCardsParser();
		f.setFile("./FactoryScenarios/Scenario_1.txt");
		cards = f.getCards();

		assertEquals("These are pins 1, 2 and 3, the 3 pins on the left side. \n" + "Press button 1 to continue.",
				cards.get(0).getText());

		assertEquals("", cards.get(0).getButtonList().get(0).getText());

		assertEquals(true, cards.get(0).getCells().get(0).getPinState(0));
		assertEquals(true, cards.get(0).getCells().get(0).getPinState(1));
		assertEquals(true, cards.get(0).getCells().get(0).getPinState(2));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(3));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(4));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(5));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(6));
		assertEquals(false, cards.get(0).getCells().get(0).getPinState(7));// testing card 0 (Scenario_2.txt)

		assertEquals("These are pins 1 and 2, the top two pins on the left side. \n" + "Press button 1 to continue.",
				cards.get(2).getText());
		assertEquals("", cards.get(2).getButtonList().get(0).getText());
		
		assertEquals(true, cards.get(2).getCells().get(0).getPinState(0));
		assertEquals(true, cards.get(2).getCells().get(0).getPinState(1));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(2));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(3));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(4));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(5));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(6));
		assertEquals(false, cards.get(2).getCells().get(0).getPinState(7)); // testing card 2 (Scenario_2.txt)
	}

}
