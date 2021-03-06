package enamel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * 
 * @author Jeremy, Nisha, Tyler
 * 
 *         Class to parse card data to a file. New Updates.
 *
 */
public class CardsToFileParser {

	private ArrayList<Card> cards;
	private String scenarioFilePath;
	private String initialPrompt;
	private String endingPrompt;
	private int numButtons;
	private int numCells;
	private String body;

	public CardsToFileParser(ArrayList<Card> cards, int numButtons, int numCells, String initialPrompt,
			String endingPrompt) {
		this.cards = cards;
		this.numButtons = numButtons;
		this.numCells = numCells;
		this.initialPrompt = initialPrompt;
		this.endingPrompt = endingPrompt;
		this.body = "";
	}

	// creates the entire file as one string
	public void createBody() {
		body += "Cell " + numCells;
		body += "\nButton " + numButtons;
//		body += "\n" + initialPrompt;
		// write stuff for each card
		for (Card currCard : cards) {
			body += "\n" + writeCard(currCard);
		}
		// body += "\n" + endingPrompt;
		// clears cells at end
		for (int i = 0; i < this.numCells; i++) {
			body += "\n/~disp-cell-clear:" + i + "";
		}
		System.out.println(body);
	}

	/**
	 * writes the stuff for each card
	 * 
	 * @param currCard
	 *            - the card
	 * @return
	 */
	public String writeCard(Card currCard) {
		// clear cells at start
		String result = "";
		// String result = "/~disp-cell-clear:0";
		// for (int i = 1; i < this.numCells; i++) {
		// result += "\n/~disp-cell-clear:" + i;
		// }
		// result = writeCells(currCard, result);
		System.out.println(currCard.getText());
		result = writeTextAndCheckCells(currCard, result, currCard.getText(), -1);

		if (currCard.getEnbled()) {
			result = writeButtons(currCard, result);
		}

		result += "\n\n/~NEXTT";

		return result;
	}

	private String writeButtons(Card currCard, String result) {
		ArrayList<DataButton> buttons = currCard.getButtonList();

		result = writeInput(result, buttons);
		for (int i = 0; i < buttons.size(); i++) {
			switch (i) {
			case (0):
				result += "\n/~ONEE";
				break;
			case (1):
				result += "\n/~TWOO";
				break;
			case (2):
				result += "\n/~THREEE";
				break;
			case (3):
				result += "\n/~FOURR";
				break;
			case (4):
				result += "\n/~FIVEE";
				break;
			case (5):
				result += "\n/~SIXX";
				break;
			}

			//Still not sure how we are doing audio. Are we adding text
			//onto the editor for them or are we storing it into the 
			//audio field in the button
			//which way do we want???
			//
			// been thinking list of actions which is editable on double click.

			String audioPath = buttons.get(i).getAudio();
			// result = checkAudio(result, audioPath);
			result = writeTextAndCheckCells(currCard, result, buttons.get(i).getText(), i);
			result += "\n/~skip:NEXTT";
		}

		return result;
	}

	

	private String writeTextAndCheckCells(Card currCard, String result, String input, int buttonNum) {
		System.out.println(input);
		if (input == null) {
			return result;
		}
		String[] arr = input.split("\n");
		for (int j = 0; j < arr.length; j++) {
			// this was our previous indicator
			// currently we are just writing the command for them
			// which way do we want???
			// this whole if statement is just checking for our previous indicator
			if (arr[j].length() == 20 && arr[j].substring(0, 9).equals("/Pins on ")) {
				boolean checkNumber = true;
				for (int k = 0; k < arr[j].substring(12).length(); k++) {
					if (arr[j].substring(12).charAt(k) != '0' && arr[j].substring(12).charAt(k) != '1') {
						System.out.println("hi");
						checkNumber = false;
					}
				}
				if (checkNumber) {
					result += "\n/~disp-cell-clear:" + (Character.getNumericValue(arr[j].charAt(9)) - 1);
					result += "\n/~disp-cell-pins:" + (Character.getNumericValue(arr[j].charAt(9)) - 1) + " "
							+ arr[j].substring(12);
				} else {
					if (buttonNum == -1) {
						JOptionPane.showMessageDialog(null, "On card " + (currCard.getId())
								+ " the pins trying to be displayed were not 8 1's and 0's. Therefore this line will be ignored. Please change it and save again if you wish to correct this",
								"Alert", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "On card " + (currCard.getId()) + " on button "
								+ (buttonNum + 1)
								+ " the pins trying to be displayed were not 8 1's and 0's. Therefore this line will be ignored. Please change it and save again if you wish to correct this",
								"Alert", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if ( arr[j].length() >= 17 && arr[j].substring(0, 17).equals("/Play sound file ") ) {
				result += "\n/~sound:" + arr[j].substring(17);
			} else if (arr[j].length() >= 9 && arr[j].substring(0, 9).equals("/Wait for")) {
				result += "\n/~pause:" + arr[j].charAt(10);
			} else if (arr[j].length() >= 15 && arr[j].substring(0, 15).equals("/Clear all pins")) {
				result += "\n/~disp-clearAll";
			} else if (arr[j].length() >= 29 && arr[j].substring(0, 19).equals("/Display character ")) {
				result += "\n/~disp-cell-char:" + (Character.getNumericValue(arr[j].charAt(29)) - 1) + " " + arr[j].charAt(19 );
			} else if (arr[j].length() >= 16 && arr[j].substring(0, 16).equals("/Display string ")) {
				result += "\n/~disp-string:" + arr[j].substring(16);
			} else {
				result += "\n" + arr[j];
			}
		}

		return result;
	}

	/**
	 * Set up for input
	 * 
	 * @param result
	 *            - previous input - adds onto this and returns it
	 * @param buttons
	 *            - list of buttons to print
	 * @return
	 */
	private String writeInput(String result, ArrayList<DataButton> buttons) {
		for (int i = 0; i < buttons.size(); i++) {
			result += "\n/~skip-button:" + i + " ";
			switch (i) {
			case (0):
				result += "ONEE";
				break;
			case (1):
				result += "TWOO";
				break;
			case (2):
				result += "THREEE";
				break;
			case (3):
				result += "FOURR";
				break;
			case (4):
				result += "FIVEE";
				break;
			case (5):
				result += "SIXX";
				break;
			}
		}
		result += "\n/~user-input";
		return result;
	}


	/**
	 * returns the text - this is the whole text for the file
	 * 
	 * @return
	 */
	public String getText() {
		return this.body;
	}
}
