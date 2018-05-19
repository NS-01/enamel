package enamel.testCases;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import enamel.BrailleCell;
import enamel.Card;
import enamel.CardsToFileParser;
import enamel.DataButton;

public class testCTFP {

	ArrayList<Card> cards = new ArrayList<>();

	@Test
	public void testCtor() {
		CardsToFileParser cTFP = new CardsToFileParser(cards, 1, 2, "Hello", "Bye");
		assertEquals("", cTFP.getText());
	}

	@Test
	public void testCellButton() {
		Card test = new Card(0, "Card1", "Hi", true);
		cards.add(test);
		CardsToFileParser cTFP = new CardsToFileParser(cards, 1, 2, "Hello", "Bye");
		cTFP.createBody();
		String text = "Cell 2\nButton 1\n\n/~user-input\n\n/~NEXTT\n/~disp-cell-clear:0\n/~disp-cell-clear:1";
		assertEquals(text, cTFP.getText());
		CardsToFileParser cTFP2 = new CardsToFileParser(cards, 5, 1, "HI", "Good Bye\nHave a Nice Day");
		cTFP2.createBody();
		text = "Cell 1\nButton 5\n\n/~user-input\n\n/~NEXTT\n/~disp-cell-clear:0";
		assertEquals(text, cTFP2.getText());
	}

	@Test
	public void testWriteButtons() {
		CardsToFileParser cTFP = new CardsToFileParser(cards, 2, 3, "Hello", "Bye");
		Card test = new Card(0, "Card1", "Hi", true);//*********************************************
		ArrayList<DataButton> bList = new ArrayList<>();
		DataButton b1 = new DataButton(0);
		b1.addText("hello");
		bList.add(b1);
		DataButton b2 = new DataButton(1);
		b2.addText("bye");
		bList.add(b2);
		test.setBList(bList);
		String returned = cTFP.writeCard(test);
		System.out.println(returned);
		String result = "";
		result += "\n/~skip-button:" + 0 + " ONEE";
		result += "\n/~skip-button:" + 1 + " TWOO";
		result += "\n/~user-input";
		result += "\n/~ONEE";
		result += "\nhello";
		result += "\n/~skip:NEXTT";
		result += "\n/~TWOO";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n\n/~NEXTT";
		assertEquals(result, returned); // Test basic button

		b1.setAudio("correct.wav");
		b1.setText("/~sound:correct.wav\nhello");
		returned = cTFP.writeCard(test);
		System.out.println(returned);
		result = "";
		result += "\n/~skip-button:" + 0 + " ONEE";
		result += "\n/~skip-button:" + 1 + " TWOO";
		result += "\n/~user-input";
		result += "\n/~ONEE";
		result += "\n/~sound:correct.wav";
		result += "\nhello";
		result += "\n/~skip:NEXTT";
		result += "\n/~TWOO";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n\n/~NEXTT";
		assertEquals(result, returned); // button with sound file only

		b1.setAudio(".\\FactoryScenarios\\correct.wav");
		returned = cTFP.writeCard(test);
		System.out.println(returned);
		result = "";
		result += "\n/~skip-button:" + 0 + " ONEE";
		result += "\n/~skip-button:" + 1 + " TWOO";
		result += "\n/~user-input";
		result += "\n/~ONEE";
		result += "\n/~sound:correct.wav";
		result += "\nhello";
		result += "\n/~skip:NEXTT";
		result += "\n/~TWOO";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n\n/~NEXTT";
		assertEquals(result, returned); // button with sound file with path

		b1.setText("hello\n/Pins on 1: 10101010");
		returned = cTFP.writeCard(test);
		System.out.println(returned);
		result = "";
		result += "\n/~skip-button:" + 0 + " ONEE";
		result += "\n/~skip-button:" + 1 + " TWOO";
		result += "\n/~user-input";
		result += "\n/~ONEE";
		result += "\nhello";
		result += "\n/~disp-cell-clear:0";
		result += "\n/~disp-cell-pins:0 10101010";
		result += "\n/~skip:NEXTT";
		result += "\n/~TWOO";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n\n/~NEXTT";
		assertEquals(result, returned);

		b1.setText("hello\n/Pins on 0: 1010101a");
		returned = cTFP.writeCard(test);
		System.out.println(returned);
		result = "";
		result += "\n/~skip-button:" + 0 + " ONEE";
		result += "\n/~skip-button:" + 1 + " TWOO";
		result += "\n/~user-input";
		result += "\n/~ONEE";
		result += "\nhello";
		result += "\n/~skip:NEXTT";
		result += "\n/~TWOO";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n\n/~NEXTT";
		assertEquals(result, returned);// doesnt print if there is something other than 1's and 0's ***having the pop
										// up is correct
	}


	@Test
	public void testPrompt() {
		CardsToFileParser cTFP = new CardsToFileParser(cards, 2, 3, "Hello", "Bye");
		Card test = new Card(0, "Card1", "Hi", true);//*********************************************
		String returned = cTFP.writeCard(test);
		System.out.println(returned);
		String result = "";
		result += "\n/~user-input";
		result += "\n\n/~NEXTT";
		assertEquals(result, returned); // null prompt

		test.setText("");
		returned = cTFP.writeCard(test);
		System.out.println(returned);
		result = "";
		result += "\n" + "";
		result += "\n/~user-input";
		result += "\n\n/~NEXTT";
		assertEquals(result, returned); // empty prompt

		test.setText("Hello World\n/Play sound file correct.wav\n/Wait for 2 second(s)\n/Clear all pins\n/Display character a on cell 2\n/Display string asdf");
		returned = cTFP.writeCard(test);
		System.out.println(returned);
		result = "";
		result += "\n" + "Hello World";
		result += "\n/~sound:correct.wav";
		result += "\n/~pause:2";
		result += "\n/~disp-clearAll";
		result += "\n/~disp-cell-char:1 a";
		result += "\n/~disp-string:asdf";
		result += "\n/~user-input";
		result += "\n\n/~NEXTT";
		assertEquals(result, returned); // basic prompt
	}

	@Test
	public void testWriteCard() {
		CardsToFileParser cTFP = new CardsToFileParser(cards, 6, 3, "Hello", "Bye");
		Card test = new Card(0, "Card1", "Hi", true);//*********************************************
		test.setText("Hello World");
		test.addText("/Pins on 1: 10101010");
		test.addText("/Pins on 2: 11111111");

		ArrayList<BrailleCell> cList = new ArrayList<>();
		BrailleCell c1 = new BrailleCell();
		c1.setPins("10101010");
		cList.add(c1);
		BrailleCell c2 = new BrailleCell();
		c2.setPins("11111111");
		cList.add(c2);
		test.setCells(cList);

		ArrayList<DataButton> bList = new ArrayList<>();
		DataButton b1 = new DataButton(0);
		b1.addText("hello");
		bList.add(b1);
		DataButton b2 = new DataButton(1);
		b2.addText("bye");
		bList.add(b2);
		DataButton b3 = new DataButton(2);
		b3.addText("bye");
		bList.add(b3);
		DataButton b4 = new DataButton(3);
		b4.addText("bye");
		bList.add(b4);
		DataButton b5 = new DataButton(4);
		b5.addText("bye");
		bList.add(b5);
		DataButton b6 = new DataButton(5);
		b6.addText("bye");
		bList.add(b6);
		test.setBList(bList);

		String returned = cTFP.writeCard(test);
		System.out.println(returned);
		
		String result = "\nHello World";
		result += "\n/~disp-cell-clear:0";
		result += "\n/~disp-cell-pins:0 10101010";
		result += "\n/~disp-cell-clear:1";
		result += "\n/~disp-cell-pins:1 11111111";
		result += "\n/~skip-button:" + 0 + " ONEE";
		result += "\n/~skip-button:" + 1 + " TWOO";
		result += "\n/~skip-button:" + 2 + " THREEE";
		result += "\n/~skip-button:" + 3 + " FOURR";
		result += "\n/~skip-button:" + 4 + " FIVEE";
		result += "\n/~skip-button:" + 5 + " SIXX";
		result += "\n/~user-input";
		result += "\n/~ONEE";
		result += "\nhello";
		result += "\n/~skip:NEXTT";
		result += "\n/~TWOO";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n/~THREEE";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n/~FOURR";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n/~FIVEE";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n/~SIXX";
		result += "\nbye";
		result += "\n/~skip:NEXTT";
		result += "\n\n/~NEXTT";
		assertEquals(result, returned); // basic combination of prompt cells and buttons
	}

}
