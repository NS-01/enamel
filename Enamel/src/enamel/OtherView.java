package enamel;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import audioRecorder.RecorderFrame;

import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Scrollbar;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import java.awt.GridBagConstraints;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JRadioButton;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.lang.Object;
import enamel.ScenarioParser;
import javax.swing.JSeparator;

/**
 *
 * @author Jeremy, Nisha, Tyler
 *
 *         GUI class to that opens after setting up cells and buttons for new
 *         scenario or after choosing to edit a scenario. This class allows user
 *         to create flow of scenario: ask question, receive response, add
 *         actions to each response, raise pins, record/insert/delete audio
 *         files. User can also change the order of those question. User can
 *         save and test the current scenario. Accessibility features are
 *         implemented.
 *
 */
@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
public class OtherView {

	private JFrame aViewFrame;
	private int numCells = 1;
	private int numButtons = 1;
	private JLabel lblCurrCell;
	private JTextField txtCardName;
	private JTextField txtAudiofilenamemp;
	private JTextField textField;
	private JEditorPane dtrpnEnterAPrompt;
	private JRadioButton pOne;
	private JRadioButton pTwo;
	private JRadioButton pThree;
	private JRadioButton pFour;
	private JRadioButton pFive;
	private JRadioButton pSix;
	private JRadioButton pSeven;
	private JRadioButton pEight;
	private JEditorPane buttonEditor;
	private JList list;
	private DefaultListModel<String> listModel;
	private static String testPins;

	private String title;

	private int currButton;
	private int currCell;
	private int currCard;
	private ArrayList<Card> cards;
	private String initialPrompt;
	private String endingPrompt;
	private String path;
	private boolean promptEdit = false;
	private boolean buttonEdit = false;
	// non zero

	/**
	 * Create the application.
	 */
	public OtherView(int numCells, int numButtons, ArrayList<Card> cards, String initialPrompt, String endingPrompt) {
		this.numButtons = numButtons;

		if (initialPrompt == null || initialPrompt.equals("")) {
			this.initialPrompt = "New Scenario";
			this.title = this.initialPrompt;
		} else {
			this.initialPrompt = initialPrompt;
			this.title = initialPrompt;
		}
		if (endingPrompt == null || endingPrompt.equals("")) {
			this.endingPrompt = "Good Bye";
		} else {
			this.endingPrompt = endingPrompt;
		}

		this.numCells = numCells;
		this.cards = new ArrayList<Card>(cards);
		if (this.cards.get(0).getButtonList().isEmpty()) {
			this.cards.get(0).getButtonList().add(new DataButton(0));
		}
		this.path = "";
		initialize();
		this.currButton = 0;
		this.currCell = 0;
		this.currCard = 0;

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		aViewFrame = new JFrame();
		aViewFrame.getContentPane().setBackground(new Color(217, 217, 217));// (Color.LIGHT_GRAY.brighter());
		aViewFrame.getContentPane().setLayout(null);

		lblCurrCell = new JLabel("1/" + this.numCells);
		lblCurrCell.setBounds(703, 216, 31, 16);
		aViewFrame.getContentPane().add(lblCurrCell);

		JLabel lblPrompt = new JLabel("PROMPT");
		lblPrompt.setBounds(33, 65, 549, 16);
		aViewFrame.getContentPane().add(lblPrompt);

		JLabel lblOrder = new JLabel("ORDER");
		lblOrder.setBounds(947, 10, 61, 16);
		aViewFrame.getContentPane().add(lblOrder);

		txtCardName = new JTextField();
		txtCardName.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// do nothing
			}

			@Override
			public void focusLost(FocusEvent e) {
				cards.get(currCard).setName(txtCardName.getText());
				setCardList();
			}
		});
		txtCardName.setToolTipText("Enter a name for the card");
		txtCardName.setText(cards.get(currCard).getName());
		txtCardName.setBounds(33, 28, 130, 26);
		aViewFrame.getContentPane().add(txtCardName);
		txtCardName.setColumns(10);

		dtrpnEnterAPrompt = new JEditorPane();
		dtrpnEnterAPrompt.setText("Enter a Prompt for this card");
		dtrpnEnterAPrompt.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				dtrpnEnterAPrompt.setText(cards.get(currCard).getText());
				promptEdit = true;
			}

			@Override
			public void focusLost(FocusEvent e) {
				updatePrompt();
			}
		});
		JScrollPane promptPane = new JScrollPane(dtrpnEnterAPrompt);
		promptPane.setBounds(33, 85, 561, 128);
		aViewFrame.getContentPane().add(promptPane);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(33, 320, 561, 65);
		aViewFrame.getContentPane().add(buttonPanel);
		buttonPanel.setLayout(null);

		JLabel label = new JLabel("");
		label.setBounds(53, 19, 0, 0);
		buttonPanel.add(label);

		// determine how many buttons to display
		if (this.numButtons >= 1) {
			JButton button = new JButton("1");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(0);
				}
			});
			button.setBounds(20, 20, 80, 30);
			buttonPanel.add(button);
		}
		if (this.numButtons >= 2) {
			JButton button_1 = new JButton("2");
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(1);
				}

			});
			button_1.setBounds(110, 20, 80, 30);
			buttonPanel.add(button_1);
		}
		if (this.numButtons >= 3) {
			JButton button_2 = new JButton("3");
			button_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(2);
				}
			});
			button_2.setBounds(200, 20, 75, 30);
			buttonPanel.add(button_2);
		}
		if (this.numButtons >= 4) {
			JButton button_3 = new JButton("4");
			button_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(3);
				}
			});
			button_3.setBounds(290, 20, 75, 30);
			buttonPanel.add(button_3);
		}
		if (this.numButtons >= 5) {
			JButton button_4 = new JButton("5");
			button_4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(4);
				}
			});
			button_4.setBounds(380, 20, 75, 30);
			buttonPanel.add(button_4);
		}
		if (this.numButtons >= 6) {
			JButton button_5 = new JButton("6");
			button_5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(5);
				}
			});
			button_5.setBounds(470, 20, 75, 30);
			buttonPanel.add(button_5);
		}

		JLabel lblButtons = new JLabel("BUTTONS");
		lblButtons.setBounds(33, 300, 61, 16);
		aViewFrame.getContentPane().add(lblButtons);

		JLabel lblAudio = new JLabel("AUDIO");
		lblAudio.setBounds(33, 238, 61, 16);
		aViewFrame.getContentPane().add(lblAudio);

		txtAudiofilenamemp = new JTextField();
		txtAudiofilenamemp.setToolTipText("Audio File Name");
		txtAudiofilenamemp.setBounds(33, 256, 209, 26);
		aViewFrame.getContentPane().add(txtAudiofilenamemp);
		txtAudiofilenamemp.setColumns(10);

		JPanel cellPanel = new JPanel();
		cellPanel.setBounds(668, 65, 82, 140);
		aViewFrame.getContentPane().add(cellPanel);
		cellPanel.setLayout(null);

		pOne = new JRadioButton("");
		pOne.setToolTipText("Pin One");
		pOne.setBounds(6, 6, 28, 23);
		cellPanel.add(pOne);

		pFour = new JRadioButton("");
		pFour.setToolTipText("Pin Four");
		pFour.setBounds(46, 6, 28, 23);
		cellPanel.add(pFour);

		pTwo = new JRadioButton("");
		pTwo.setToolTipText("Pin  Two");
		pTwo.setBounds(6, 41, 28, 23);
		cellPanel.add(pTwo);

		pFive = new JRadioButton("");
		pFive.setToolTipText("Pin  Five");
		pFive.setBounds(46, 41, 28, 23);
		cellPanel.add(pFive);

		pThree = new JRadioButton("");
		pThree.setToolTipText("Pin  Three");
		pThree.setBounds(6, 76, 28, 23);
		cellPanel.add(pThree);

		pSix = new JRadioButton("");
		pSix.setToolTipText("Pin  Six");
		pSix.setBounds(46, 76, 28, 23);
		cellPanel.add(pSix);

		pSeven = new JRadioButton("");
		pSeven.setToolTipText("Pin  Seven");
		pSeven.setBounds(6, 111, 28, 23);
		cellPanel.add(pSeven);

		pEight = new JRadioButton("");
		pEight.setToolTipText("Pin  Eight");
		pEight.setBounds(46, 111, 28, 23);
		cellPanel.add(pEight);

		// Under Construction
		// textField = new JTextField();
		// textField.setBounds(73, 185, 130, 26);
		// aViewFrame.getContentPane().add(textField);
		// textField.setColumns(10);
		//
		// JLabel lblLetter = new JLabel("Letter:");
		// lblLetter.setBounds(20, 185, 55, 16);
		// aViewFrame.getContentPane().add(lblLetter);

		JButton btnAudio = new JButton("Audio");
		btnAudio.getAccessibleContext()
				.setAccessibleDescription("Click to insert an audio action to the chosen button response");
		btnAudio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileFilter wavFilter = new FileFilter() {
					@Override
					public String getDescription() {
						return "Sound file (*.WAV)";
					}

					@Override
					public boolean accept(File file) {
						if (file.isDirectory()) {
							return true;
						} else {
							return file.getName().toLowerCase().endsWith(".wav");
						}
					}
				};

				fc.setFileFilter(wavFilter);
				fc.setAcceptAllFileFilterUsed(false);
				fc.setCurrentDirectory(new java.io.File("./FactoryScenarios/AudioFiles"));
				fc.setDialogTitle("Please Choose File to Open");
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if (fc.showOpenDialog(btnAudio) == JFileChooser.APPROVE_OPTION) {
					String temp = fc.getSelectedFile().getName().toString();

					if (temp.length() > 4) {
						if (!temp.toLowerCase().endsWith(".wav")) {
							JOptionPane.showMessageDialog(null, "Please select a .wav file", "Alert",
									JOptionPane.ERROR_MESSAGE);
						} else {
							if (!buttonEdit) {
								buttonEditor.setText("");
								buttonEdit = true;
							}
							setButtonText(buttonEditor.getText() + "\n/~sound:" + (temp));
							updateButton();
							cards.get(currCard).getButtonList().get(currButton).setAudio(temp);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Please select a .wav file", "Alert",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
		btnAudio.setBounds(656, 320, 117, 29);
		aViewFrame.getContentPane().add(btnAudio);

		// Change the state of pin
		JButton btnRaisePins = new JButton("Raise Pins");
		btnRaisePins.getAccessibleContext().setAccessibleDescription(
				"Click to insert an action to selected button response so that braille cell pins are raised");
		btnRaisePins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputValue = JOptionPane.showInputDialog("Please input which pins to raise");
				boolean checkNumber = true;
				if (inputValue != null) {
					for (int i = 0; i < inputValue.length(); i++) {
						System.out.println(i);
						if (inputValue.charAt(i) != '0' && inputValue.charAt(i) != '1') {
							System.out.println("hi");
							checkNumber = false;
						}
					}
					System.out.println(inputValue.length() + " " + inputValue);
					if (inputValue.length() == 8 && checkNumber) {
						if (!buttonEdit) {
							buttonEditor.setText("");
							buttonEdit = true;
						}
						setButtonText(buttonEditor.getText() + "\n/Pins on " + (currCell) + ": " + inputValue);
						updateButton();
					} else {
						JOptionPane.showMessageDialog(null,
								"Please enter 8 1's or 0's corresponding to the pins you want to raise", "Alert",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
		});
		btnRaisePins.setBounds(656, 357, 117, 29);
		aViewFrame.getContentPane().add(btnRaisePins);

		// exit the editor window
		JButton btnExit = new JButton("Exit");
		btnExit.getAccessibleContext().setAccessibleDescription("Click to exit Authoring View Editor");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Do you want to EXIT? \nNo changes will be saved!!!",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					aViewFrame.dispose();
				} else {
					// do nothing
				}
			}
		});
		btnExit.setBounds(10, 663, 75, 50);
		aViewFrame.getContentPane().add(btnExit);

		// save the current card and write it to a file
		JButton btnSave = new JButton("Save");
		btnSave.getAccessibleContext().setAccessibleDescription("Click to save your work");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (buttonEdit == false) {
					buttonEditor.setText("");
				}
				if (promptEdit == false) {
					dtrpnEnterAPrompt.setText("");
				}
				JButton save = new JButton();
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new java.io.File("./FactoryScenarios"));
				fc.setDialogTitle("Save as");
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				// if(!title.equals(null)){
				fc.setSelectedFile(new File(title + ".txt"));
				// }
				FileFilter txtFilter = new FileFilter() {
					@Override
					public String getDescription() {
						return "Text file (*.txt)";
					}

					@Override
					public boolean accept(File file) {
						if (file.isDirectory()) {
							return true;
						} else {
							return file.getName().toLowerCase().endsWith(".txt");
						}
					}
				};
				fc.setFileFilter(txtFilter);
				fc.setAcceptAllFileFilterUsed(false);

				int userChoice = fc.showSaveDialog(null);
				if (userChoice == JFileChooser.APPROVE_OPTION) {
					path = fc.getSelectedFile().getAbsolutePath();
					if (!path.toLowerCase().endsWith(".txt")) {
						path += ".txt";
					}

					File txtFile = new File(path);
					updateButton();
					updatePrompt();
					updateCell();

					CardsToFileParser a = new CardsToFileParser(cards, numButtons, numCells, initialPrompt,
							endingPrompt);
					a.createBody();
					ScenarioWriter sW = new ScenarioWriter(path);
					try {
						sW.write(a.getText());
						JOptionPane.showMessageDialog(null, "Saved the Scenario file to:\n" + path);

					} catch (IOException ex) {
						JOptionPane.showMessageDialog(null, "Error", "Failed to save file!", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}
				}

			}
		});
		btnSave.setBounds(90, 663, 75, 50);
		aViewFrame.getContentPane().add(btnSave);

		// test running the scenario currently being worked on
		JButton btnTest = new JButton("Test");
		btnTest.getAccessibleContext().setAccessibleDescription("Click to Test this scenario");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (path.equals("")) {
					JOptionPane.showMessageDialog(null, "Please save first", "Alert", JOptionPane.ERROR_MESSAGE);
				} else {
					updateButton();
					updatePrompt();
					updateCell();
					CardsToFileParser a = new CardsToFileParser(cards, numButtons, numCells, initialPrompt,
							endingPrompt);
					a.createBody();
					ScenarioWriter sW = new ScenarioWriter(path);
					try {
						sW.write(a.getText());
						new Thread(new Runnable() {
							public void run() {
								ScenarioParser s = new ScenarioParser(true);
								s.setScenarioFile(path);
							}
						}).start();
					} catch (IOException e1) {
						System.out.println("failed to print");
					}
				}

			}
		});
		btnTest.setBounds(170, 663, 75, 50);
		aViewFrame.getContentPane().add(btnTest);

		buttonEditor = new JEditorPane();
		buttonEditor.getAccessibleContext().setAccessibleDescription("Enter a response for this button");
		buttonEditor.setText("Enter a response for this button");
		buttonEditor.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				buttonEditor.setText(cards.get(currCard).getButtonList().get(currButton).getText());
				buttonEdit = true;
			}

			@Override
			public void focusLost(FocusEvent e) {
				updateButton();

			}
		});

		JScrollPane buttonPane = new JScrollPane(buttonEditor);
		buttonPane.getAccessibleContext().setAccessibleDescription("Use to scroll the view");
		buttonPane.setBounds(33, 427, 561, 113);
		aViewFrame.getContentPane().add(buttonPane);

		JButton button_6 = new JButton("<");
		button_6.getAccessibleContext().setAccessibleDescription("Go to previous cell to change raised pins");
		button_6.setToolTipText("Left Cell Button");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currCell == 0) {
					JOptionPane.showMessageDialog(null, "You are already at the first cell", "Alert",
							JOptionPane.ERROR_MESSAGE);
				} else {
					updateCell();
					currCell--;
					setCurrCellPins(cards.get(currCard).getCells().get(currCell));
					lblCurrCell.setText("" + (currCell + 1) + "/" + numCells);
				}
			}
		});
		button_6.setBounds(604, 107, 52, 39);
		aViewFrame.getContentPane().add(button_6);

		JButton button_7 = new JButton(">");
		button_7.getAccessibleContext().setAccessibleDescription("Go to Next cell to change raised pins");
		button_7.setToolTipText("Right Cell Button");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currCell + 1 == numCells) {
					JOptionPane.showMessageDialog(null, "You are already at the last cell", "Alert",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (cards.get(currCard).getCells().size() > currCell + 1) {
						updateCell();
						currCell++;
						setCurrCellPins(cards.get(currCard).getCells().get(currCell));
						lblCurrCell.setText("" + (currCell + 1) + "/" + numCells);
					} else {
						BrailleCell temp = new BrailleCell();
						cards.get(currCard).getCells().add(temp);
						System.out.println(cards.get(currCard).getCells().size());
						updateCell();
						currCell++;
						setCurrCellPins(cards.get(currCard).getCells().get(currCell));
						lblCurrCell.setText("" + (currCell + 1) + "/" + numCells);
					}
				}

			}
		});
		button_7.setBounds(762, 107, 52, 39);
		aViewFrame.getContentPane().add(button_7);

		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.getAccessibleContext().setAccessibleDescription("Card Order List");
		list.setToolTipText("Card Order List");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setBounds(897, 37, 157, 128);
		listScroller.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
		aViewFrame.getContentPane().add(listScroller);

		JButton btnR = new JButton("Record");
		btnR.getAccessibleContext().setAccessibleDescription("Click to record new audio");
		btnR.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnR.setToolTipText("Record an Audio FIle");
		btnR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RecorderFrame rf = new RecorderFrame();
				rf.displayRecorder();
			}
		});
		btnR.setBounds(246, 262, 82, 20);
		aViewFrame.getContentPane().add(btnR);

		JButton btnI = new JButton("Insert");
		btnI.getAccessibleContext().setAccessibleDescription("Click to insert an audio file");
		btnI.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnI.setToolTipText("Import an Audio File");
		btnI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileFilter wavFilter = new FileFilter() {
					@Override
					public String getDescription() {
						return "Sound file (*.WAV)";
					}

					@Override
					public boolean accept(File file) {
						if (file.isDirectory()) {
							return true;
						} else {
							return file.getName().toLowerCase().endsWith(".wav");
						}
					}
				};

				fc.setFileFilter(wavFilter);
				fc.setAcceptAllFileFilterUsed(false);
				fc.setCurrentDirectory(new java.io.File("./FactoryScenarios/AudioFiles"));
				fc.setDialogTitle("Please Choose File to Open");
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				if (fc.showOpenDialog(btnI) == JFileChooser.APPROVE_OPTION) {
					String temp = fc.getSelectedFile().getName().toString();
					txtAudiofilenamemp.setText(temp);
					cards.get(currCard).setSound(temp);
				}
			}
		});
		btnI.setBounds(337, 262, 82, 20);
		aViewFrame.getContentPane().add(btnI);

		JButton btnD = new JButton("Delete");
		btnD.getAccessibleContext().setAccessibleDescription("Click to delete inserted audio");
		btnD.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnD.setToolTipText("Delete the Selected Audio File");
		btnD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cards.get(currCard).setSound("");
				txtAudiofilenamemp.setText(null);
			}
		});
		btnD.setBounds(428, 262, 82, 20);
		aViewFrame.getContentPane().add(btnD);

		JButton btnCardUp = new JButton("Card Up");
		btnCardUp.getAccessibleContext().setAccessibleDescription("Press to move selected card up in card list order");
		btnCardUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected = list.getSelectedIndex();
				if (selected == 0) {
					JOptionPane.showMessageDialog(null, "This card is already at the top", "Alert",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (currCard == selected) {
						currCard--;
					} else if (currCard == selected - 1) {
						currCard++;
					}
					Card temp = cards.get(selected);
					cards.set(selected, cards.get(selected - 1));
					cards.set(selected - 1, temp);
					setCardList();

				}
			}
		});
		btnCardUp.setBounds(897, 172, 160, 20);
		aViewFrame.getContentPane().add(btnCardUp);

		JButton btnCardDown = new JButton("Card Down");
		btnCardDown.getAccessibleContext()
				.setAccessibleDescription("Press to move selected card down in card list order");
		btnCardDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected = list.getSelectedIndex();
				if (selected == cards.size() - 1) {
					JOptionPane.showMessageDialog(null, "This card is already at the bottom", "Alert",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (currCard == selected) {
						currCard++;
					} else if (currCard == selected - 1) {
						currCard--;
					}
					Card temp = cards.get(selected);
					cards.set(selected, cards.get(selected + 1));
					cards.set(selected + 1, temp);
					setCardList();
				}
			}
		});
		btnCardDown.setBounds(897, 197, 160, 20);
		aViewFrame.getContentPane().add(btnCardDown);

		JButton btnNextCard = new JButton("Next Card");
		btnNextCard.getAccessibleContext().setAccessibleDescription(
				"Click to add a new card to the list. By doing so your view will change to next card");
		btnNextCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cards.size() > currCard + 1) {
					nextCard();
				} else {
					System.out.println(cards.size());
					Card temp = new Card(currCard + 1, "Card " + (currCard + 2), "", true);
					cards.add(temp);
					temp.getButtonList().add(new DataButton(0));
					temp.getCells().add(new BrailleCell());
					nextCard();
				}
			}
		});
		btnNextCard.setBounds(908, 629, 146, 29);
		aViewFrame.getContentPane().add(btnNextCard);

		JButton btnPreviousCard = new JButton("Previous Card");
		btnPreviousCard.getAccessibleContext().setAccessibleDescription(
				"Click to go back to the previus card u edited. By doing so your view will change to next card");
		btnPreviousCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currCard == 0) {
					JOptionPane.showMessageDialog(null, "You are already at the first card", "Alert",
							JOptionPane.ERROR_MESSAGE);
				} else {
					prevCard();
				}
			}
		});
		btnPreviousCard.setBounds(908, 589, 146, 29);
		aViewFrame.getContentPane().add(btnPreviousCard);

		JLabel lblCell = new JLabel("Cell:");
		lblCell.getAccessibleContext().setAccessibleDescription("Indicates Cell number of the cell being displayed");
		lblCell.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCell.setBounds(668, 216, 31, 14);
		aViewFrame.getContentPane().add(lblCell);
		
		JLabel label_1 = new JLabel("1/1");
		label_1.setBounds(703, 567, 31, 16);
		aViewFrame.getContentPane().add(label_1);
		
		JPanel buttonCellPanel = new JPanel();
		buttonCellPanel.setLayout(null);
		buttonCellPanel.setBounds(668, 413, 82, 140);
		aViewFrame.getContentPane().add(buttonCellPanel);
		
		JRadioButton radioButton = new JRadioButton("");
		radioButton.setToolTipText("Pin One");
		radioButton.setBounds(6, 6, 28, 23);
		buttonCellPanel.add(radioButton);
		
		JRadioButton radioButton_1 = new JRadioButton("");
		radioButton_1.setToolTipText("Pin Four");
		radioButton_1.setBounds(46, 6, 28, 23);
		buttonCellPanel.add(radioButton_1);
		
		JRadioButton radioButton_2 = new JRadioButton("");
		radioButton_2.setToolTipText("Pin  Two");
		radioButton_2.setBounds(6, 41, 28, 23);
		buttonCellPanel.add(radioButton_2);
		
		JRadioButton radioButton_3 = new JRadioButton("");
		radioButton_3.setToolTipText("Pin  Five");
		radioButton_3.setBounds(46, 41, 28, 23);
		buttonCellPanel.add(radioButton_3);
		
		JRadioButton radioButton_4 = new JRadioButton("");
		radioButton_4.setToolTipText("Pin  Three");
		radioButton_4.setBounds(6, 76, 28, 23);
		buttonCellPanel.add(radioButton_4);
		
		JRadioButton radioButton_5 = new JRadioButton("");
		radioButton_5.setToolTipText("Pin  Six");
		radioButton_5.setBounds(46, 76, 28, 23);
		buttonCellPanel.add(radioButton_5);
		
		JRadioButton radioButton_6 = new JRadioButton("");
		radioButton_6.setToolTipText("Pin  Seven");
		radioButton_6.setBounds(6, 111, 28, 23);
		buttonCellPanel.add(radioButton_6);
		
		JRadioButton radioButton_7 = new JRadioButton("");
		radioButton_7.setToolTipText("Pin  Eight");
		radioButton_7.setBounds(46, 111, 28, 23);
		buttonCellPanel.add(radioButton_7);
		
		JButton button = new JButton("<");
		button.setToolTipText("Left Cell Button");
		button.setBounds(604, 455, 52, 39);
		aViewFrame.getContentPane().add(button);
		
		JButton button_1 = new JButton(">");
		button_1.setToolTipText("Right Cell Button");
		button_1.setBounds(762, 455, 52, 39);
		aViewFrame.getContentPane().add(button_1);
		
		JLabel label_2 = new JLabel("Cell:");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_2.setBounds(668, 567, 31, 14);
		aViewFrame.getContentPane().add(label_2);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(864, 11, 209, 739);
		aViewFrame.getContentPane().add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 629, 866, 100);
		aViewFrame.getContentPane().add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(0, 291, 866, 339);
		aViewFrame.getContentPane().add(separator_2);

		aViewFrame.setResizable(false);
		aViewFrame.setBackground(new Color(255, 255, 255));
		aViewFrame.setTitle("AuthoringApp view");
		aViewFrame.getAccessibleContext().setAccessibleDescription("Authoring App Editor");
		aViewFrame.setBounds(100, 100, 1089, 769);
		aViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		aViewFrame.addWindowListener(new confirmClose());

		aViewFrame.setVisible(true);
	}

	private class confirmClose extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showConfirmDialog(null, "Do you want to EXIT? \nNo changes will be saved!!!",
					"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				aViewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} else {
				// do nothing
			}
		}
	}

	/**
	 * Method to set Prompt text
	 *
	 * @param text
	 */
	public void setPromptText(String text) {
		dtrpnEnterAPrompt.setText(text);
	}

	/**
	 * Method to set the pins of braille sell
	 *
	 * @param cell
	 */
	public void setCurrCellPins(BrailleCell cell) {
		pOne.setSelected(cell.getPinState(0));
		pTwo.setSelected(cell.getPinState(1));
		pThree.setSelected(cell.getPinState(2));
		pFour.setSelected(cell.getPinState(3));
		pFive.setSelected(cell.getPinState(4));
		pSix.setSelected(cell.getPinState(5));
		pSeven.setSelected(cell.getPinState(6));
		pEight.setSelected(cell.getPinState(7));
	}

	/**
	 * Method to set button text
	 *
	 * @param text
	 */
	public void setButtonText(String text) {
		buttonEditor.setText(text);
	}

	/**
	 * Method to set card list
	 */

	public void setCardList() {
		listModel.clear();
		for (Card cards : this.cards) {
			listModel.addElement(cards.getName());
		}
		list.setSelectedIndex(currCard);
	}

	/**
	 * Method to show button text
	 *
	 * @param buttonNum
	 */
	public void showButtonText(int buttonNum) { // ButtonNum 0-5
		if (currButton != buttonNum) {
			try {
				String replace = cards.get(currCard).getButtonList().get(buttonNum).getText();
				cards.get(currCard).getButtonList().get(currButton).setText(buttonEditor.getText());
				currButton = buttonNum;
				this.setButtonText(replace);
			} catch (Exception e) {
				int size = cards.get(currCard).getButtonList().size();
				cards.get(currCard).getButtonList().get(currButton).setText(buttonEditor.getText());
				while (size <= buttonNum) {
					System.out.println(size);
					cards.get(currCard).getButtonList().add(new DataButton(size));
					size = cards.get(currCard).getButtonList().size();
				}
				System.out.println(size);
				currButton = buttonNum;
				this.setButtonText("");
			}
		}
	}

	/**
	 * Method to update button
	 */
	public void updateButton() {
		if (cards.get(currCard).getButtonList().isEmpty()) {
			DataButton temp = new DataButton(0);
			cards.get(currCard).getButtonList().add(temp);
		}
		cards.get(currCard).getButtonList().get(currButton).setText(buttonEditor.getText());
	}

	/**
	 * Method to update prompt
	 */
	public void updatePrompt() {
		cards.get(currCard).setText(dtrpnEnterAPrompt.getText());
	}

	/**
	 * Method to update braille cell
	 */
	public void updateCell() {
		BrailleCell temp = new BrailleCell();
		String s = "";
		s += pOne.isSelected() ? "1" : "0";
		s += pTwo.isSelected() ? "1" : "0";
		s += pThree.isSelected() ? "1" : "0";
		s += pFour.isSelected() ? "1" : "0";
		s += pFive.isSelected() ? "1" : "0";
		s += pSix.isSelected() ? "1" : "0";
		s += pSeven.isSelected() ? "1" : "0";
		s += pEight.isSelected() ? "1" : "0";
		temp.setPins(s);
		cards.get(currCard).getCells().set(currCell, temp);
	}

	/**
	 * Method to show prompt
	 */
	public void showPrompt() {
		setPromptText(cards.get(currCard).getText());
	}

	/**
	 * Method to go to next card
	 */
	public void nextCard() {
		updateButton();
		updatePrompt();
		updateCell();
		setCurrCellPins(cards.get(currCard).getCells().get(currCell));
		cards.get(currCard).setName(txtCardName.getText());
		currCard++;
		currButton = 0;
		currCell = 0;
		if (cards.get(currCard).getButtonList().isEmpty()) {
			cards.get(currCard).getButtonList().add(new DataButton(0));
		}
		lblCurrCell.setText("1/" + this.numCells);
		this.setButtonText(cards.get(currCard).getButtonList().get(0).getText());
		txtCardName.setText(cards.get(currCard).getName());
		showPrompt();
		setCurrCellPins(cards.get(currCard).getCells().get(currCell));
		setCardList();
	}

	/**
	 * Method to go to previous card
	 */
	public void prevCard() {
		updateButton();
		updatePrompt();
		updateCell();
		setCurrCellPins(cards.get(currCard).getCells().get(currCell));
		cards.get(currCard).setName(txtCardName.getText());
		currCard--;
		currButton = 0;
		currCell = 0;
		lblCurrCell.setText("1/" + this.numCells);
		this.setButtonText(cards.get(currCard).getButtonList().get(0).getText());
		txtCardName.setText(cards.get(currCard).getName());
		showPrompt();
		setCurrCellPins(cards.get(currCard).getCells().get(currCell));
		setCardList();
	}

	/**
	 * Method to set edited booleans
	 */
	public void setEdited() {
		this.buttonEdit = true;
		this.promptEdit = true;
	}
}
