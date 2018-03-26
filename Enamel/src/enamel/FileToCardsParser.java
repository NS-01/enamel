package enamel;

import java.util.*;

import java.io.*;

/**
 *
 * @author Jeremy, Nisha, Tyler
 *
 *         Class to parse card data from a file.
 *
 */
public class FileToCardsParser {

	private Scanner fileScanner;
	private ArrayList<Card> cards;
	private String scenarioFilePath;
	private String initialPrompt;
	private String endingPrompt;
	private int numButtons;
	private int numCells;
	private int numLines;
	private int start;
	private boolean lastRemoved;
	private boolean inButton;
	private String fileLine;
	private int cardNum;
	private int buttonNum;
	private int currLineNum;
	private Card currCard;
	private ArrayList<DataButton> buttons;
	private ArrayList<BrailleCell> cells;
	private DataButton currButton;
	private BrailleCell currCell;

	public FileToCardsParser() {
		cards = new ArrayList<Card>();
		this.initialPrompt = "";
		this.endingPrompt = "";
	}

	public void setFile(String scenarioFile) {
		try {
			File f = new File(scenarioFile);
			fileScanner = new Scanner(f);
			String absolutePath = f.getAbsolutePath();
			scenarioFilePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
			checkNumLines(scenarioFile);
			checkButtonsAndCells();
			parse();
			checkLast();
			print();
		} catch (Exception e) {
			System.out.println("Incorrect File Name");
		}

	}

	public int getNumLines() {
		return this.numLines;
	}

	public void checkNumLines(String scenarioFile) {
		try {
			File f = new File(scenarioFile);
			Scanner numLineChecker = new Scanner(f);
			String fileLine;
			Boolean initialFound = false;
			while (numLineChecker.hasNextLine()) {
				fileLine = numLineChecker.nextLine();
				numLines++;
				if (fileLine.length() >= 2 && fileLine.substring(0, 2).equals("/~") && !initialFound) {
					start = numLines;
					initialFound = true;
				}
			}
			numLineChecker.close();
			System.out.println(numLines);
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}

		System.out.println(start);

	}

	public void checkButtonsAndCells() {
		if (fileScanner == null) {
			try {
				File f = new File("./FactoryScenarios/test.txt");
				fileScanner = new Scanner(f);
			} catch (Exception e) {
				System.out.println("Incorrect File Name");
			}

		}
		String fileLine = fileScanner.nextLine();
		if (fileLine.length() >= 6 && fileLine.substring(0, 4).equals("Cell")) {
			if (Character.isDigit(fileLine.charAt(5))) {
				numCells = Character.getNumericValue(fileLine.charAt(5));
			} else {
				throw new IllegalArgumentException();
			}

		} else {
			throw new IllegalArgumentException();
		}
		fileLine = fileScanner.nextLine();
		if (fileLine.length() >= 8 && fileLine.substring(0, 6).equals("Button")) {
			if (Character.isDigit(fileLine.charAt(7))) {
				numButtons = Character.getNumericValue(fileLine.charAt(7));
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}

	}

	public void parse() {
		setUp();
		while (fileScanner.hasNextLine()) {

			currLineNum++;
			fileLine = fileScanner.nextLine();
			if (fileLine.replace(" ", "").equals(""))
				continue;
			if (fileLine.length() >= 2 && fileLine.substring(0, 2).equals("/~")) {
				checkCommands();
			} else {

				if (inButton) {
					currButton.addText(fileLine);
				} else {

					currCard.addText(fileLine);
				}
			}
		}
		print();

	}

	private void checkCommands() {
		if (fileLine.length() >= 17 && fileLine.substring(0, 17).equals("/~disp-cell-pins:")) {
			dispCellPins();
		} else if (fileLine.length() >= 14 && fileLine.substring(0, 14).equals("/~disp-string:")) {
			dispString();

		} else if (fileLine.length() >= 8 && fileLine.substring(0, 8).equals("/~sound:")) {
			if (inButton) {
				currButton.setAudio(
						scenarioFilePath + File.separator + "AudioFiles" + File.separator + fileLine.substring(8));
				currButton.addText(fileLine);
			} else {
				currCard.setSound(
						scenarioFilePath + File.separator + "AudioFiles" + File.separator + fileLine.substring(8));
			}
		} else if (fileLine.equals("/~user-input")) {
			inButton = true;
		} else if (fileLine.equals("/~NEXTT") || fileLine.equals("/~reset-buttons")) {
			buttons.add(new DataButton(currButton));
			inButton = false;
			nextCard();
		}
		checkButtons();
		if (currLineNum == numLines) {
			buttons.clear();
			nextCard();
		}

	}

	private void dispString() {
		if (!inButton) {
			try {
				currCell = new BrailleCell();
				currCell.displayCharacter(fileLine.charAt(14));
				try {
					cells.set(Character.getNumericValue(fileLine.charAt(17)), currCell);
				} catch (Exception e) {
					cells.add(currCell);
				}

			} catch (Exception e) {
				System.out.println("Not a Char");
			}
		}
	}

	private void dispCellPins() {
		if (!inButton) {
			currCell = new BrailleCell();
			currCell.setPins(fileLine.substring(19));
			System.out.println("Bye");
			try {
				cells.set(Character.getNumericValue(fileLine.charAt(17)), currCell);
			} catch (Exception e) {
				while (cells.size() < Character.getNumericValue(fileLine.charAt(17))) {
					cells.add(new BrailleCell());
				}
				cells.add(currCell);
			}
		} else {
			currButton.addText("\n/Pins on " + fileLine.charAt(17) + ": " + fileLine.substring(19));
		}
	}

	private void checkButtons() {
		if (fileLine.equals("/~ONEE")) {
			buttonNum = 1;
			currButton = new DataButton(buttonNum);
		} else if (fileLine.equals("/~TWOO")) {
			buttonNum = 2;
			buttons.add(new DataButton(currButton));
			currButton = new DataButton(buttonNum);
		} else if (fileLine.equals("/~THREEE")) {
			buttonNum = 3;
			buttons.add(new DataButton(currButton));
			currButton = new DataButton(buttonNum);
		} else if (fileLine.equals("/~FOURR")) {
			buttonNum = 4;
			buttons.add(new DataButton(currButton));
			currButton = new DataButton(buttonNum);
		} else if (fileLine.equals("/~FIVEE")) {
			buttonNum = 5;
			buttons.add(new DataButton(currButton));
			currButton = new DataButton(buttonNum);
		} else if (fileLine.equals("/~SIXX")) {
			buttonNum = 6;
			buttons.add(new DataButton(currButton));
			currButton = new DataButton(buttonNum);
		}
	}

	private void nextCard() {
		currCard.setBList(new ArrayList<DataButton>(buttons));
		buttons.clear();
		currCard.setCells(new ArrayList<BrailleCell>(cells));
		cells.clear();
		cards.add(currCard);
		cardNum++;
		currCard = new Card(cardNum - 1, "Card " + cardNum, "notSure");
	}

	private void setUp() {
		inButton = false;
		cardNum = 1;
		buttonNum = 1;
		currLineNum = 2;
		while (currLineNum < start - 1 && fileScanner.hasNextLine()) {

			this.initialPrompt += fileScanner.nextLine();
			currLineNum++;

		}
		currCard = new Card(cardNum - 1, "Card " + cardNum, "notSure");
		buttons = new ArrayList<DataButton>(numButtons);
		cells = new ArrayList<BrailleCell>(numCells);
		currButton = new DataButton(buttonNum);
		currCell = new BrailleCell();
	}

	public int getCells() {
		return this.numCells;
	}

	public int getButtons() {
		return this.numButtons;
	}

	public ArrayList<Card> getCards() {
		return this.cards;
	}

	public String getInitial() {
		return this.initialPrompt;
	}

	public String getEnding() {
		return this.endingPrompt;
	}

	public void checkLast() {
		if (cards.size() > 0) {
			Card temp = cards.get(cards.size() - 1);
			if (temp.getCells().isEmpty()) {
				this.endingPrompt = temp.getText();
				cards.remove(temp);
				this.lastRemoved = true;
			} else {
				this.lastRemoved = false;
			}
		}

	}

	public boolean getLastRemoved() {
		return this.lastRemoved;
	}

	public void print() {
		System.out.println(cards.size());
		for (int i = 0; i < cards.size(); i++) {
			System.out.println("In card " + i + ":\n" + cards.get(i).getText() + "\n\n");
			ArrayList<DataButton> buttonList = cards.get(i).getButtonList();
			if (buttonList.size() > 0) {
				for (int j = 0; j < buttonList.size(); j++) {
					System.out.println("In Button" + j + ":" + buttonList.get(j).getID() + "\n"
							+ buttonList.get(j).getText() + "\n");
				}
			}

			System.out.println("\n\n\n");
		}
		for (int i = 0; i < cards.size(); i++) {
			System.out.println(i);
			for (int j = 0; j < 8; j++) {

				if (!cards.get(i).getCells().isEmpty()) {

					System.out.print(cards.get(i).getCells().get(0).getPinState(j) ? "1" : "0");
				}

			}
			System.out.println();
		}
	}

}