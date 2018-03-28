package enamel;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

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

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

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
public class NewView {

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
	public NewView(int numCells, int numButtons, ArrayList<Card> cards, String initialPrompt, String endingPrompt) {
		this.numButtons = numButtons;

		if (initialPrompt == null || initialPrompt.equals("")) {
			this.initialPrompt = "New Scenario";
		} else {
			this.initialPrompt = initialPrompt;
		}
		this.title = this.initialPrompt;
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
		aViewFrame.getContentPane().setBackground(new Color(217, 217, 217));
		aViewFrame.getContentPane()
				.setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("20px"), ColumnSpec.decode("75px:grow"), FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("75px"), FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("318px:grow"),
						FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("52px"), FormSpecs.UNRELATED_GAP_COLSPEC,
						ColumnSpec.decode("82px"), FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("52px"),
						ColumnSpec.decode("30px"), ColumnSpec.decode("126px"), FormSpecs.UNRELATED_GAP_COLSPEC,
						ColumnSpec.decode("52px"), FormSpecs.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("52px"), },
						new RowSpec[] { FormSpecs.UNRELATED_GAP_ROWSPEC, RowSpec.decode("16px"),
								FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("128px:grow"),
								FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("20px:grow"),
								FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("26px"),
								FormSpecs.LABEL_COMPONENT_GAP_ROWSPEC, RowSpec.decode("65px"),
								FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
								FormSpecs.PARAGRAPH_GAP_ROWSPEC, RowSpec.decode("113px"), RowSpec.decode("112px"),
								RowSpec.decode("50px"), }));


		lblCurrCell = new JLabel("1/" + this.numCells);
		aViewFrame.getContentPane().add(lblCurrCell, "18, 10, left, top");
		
		JLabel lblPrompt = new JLabel("PROMPT");
		aViewFrame.getContentPane().add(lblPrompt, "2, 2, left, top");

		JLabel lblOrder = new JLabel("ORDER");
		aViewFrame.getContentPane().add(lblOrder, "14, 2, left, top");
		
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
		txtCardName.setColumns(10);
		aViewFrame.getContentPane().add(txtCardName, "14, 10, 3, 1, right, top");

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
		aViewFrame.getContentPane().add(promptPane, "2, 4, 5, 1, fill, fill");
		
		JPanel buttonPanel = new JPanel();
		aViewFrame.getContentPane().add(buttonPanel, "2, 10, 5, 1, fill, fill");
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
		aViewFrame.getContentPane().add(lblButtons, "2, 8, 5, 1, fill, bottom");
		
		JPanel cellPanel = new JPanel();
		cellPanel.setLayout(null);
		aViewFrame.getContentPane().add(cellPanel, "10, 4, 1, 3, fill, fill");

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
		aViewFrame.getContentPane().add(btnExit, "2, 16, left, fill");

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
				if(title != null){
					fc.setSelectedFile(new File(title + ".txt"));
				}
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
		aViewFrame.getContentPane().add(btnSave, "4, 16, left, fill");

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
		aViewFrame.getContentPane().add(btnTest, "6, 16, left, fill");
		
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
		buttonPane.setBounds(250, 400, 561, 113);
		aViewFrame.getContentPane().add(buttonPane, "2, 12, 5, 3, fill, fill");
		
		JButton button_6 = new JButton("<");
		button_6.getAccessibleContext().setAccessibleDescription("Go to previous cell to change raised pins");
		button_6.setToolTipText("Go the next cell");
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
		aViewFrame.getContentPane().add(button_6, "8, 4, fill, center");
		
		JButton button_7 = new JButton(">");
		button_7.getAccessibleContext().setAccessibleDescription("Go to Next cell to change raised pins");
		button_7.setToolTipText("Go the previous cell");
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
		aViewFrame.getContentPane().add(button_7, "12, 4, fill, center");

		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.getAccessibleContext().setAccessibleDescription("Card Order List");
		list.setToolTipText("Card Order List");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
		aViewFrame.getContentPane().add(listScroller, "14, 4, 5, 1, fill, fill");

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
		aViewFrame.getContentPane().add(btnCardUp, "14, 6, 5, 1, fill, fill");

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
		aViewFrame.getContentPane().add(btnCardDown, "14, 8, 5, 1, fill, fill");

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
		aViewFrame.getContentPane().add(btnNextCard, "16, 16, 3, 1, fill, top");

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
		aViewFrame.getContentPane().add(btnPreviousCard, "14, 16, fill, top");

		

		JLabel lblName = new JLabel("NAME");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 12));
		aViewFrame.getContentPane().add(lblName, "14, 10, left, top");

		

		

		

		
		
		aViewFrame.setBackground(new Color(255, 255, 255));
		aViewFrame.setTitle("AuthoringApp view");
		aViewFrame.getAccessibleContext().setAccessibleDescription("Authoring App Editor");
		aViewFrame.setBounds(100, 100, 1000, 612);
		aViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		aViewFrame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("FILE");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);

		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);

		JMenuItem mntmTest = new JMenuItem("Test");
		mnFile.add(mntmTest);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("EDIT");
		menuBar.add(mnEdit);

		JMenuItem mntmScenarioForm = new JMenuItem("Scenario Form");
		mnEdit.add(mntmScenarioForm);

		JMenu mnView = new JMenu("VIEW");
		menuBar.add(mnView);

		JMenuItem mntmFullscreen = new JMenuItem("Fullscreen");
		mnView.add(mntmFullscreen);

		JMenu mnAudio = new JMenu("AUDIO");
		menuBar.add(mnAudio);

		JMenuItem mntmRecord = new JMenuItem("Record");
		mnAudio.add(mntmRecord);

		JMenu mnHelp = new JMenu("HELP");
		menuBar.add(mnHelp);

		JMenuItem mntmTutorial = new JMenuItem("Tutorial");
		mnHelp.add(mntmTutorial);

		JMenu mnAbout = new JMenu("ABOUT");
		menuBar.add(mnAbout);

		JMenuItem mntmGithub = new JMenuItem("Github");
		mnAbout.add(mntmGithub);

		JMenuItem mntmCoursieWebsite = new JMenuItem("Course Website");
		mnAbout.add(mntmCoursieWebsite);
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