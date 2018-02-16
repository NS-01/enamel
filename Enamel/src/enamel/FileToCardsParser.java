package enamel;

import java.util.*;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.io.*;

public class FileToCardsParser {

        private Scanner fileScanner;
        private ArrayList<Card> cards;
        private String scenarioFilePath;
        private String initialPrompt;
        private int numButtons;
        private int numCells;
        private int numLines;
        private int start;

        public FileToCardsParser() {
                cards = new ArrayList<Card>();
        }

        public void setFile(String scenarioFile) {
                try {
                        File f = new File(scenarioFile);
                        fileScanner = new Scanner(f);
                        String absolutePath = f.getAbsolutePath();
                        scenarioFilePath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
                        String fileLine;
                        checkNumLines(scenarioFile);
                        checkButtonsAndCells();
                        parse();
                } catch (Exception e) {
                        System.out.println("Incorrect File Name");
                }
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
                        System.out.println(numLines);
                } catch (Exception e) {
                        System.out.println("Something went wrong");
                }
                System.out.println(start);

        }

        public void checkButtonsAndCells() {
                String fileLine = fileScanner.nextLine();

                if (fileLine.substring(0, 4).equals("Cell")) {
                        numCells = Character.getNumericValue(fileLine.charAt(5));
                } else {
                        throw new IllegalArgumentException();
                }
                fileLine = fileScanner.nextLine();
                if (fileLine.substring(0, 6).equals("Button")) {
                        numButtons = Character.getNumericValue(fileLine.charAt(7));
                } else {
                        throw new IllegalArgumentException();
                }


        }

        public void parse() {
                boolean inButton = false;
                String fileLine;
                int cardNum = 1;
                int cellNum = 1;
                int buttonNum = 1;
                int currLineNum = 2;
                while (currLineNum < start-1 && fileScanner.hasNextLine()) {
                        currLineNum++;
                        fileScanner.nextLine();
                }
                Card currCard = new Card(cardNum - 1, "card" + cardNum, "notSure");
                ArrayList<DataButton> buttons = new ArrayList<DataButton>(numButtons);
                ArrayList<BrailleCell> cells = new ArrayList<BrailleCell>(numCells);
                DataButton currButton = new DataButton(buttonNum);
                BrailleCell currCell = new BrailleCell();
                while (fileScanner.hasNextLine()) {
                        currLineNum++;
                        fileLine = fileScanner.nextLine();
                        if (fileLine.replace(" ", "").equals("")) continue;
                        //System.out.println(fileLine);
                        //System.out.println(fileLine.equals("/~disp-cell-clear:" + (numCells-1)));
                        // System.out.println(!inButton);
                        if (fileLine.length() >= 2 && fileLine.substring(0, 2).equals("/~")) {
                                if (currLineNum == numLines) {
                                        buttons.clear();
                                        currCard.setBList(new ArrayList<DataButton>(buttons));
                                        currCard.setCells(cells);
                                        cards.add(currCard);
                                        currCard = new Card(cardNum - 1, "card"+ cardNum, "notSure");
                                }
                                if (fileLine.equals("/~NEXTT") ||
                                // ( fileLine.equals("/~disp-cell-clear:" + (numCells-1)) &&
                                // !inButton) ) {
                                                fileLine.equals("/~reset-buttons") ) {
                                        buttons.add(new DataButton(currButton));
                                        inButton = false;
                                        currCard.setBList(new ArrayList<DataButton>(buttons));
                                        buttons.clear();
                                        currCard.setCells(cells);
                                        cards.add(currCard);
                                        currCard = new Card(cardNum - 1, "card"+ cardNum, "notSure");
                                }

                                // else if (fileLine.length() >= 18 && fileLine.substring(0,
                                // 18).equals("/~disp-cell-clear:")) continue;
                                else if (fileLine.length() >= 17 && fileLine.substring(0, 17).equals("/~disp-cell-pins:")) {
                                        currCell.setPins(fileLine.substring(19));
                                        try {
                                                cells.set(Character.getNumericValue(fileLine.charAt(17)), currCell);
                                        } catch (Exception e) {
                                                cells.add(currCell);
                                        }
                                } else if (fileLine.length() >= 8 && fileLine.substring(0, 8).equals("/~sound:")) {
                                        if (inButton) {
                                                currButton.setAudio(scenarioFilePath + File.separator + "AudioFiles" + File.separator
                                                                + fileLine.substring(8));
                                        } else {
                                                currCard.setSound(scenarioFilePath + File.separator + "AudioFiles" + File.separator
                                                                + fileLine.substring(8));
                                        }
                                } else if (fileLine.equals("/~user-input")) {
                                        inButton = true;
                                } else if (fileLine.equals("/~ONEE")) {
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

                        } else {

                                if (inButton) {
                                        currButton.addText(fileLine);
                                } else {

                                        currCard.addText(fileLine);
                                }
                        }
                }
                System.out.println(cards.size());
                for (int i = 0; i < cards.size(); i++) {
                        System.out.println("In card " + i + ":\n" + cards.get(i).getText() + "\n\n");
                        ArrayList<DataButton> buttonList = cards.get(i).getButtonList();
                        if (buttonList.size() > 0){
                                for (int j = 0; j < buttonList.size(); j++) {
                                        System.out.println(
                                                        "In Button" + j + ":" +buttonList.get(j).getID() + "\n" + buttonList.get(j).getText() + "\n");
                                }
                        }

                        System.out.println("\n\n\n");
                }
                for (int i = 0; i < cards.size(); i++) {
                	for (int j = 0; j < 8; j++) {
                		System.out.print(cards.get(i).getCells().get(0).getPinState(j));
                	}
                	System.out.println();
                }
                
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

}
