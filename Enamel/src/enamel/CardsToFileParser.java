package enamel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * 
 * @author Jeremy, Nisha, Tyler
 * 
 *         Class to parse card data to a file.
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

	//creates the entire file as one string
	public void createBody() {
		body += "Cell " + numCells;
		body += "\nButton " + numButtons;
//		body += "\n" + initialPrompt;
		//write stuff for each card
		for (Card currCard : cards) {
			body += "\n" + writeCard(currCard);
		}
//		body += "\n" + endingPrompt;
		//clears cells at end
		for (int i = 0; i < this.numCells; i++) {
			body += "\n/~disp-cell-clear:" + i + "";
		}
		System.out.println(body);
	}

	/**
	 * writes the stuff for each card
	 * @param currCard - the card 
	 * @return
	 */
	public String writeCard(Card currCard) {
		//clear cells at start
		String result = "";
//		String result = "/~disp-cell-clear:0";
//		for (int i = 1; i < this.numCells; i++) {
//			result += "\n/~disp-cell-clear:" + i;
//		}
//		result = writeCells(currCard, result);
		
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
			switch(i) {
			case (0):
				result += "\n/~ONEE"; break;
			case (1):
				result += "\n/~TWOO"; break;
			case (2):
				result += "\n/~THREEE"; break;
			case (3):
				result += "\n/~FOURR"; break;
			case (4):
				result += "\n/~FIVEE"; break;
			case (5):
				result += "\n/~SIXX"; break;
			}
			
			//Still not sure how we are doing audio. Are we adding text
			//onto the editor for them or are we storing it into the 
			//audio field in the button
			//which way do we want???
			String audioPath = buttons.get(i).getAudio();
			//result = checkAudio(result, audioPath);
			result = writeTextAndCheckCells(currCard, result, buttons.get(i).getText(), i);
			result += "\n/~skip:NEXTT";
		}
		
		return result;
	}

	/**
	 * this was a previous way we had for checking audio
	 * @param result
	 * @param audioPath
	 * @return
	 */
	private String checkAudio(String result, String audioPath) {
		if (!audioPath.equals("")) {
			int slashPos = 0;
			for (int j = 0; j < audioPath.length(); j++) {
				if (audioPath.charAt(j) == '\\') {
					slashPos = j;
				}
			}
			if (slashPos == 0) {
				result += "\n/~sound:" + audioPath;
			}
			else {
				result += "\n/~sound:" + audioPath.substring(slashPos + 1);
			}
			
		}
		return result;
	}

	private String writeTextAndCheckCells(Card currCard, String result, String input, int buttonNum) {
		String[] arr = input.split("\n");
		for (int j = 0; j < arr.length; j++) {
			// this was our previous indicator
			//currently we are just writing the command for them
			//which way do we want???
			//this whole if statement is just checking for our previous indicator
			if ( arr[j].length() == 20 && arr[j].substring(0, 9).equals("/Pins on ") ) {
				boolean checkNumber = true;
				for (int k = 0; k < arr[j].substring(12).length(); k++) {
					if (arr[j].substring(12).charAt(k) != '0' && arr[j].substring(12).charAt(k) != '1') {
						System.out.println("hi");
						checkNumber = false;
					}
				}
				if (checkNumber) {
					result += "\n/~disp-cell-clear:" + (Character.getNumericValue(arr[j].charAt(9))-1);
					result += "\n/~disp-cell-pins:" + (Character.getNumericValue(arr[j].charAt(9))-1) + " " + arr[j].substring(12);
				}
				else {
					if (buttonNum == -1) {
						JOptionPane.showMessageDialog(null,
								"On card " + (currCard.getId()) +  
								" the pins trying to be displayed were not 8 1's and 0's. Therefore this line will be ignored. Please change it save again if you wish to correct this"
								, "Alert", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null,
								"On card " + (currCard.getId()) +  " on button " + (buttonNum+1) +
								" the pins trying to be displayed were not 8 1's and 0's. Therefore this line will be ignored. Please change it save again if you wish to correct this"
								, "Alert", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if ( arr[j].length() >= 8 && arr[j].substring(0, 8).equals("/sound: ") ) {
				result += "\n/~sound:" + arr[j].substring(8);
			} else {
				result += "\n" + arr[j];
			}
		}
		
		return result;
	}
	
	/**
	 * Set up for input
	 * @param result - previous input - adds onto this and returns it
	 * @param buttons - list of buttons to print
	 * @return
	 */
	private String writeInput(String result, ArrayList<DataButton> buttons) {
		for (int i = 0; i < buttons.size(); i++) {
			result += "\n/~skip-button:" + i + " ";
			switch(i) {
			case (0):
				result += "ONEE"; break;
			case (1):
				result += "TWOO"; break;
			case (2):
				result += "THREEE"; break;
			case (3):
				result += "FOURR"; break;
			case (4):
				result += "FIVEE"; break;
			case (5):
				result += "SIXX"; break;
			}
		}
		result += "\n/~user-input";
		return result;
	}

	/**
	 * writes the cells
	 * @param currCard - which card to write from
	 * @param result - method adds to this string and returns it
	 * @return
	 */
	private String writeCells(Card currCard, String result) {
		ArrayList<BrailleCell> cells = currCard.getCells();
		for (int i = 0; i < cells.size(); i++) {
			String pins = "";
			if (cells.get(i) != null) {
				for (int j = 0; j < cells.get(i).getNumberOfPins(); j++) {
					pins += cells.get(i).getPinState(j) ? "1" : "0";
				}
				result += "\n/~disp-cell-pins:" + i + " " + pins;
			}
			
		}
		return result;
	}

	/**
	 * returns the text - this is the whole text for the file
	 * @return
	 */
	public String getText() {
		return this.body;
	}
}
