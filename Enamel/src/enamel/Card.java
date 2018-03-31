package enamel;

import java.util.ArrayList;

/**
 * 
 * @author Jeremy, Nisha, Tyler
 * 
 *         Class to be used by Authoring Viewer to set and get feature details.
 *
 */
@SuppressWarnings("unused")
public class Card {
	// actions has attributes:
	// Name: String
	// Type: String -> {text or audio}
	// ID: int
	// enable: Boolean
	// private fields
	private int id; // unique id to access card object
	private int position; // specifies position in the list of cards
	private String cardName; // card1 card2 card3
	private String type;
	private ArrayList<DataButton> bList;
	private String text;
	private ArrayList<BrailleCell> cells;
	private String sound;

	/**
	 * Constructor to create card object
	 * 
	 * @param id
	 * @param name
	 * @param type
	 */
	public Card(int id, String name, String type) {
		this.id = id;
		this.cardName = name;
		this.type = type;
		this.bList = new ArrayList<>();
		this.cells = new ArrayList<>();
		this.sound = "";
	}

	// Methods
	/**
	 * Method to get cards id
	 * 
	 * @return id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Method to get card name
	 * 
	 * @return card name
	 */
	public String getName() {
		return this.cardName;
	}

	/**
	 * Method to get card type
	 * 
	 * @return cardtype
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Method to get text
	 * 
	 * @return text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Method to get sound
	 * 
	 * @return sound
	 */
	public String getSound() {
		return this.sound;
	}

	/**
	 * Method to get list of cells
	 * 
	 * @return cell list
	 */
	public ArrayList<BrailleCell> getCells() {
		return this.cells;
	}

	/**
	 * Method to get list of buttons
	 * 
	 * @return button list
	 */
	public ArrayList<DataButton> getButtonList() {
		return this.bList;
	}

	/**
	 * Method to add text
	 * 
	 * @param newText
	 */
	public void addText(String newText) {
		if (text == null) {
			text = newText;
		} else {
			text += "\n" + newText;
		}
	}

	/**
	 * Method to set text
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * method to set sound
	 * 
	 * @param newSound
	 */
	public void setSound(String newSound) {
		this.sound = newSound;
	}

	/**
	 * Method to set button list
	 * 
	 * @param newList
	 */
	public void setBList(ArrayList<DataButton> newList) {
		this.bList = newList;
	}

	/**
	 * Method to set cell list
	 * 
	 * @param newList
	 */
	public void setCells(ArrayList<BrailleCell> newList) {
		this.cells = newList;
	}

	/**
	 * Method to set name
	 * 
	 * @param s
	 */
	public void setName(String s) {
		this.cardName = s;
	}
}
