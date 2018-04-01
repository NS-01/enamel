package enamel.testCases;

import static org.junit.Assert.*;
import java.util.ArrayList;

import org.junit.*;

import enamel.BrailleCell;
import enamel.Card;
import enamel.DataButton;

/**
 * 
 * @author Jeremy, Nisha, Tyler
 * 
 *         This Class tests constructors and methods of Card class.
 *
 */

public class testCard {

	@Test
	public void testCtorAndGets() {
		Card test1 = new Card(10, "Card 1", "A", false);
		assertEquals(test1.getId(), 10);
		assertEquals(test1.getName(), "Card 1");
		assertEquals(test1.getType(), "A");
		assertEquals(test1.getButtonList(), new ArrayList<DataButton>());
		assertEquals(test1.getCells(), new ArrayList<BrailleCell>());
		assertEquals(test1.getText(), null);
	}

	@Test
	public void testSetText() {
		Card test1 = new Card(10, "Card 1", "A", false);
		test1.setText("hellO");
		assertEquals(test1.getText(), "hellO");
		test1.setText("");
		assertEquals(test1.getText(), "");
		test1.setText("-1");
		assertEquals(test1.getText(), "-1");
		test1.setText(null);
		assertEquals(test1.getText(), null);
	}

	@Test
	public void testAddText() {
		Card test1 = new Card(10, "Card 1", "A", false);//*********************************************
		test1.setText("hellO");
		test1.addText("");
		assertEquals(test1.getText(), "hellO\n");
		test1.addText("test");
		assertEquals(test1.getText(), "hellO\n\ntest");
		test1.addText(null);
		assertEquals(test1.getText(), "hellO\n\ntest\n" + null);
		test1.setText(null);
		test1.addText("Hello");
		assertEquals(test1.getText(), "Hello");
	}

	@Test
	public void testSetSound() {
		Card test1 = new Card(10, "Card 1", "A", false);//*********************************************
		test1.setSound("Hello.wav");
		assertEquals(test1.getSound(), "Hello.wav");
		test1.setSound(null);
		assertEquals(test1.getSound(), null);
		test1.setSound("");
		assertEquals(test1.getSound(), "");
		test1.setSound("test");
		assertEquals(test1.getSound(), "test");
	}

	@Test
	public void testSetBList() {
		Card test1 = new Card(10, "Card 1", "A", false);//*********************************************
		assertEquals(test1.getButtonList(), new ArrayList<DataButton>());
		ArrayList<DataButton> bList = new ArrayList<>();
		test1.setBList(bList);
		assertEquals(test1.getButtonList(), bList);
		bList.add(new DataButton(1));
		assertEquals(test1.getButtonList(), bList);
		ArrayList<DataButton> bList2 = new ArrayList<>();
		bList2.add(new DataButton(10));
		bList2.add(new DataButton(3));
		test1.setBList(bList2);
		assertEquals(test1.getButtonList(), bList2);
	}

	@Test
	public void testSetCells() {
		Card test1 = new Card(10, "Card 1", "A", false);//*********************************************
		assertEquals(test1.getCells(), new ArrayList<BrailleCell>());
		ArrayList<BrailleCell> cellList = new ArrayList<>();
		test1.setCells(cellList);
		assertEquals(test1.getCells(), cellList);
		cellList.add(new BrailleCell());
		assertEquals(test1.getCells(), cellList);
		ArrayList<BrailleCell> cellList2 = new ArrayList<>();
		cellList2.add(new BrailleCell());
		cellList2.add(new BrailleCell());
		test1.setCells(cellList2);
		assertEquals(test1.getCells(), cellList2);
	}

	@Test
	public void testSetName() {
		Card test1 = new Card(10, "Card 1", "A", false); //*********************************************
		test1.setName("Hello");
		assertEquals(test1.getName(), "Hello");
		test1.setName(null);
		assertEquals(test1.getName(), null);
		test1.setName("");
		assertEquals(test1.getName(), "");
		test1.setName("test");
		assertEquals(test1.getName(), "test");
	}
}