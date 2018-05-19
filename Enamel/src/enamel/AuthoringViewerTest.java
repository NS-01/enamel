package enamel;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import java.awt.GridBagConstraints;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.Icon;

import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Event;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JRadioButton;
import java.awt.GridLayout;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import java.lang.Object;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.CardLayout;
import javax.swing.ImageIcon;

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
// New Updates
@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
public class AuthoringViewerTest {

	private JFrame aViewFrame;
	private JPanel container;
	private int numCells = 1;
	private int numButtons = 1;
	private JTextField textField;
	private DefaultListModel<String> listModel;
	private static String testPins;

	private String title;

	private int currButton;
	private int currCell;
	private int responseCell;
	private int currCard;
	private String initialPrompt;
	private String endingPrompt;
	private String path;
	private boolean promptEdit = false;
	private boolean buttonEdit = false;
	// non zero

	private JRadioButton pOne;
	private JRadioButton pTwo;
	private JRadioButton pThree;
	private JRadioButton pFour;
	private JRadioButton pFive;
	private JRadioButton pSix;
	private JRadioButton pSeven;
	private JRadioButton pEight;
	private JEditorPane promptTextField;
	private JLabel lblCurrCell;
	private JTextField txtCardName;
	private JTextField txtAudiofilenamemp;
	private JEditorPane buttonEditor;
	private JList list;
	private ArrayList<Card> cards;
	private JPanel cellPanelLabel;
	private JLabel promptCellLabel;
	private JLabel responseCellLabel;
	private JRadioButton rspOne;
	private JRadioButton rspFour;
	private JRadioButton rspTwo;
	private JRadioButton rspFive;
	private JRadioButton rspThree;
	private JRadioButton rspSix;
	private JRadioButton rspSeven;
	private JRadioButton rspEight;
	private JPanel buttonPanel;
	private JPanel generalCellPanel;
	private JPanel undoRedoResponsePanel;
	private JList actionList;
	private DefaultListModel<String> actionListModel;

	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// ArrayList<Card> stuff = new ArrayList<Card>();
	// Card hi = new Card(1, "Card 1", "idk");
	// stuff.add(hi);
	// AuthoringViewerTest window = new AuthoringViewerTest(2, 6, stuff, "Hi",
	// "Bye");
	// window.aViewFrame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	//
	// }

	/**
	 * Create the application.
	 */
	public AuthoringViewerTest(int numCells, int numButtons, ArrayList<Card> cards, String initialPrompt,
			String endingPrompt) {

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
		this.responseCell = 0;
		this.currCard = 0;

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setUpFrame();

		createPromptTextField();

		createPromptCell();

		createListPanel();

		createButtonLabelPanel();

		createResponsePanel();

		createResponseCell();

		createPrevNextButtons();

		createMenuBar();

		// New Commit might need to delete
		/*
		 * JPanel cardNamePanel = new JPanel(); cardNamePanel.setBackground(new
		 * Color(217, 217, 217)); GridBagConstraints gbc_panel_1 = new
		 * GridBagConstraints(); gbc_panel_1.insets = new Insets(10, 5, 5, 10);
		 * gbc_panel_1.fill = GridBagConstraints.BOTH; gbc_panel_1.gridx = 2;
		 * gbc_panel_1.gridy = 3; aViewFrame.getContentPane().add(cardNamePanel,
		 * gbc_panel_1); cardNamePanel.setLayout(new BorderLayout(0, 0));
		 * 
		 * txtCardName = new JTextField(); txtCardName.addFocusListener(new
		 * FocusListener() {
		 * 
		 * @Override public void focusGained(FocusEvent e) { // do nothing }
		 * 
		 * @Override public void focusLost(FocusEvent e) {
		 * cards.get(currCard).setName(txtCardName.getText()); setCardList(); }
		 * }); txtCardName.setToolTipText("Enter a name for the card");
		 * txtCardName.setText(cards.get(currCard).getName());
		 * txtCardName.setColumns(10); cardNamePanel.add(txtCardName,
		 * BorderLayout.NORTH);
		 */
		// end new commit

		JButton btnEnableUserResponse = new JButton("Enable User Response");
		btnEnableUserResponse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// on button Press Enable the button panel and button pane
				// buttonEditor.setEnabled(true);
				// buttonPanel.setVisible(true);
				// generalCellPanel.setVisible(true);
				cards.get(currCard).setEnabled(true);
				setVisible(true);
			}
		});
		btnEnableUserResponse.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_btnEnableUserResponse = new GridBagConstraints();
		gbc_btnEnableUserResponse.insets = new Insets(0, 0, 5, 5);
		gbc_btnEnableUserResponse.gridx = 1;
		gbc_btnEnableUserResponse.gridy = 4;
		container.add(btnEnableUserResponse, gbc_btnEnableUserResponse);

		// Actions List View
		JScrollPane actionListScroller = new JScrollPane((Component) null);
		GridBagConstraints gbc_actionListScroller = new GridBagConstraints();
		gbc_actionListScroller.insets = new Insets(0, 0, 5, 0);
		gbc_actionListScroller.fill = GridBagConstraints.BOTH;
		gbc_actionListScroller.gridx = 2;
		gbc_actionListScroller.gridy = 4;
		container.add(actionListScroller, gbc_actionListScroller);
		actionListModel = new DefaultListModel<>();
		actionList = new JList(actionListModel);
		actionList.setVisibleRowCount(-1);
		actionList.setToolTipText("List of Actions");
		actionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		actionList.setLayoutOrientation(JList.VERTICAL);
		actionListScroller.setViewportView(actionList);

		createUndoRedoPanelButtons();
		createResponseUndoRedoPanelButtons();
		displayFrame();
	}

	/*************************************************************
	 * Undo redo pause and other actions
	 *************************************************************/
	private void createUndoRedoPanelButtons() {
		JPanel undoRedoPanel = new JPanel();
		GridBagConstraints gbc_undoRedoPanel = new GridBagConstraints();
		gbc_undoRedoPanel.insets = new Insets(0, 10, 5, 5);
		gbc_undoRedoPanel.fill = GridBagConstraints.BOTH;
		gbc_undoRedoPanel.gridx = 0;
		gbc_undoRedoPanel.gridy = 3;
		container.add(undoRedoPanel, gbc_undoRedoPanel);

		Icon undoIcon = new ImageIcon("Images/undo-16.png");
		undoRedoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		JButton btnUndo = new JButton("Undo", undoIcon);
		btnUndo.setFont(new Font("Tahoma", Font.BOLD, 14));
		// btnUndo.setSelectedIcon(new
		// ImageIcon("Images/undo-16.png"),JButton.LEFT);
		undoRedoPanel.add(btnUndo);

		Icon redoIcon = new ImageIcon("Images/redo-16.png");
		JButton btnRedo = new JButton("Redo", redoIcon);
		btnRedo.setFont(new Font("Tahoma", Font.BOLD, 14));
		undoRedoPanel.add(btnRedo);

		JButton btnPause = new JButton("Pause", null);
		btnPause.setFont(new Font("Tahoma", Font.BOLD, 14));
		undoRedoPanel.add(btnPause);

		pauseAction(btnPause);

		JComboBox comboBox = new JComboBox();
		comboBox.setRenderer(new CustomComboBoxRenderer("INSERT ACTION"));
		comboBox.setModel(
				new DefaultComboBoxModel(new String[] { "Play Audio File", "Display Character", "Display String" }));
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboBox.setSelectedIndex(-1);
		undoRedoPanel.add(comboBox);

		comboBox.addItemListener(new ItemListener() {
			int count = 0;

			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					// count++;
					// logger.log(Level.INFO, "Cell Combo Box was used.");
					// logger.log(Level.INFO, "Cell Combo Box was used {0}
					// times", count);
				}
				int state = itemEvent.getStateChange();
				if (state == 1) {
					promptTextField.requestFocus();
					promptTextField.transferFocus();
					System.out.println(comboBox.getSelectedIndex());
					if (comboBox.getSelectedIndex() == 0) {
						// Put stuff for play sound here
						// ********************************************************************
						// comboBox.setSelectedIndex(-1);
						addAudioToPrompt();
					} else if (comboBox.getSelectedIndex() == 1) {
						displayCharacter(comboBox);
					} else if (comboBox.getSelectedIndex() == 2) {
						displayString(comboBox);
					}
					updatePrompt();
				}
				// selectedCells = comboBox.getSelectedIndex();
			}
		});

		// KeyBoard Shortcut "Ctrl+Z" for Undo on Prompt Section
		KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		// KeyBoard Shortcut "Ctrl+Y" for Redo on Prompt Section
		KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);

		UndoManager undoManager = new UndoManager();
		Document document = promptTextField.getDocument();
		document.addUndoableEditListener(new UndoableEditListener() {

			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});
		// Add ActionListeners
		btnUndo.addActionListener((ActionEvent e) -> {
			try {
				undoManager.undo();
			} catch (CannotUndoException cue) {
			}
		});
		btnRedo.addActionListener((ActionEvent e) -> {
			try {
				undoManager.redo();
			} catch (CannotRedoException cre) {
			}
		});

		// Map undo action
		promptTextField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "undoKeyStroke");
		promptTextField.getActionMap().put("undoKeyStroke", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					undoManager.undo();
				} catch (CannotUndoException cue) {
				}
			}
		});

		// Map redo action
		promptTextField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "redoKeyStroke");
		promptTextField.getActionMap().put("redoKeyStroke", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					undoManager.redo();
				} catch (CannotRedoException cre) {
				}
			}
		});

	}

	/*************************************************************
	 * Undo redo pause and other actions for Response Pane
	 *************************************************************/
	private void createResponseUndoRedoPanelButtons() {
		undoRedoResponsePanel = new JPanel();
		GridBagConstraints gbc_undoRedoPanel = new GridBagConstraints();
		gbc_undoRedoPanel.insets = new Insets(0, 10, 5, 5);
		gbc_undoRedoPanel.fill = GridBagConstraints.BOTH;
		gbc_undoRedoPanel.gridx = 0;
		gbc_undoRedoPanel.gridy = 6;
		container.add(undoRedoResponsePanel, gbc_undoRedoPanel);

		Icon undoIcon = new ImageIcon("Images/undo-16.png");
		undoRedoResponsePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		undoRedoResponsePanel.setVisible(false);
		JButton btnUndo = new JButton("Undo", undoIcon);
		btnUndo.setFont(new Font("Tahoma", Font.BOLD, 14));
		// btnUndo.setSelectedIcon(new
		// ImageIcon("Images/undo-16.png"),JButton.LEFT);
		undoRedoResponsePanel.add(btnUndo);

		Icon redoIcon = new ImageIcon("Images/redo-16.png");
		JButton btnRedo = new JButton("Redo", redoIcon);
		btnRedo.setFont(new Font("Tahoma", Font.BOLD, 14));
		undoRedoResponsePanel.add(btnRedo);

		JButton btnPause = new JButton("Pause", null);
		btnPause.setFont(new Font("Tahoma", Font.BOLD, 14));
		undoRedoResponsePanel.add(btnPause);

		pauseActionResponse(btnPause);

		JComboBox comboBox = new JComboBox();
		comboBox.setRenderer(new CustomComboBoxRenderer("INSERT ACTION"));
		comboBox.setModel(
				new DefaultComboBoxModel(new String[] { "Play Audio File", "Display Character", "Display String" }));
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboBox.setSelectedIndex(-1);
		undoRedoResponsePanel.add(comboBox);

		comboBox.addItemListener(new ItemListener() {
			int count = 0;

			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					// count++;
					// logger.log(Level.INFO, "Cell Combo Box was used.");
					// logger.log(Level.INFO, "Cell Combo Box was used {0}
					// times", count);
				}
				int state = itemEvent.getStateChange();
				if (state == 1) {
					buttonEditor.requestFocus();
					buttonEditor.transferFocus();
					if (comboBox.getSelectedIndex() == 0) {
						// comboBox.setSelectedIndex(-1);
						addAudioToButton();
					} else if (comboBox.getSelectedIndex() == 1) {
						boolean checkChar = false;
						String input = null;
						while (!checkChar) {
							String inputValue = JOptionPane.showInputDialog("Enter a character");
							if (inputValue == null)
								checkChar = true;
							else {
								if (inputValue.length() == 1 && Character.isLetter(inputValue.charAt(0))) {
									checkChar = true;
									input = inputValue;
								} else {
									JOptionPane.showMessageDialog(null,
											"Error, enter one character. If you wish to display a string use that option");
								}
							}

						}
						if (input != null) {
							if (input != null) {
								boolean checkNumber = false;
								int cellNum = -1;
								while (!checkNumber) {
									String inputValue = JOptionPane
											.showInputDialog("Please input which cell to dispay it on");
									if (inputValue == null)
										checkNumber = true;
									else {
										try {
											cellNum = Integer.parseInt(inputValue);
											if (cellNum >= 1 && cellNum <= numCells) {
												checkNumber = true;
											} else {
												JOptionPane.showMessageDialog(null,
														"Error, enter a number between 1 and the number of cells.");
											}
										} catch (NumberFormatException exception) {
											// error
											JOptionPane.showMessageDialog(null,
													"Error, not a integer. Please try again.");
										}
									}

								}
								if (cellNum != -1) {
									setButtonText(buttonEditor.getText() + "\n/Display character " + input + " on cell "
											+ cellNum);
								}
							}
							// setButtonText(buttonEditor.getText() +
							// "\n/Display character " + input);
						}
						comboBox.setSelectedIndex(-1);
					} else if (comboBox.getSelectedIndex() == 2) {
						boolean checkStr = false;
						String input = null;
						while (!checkStr) {
							checkStr = true;
							String inputValue = JOptionPane.showInputDialog("Enter a string");
							input = inputValue;
							if (inputValue == null) {
								checkStr = true;
							} else {
								for (int i = 0; i < inputValue.length(); i++) {
									if (!Character.isLetter(inputValue.charAt(i))) {
										checkStr = false;
										input = null;
										JOptionPane.showMessageDialog(null,
												"Error, string must consist of letters and numbers only");
									}
								}
							}

						}
						if (input != null) {
							setButtonText(buttonEditor.getText() + "\n/Display string " + input);
						}
						comboBox.setSelectedIndex(-1);
					}
					updateButton();
				}
			}
		});
		// KeyBoard Shortcut "Ctrl+Shift+Z" for Undo on Response Section
		KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK | Event.SHIFT_MASK);
		// KeyBoard Shortcut "Ctrl+Shift+Y" for Redo on Response Section
		KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK | Event.SHIFT_MASK);

		UndoManager undoManager = new UndoManager();
		Document document = buttonEditor.getDocument();
		document.addUndoableEditListener(new UndoableEditListener() {

			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
			}
		});
		// Add ActionListeners
		btnUndo.addActionListener((ActionEvent e) -> {
			try {
				undoManager.undo();
			} catch (CannotUndoException cue) {
			}
		});
		btnRedo.addActionListener((ActionEvent e) -> {
			try {
				undoManager.redo();
			} catch (CannotRedoException cre) {
			}
		});

		// Map undo action
		buttonEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "undoKeyStroke");
		buttonEditor.getActionMap().put("undoKeyStroke", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					undoManager.undo();
				} catch (CannotUndoException cue) {
				}
			}
		});

		// Map redo action
		buttonEditor.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "redoKeyStroke");
		buttonEditor.getActionMap().put("redoKeyStroke", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					undoManager.redo();
				} catch (CannotRedoException cre) {
				}
			}
		});

	}

	private void pauseAction(JButton btnPause) {
		Action buttonAction = new AbstractAction("Pause") {
			int count = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				promptTextField.requestFocus();
				promptTextField.transferFocus();
				count++;
				// logger.log(Level.INFO, "Pause Button was pressed.");
				// logger.log(Level.INFO, "Pause Button was pressed {0} times",
				// count);
				// String inputValue = JOptionPane.showInputDialog("Please input
				// pause time in seconds");
				boolean checkNumber = false;
				int time = -1;
				while (!checkNumber) {
					String inputValue = JOptionPane.showInputDialog("Please input pause time in seconds");
					if (inputValue == null) {
						checkNumber = true;
					} else {
						try {
							time = Integer.parseInt(inputValue);
							if (time >= 0) {
								checkNumber = true;
							} else {
								JOptionPane.showMessageDialog(null, "Error, enter a positive integer.");
							}
						} catch (NumberFormatException exception) {
							// error
							JOptionPane.showMessageDialog(null, "Error, not a integer. Please try again.");
						}
					}

				}
				if (time != -1) {
					setPromptText(promptTextField.getText() + "\n/Wait for " + time + " second(s)");
					updatePrompt();
				}
			}
		};
		// Map pause action
		btnPause.setAction(buttonAction);
		// KeyBoard Shortcut "Ctrl+P" for Pause on Prompt Section
		btnPause.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK), "Pause");
		btnPause.getActionMap().put("Pause", buttonAction);
	}

	private void pauseActionResponse(JButton btnPause) {
		Action buttonAction = new AbstractAction("Pause") {
			int count = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				buttonEditor.requestFocus();
				buttonEditor.transferFocus();
				count++;
				// logger.log(Level.INFO, "Pause Button was pressed.");
				// logger.log(Level.INFO, "Pause Button was pressed {0} times",
				// count);
				// String inputValue = JOptionPane.showInputDialog("Please input
				// pause time in seconds");
				boolean checkNumber = false;
				int time = -1;
				while (!checkNumber) {
					String inputValue = JOptionPane.showInputDialog("Please input pause time in seconds");
					if (inputValue == null)
						checkNumber = true;
					else {
						try {
							time = Integer.parseInt(inputValue);
							if (time >= 0) {
								checkNumber = true;
							} else {
								JOptionPane.showMessageDialog(null, "Error, enter a positive integer.");
							}
						} catch (NumberFormatException exception) {
							// error
							JOptionPane.showMessageDialog(null, "Error, not a integer. Please try again.");
						}
					}

				}
				if (time != -1) {
					setButtonText(buttonEditor.getText() + "\n/Wait for " + time + " second(s)");
					updateButton();
				}
			}
		};
		// Map pause action
		btnPause.setAction(buttonAction);
		// KeyBoard Shortcut "Ctrl+Shift+P" for Pause on Response Section
		btnPause.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK), "Pause");
		btnPause.getActionMap().put("Pause", buttonAction);
	}

	/*************************************************************
	 * Undo redo pause
	 *************************************************************/

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		aViewFrame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("FILE");
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);
		mntmNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// to do: first check if unsaved work, close old window before
				// opening
				ScenarioForm sf = new ScenarioForm();
				sf.displayForm();
			}
		});

		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (buttonEdit == false) {
					buttonEditor.setText("");
				}
				if (promptEdit == false) {
					promptTextField.setText("");
				}
				JButton save = new JButton();
				JFileChooser fc = new JFileChooser() {
					@Override
					public void approveSelection() {
						File f = getSelectedFile();
						if (f.exists() && getDialogType() == SAVE_DIALOG) {
							int result = JOptionPane.showConfirmDialog(this,
									"The file already exists. Do you want to overwrite existing file?", "File Exists",
									JOptionPane.YES_NO_CANCEL_OPTION);
							switch (result) {
							case JOptionPane.YES_OPTION:
								super.approveSelection();
								return;
							case JOptionPane.NO_OPTION:
								return;
							case JOptionPane.CLOSED_OPTION:
								return;
							case JOptionPane.CANCEL_OPTION:
								cancelSelection();
								return;
							}
						}
						super.approveSelection();
					}
				};
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

		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);
		mntmOpen.addActionListener(new ActionListener() {
			// Currently just runs edit text from InitialView
			// to do: check if doc is saved before opening, close old window on
			// open
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						JButton open = new JButton();

						JFileChooser fc = new JFileChooser();
						FileFilter txtFilter = new FileFilter() {
							@Override
							public String getDescription() {
								return "Text file (*.TXT)";
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
						fc.setCurrentDirectory(new java.io.File("./FactoryScenarios"));
						fc.setDialogTitle("Please Choose File to Open");
						fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
						if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
							FileToCardsParser f = new FileToCardsParser();
							f.setFile(fc.getSelectedFile().getPath());
							AuthoringViewerTest ap = new AuthoringViewerTest(f.getCells(), f.getButtons(), f.getCards(),
									f.getInitial(), f.getEnding()); // new
																	// ActionListener()
																	// {public
																	// void
																	// actionPerformed(ActionEvent
																	// e2) {}});
							ap.setPromptText(f.getCards().get(0).getText());
							ap.setCurrCellPins(f.getCards().get(0).getCells().get(0));
							ap.setButtonText(f.getCards().get(0).getButtonList().get(0).getText());
							ap.setCardList();
							ap.setEdited();
						}

					}
				}).start();
			}
		});

		JMenuItem mntmTest = new JMenuItem("Test");
		mnFile.add(mntmTest);
		mntmTest.addActionListener(new ActionListener() {
			@Override
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
							@Override
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

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {
			@Override
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

		JMenu mnEdit = new JMenu("EDIT");
		menuBar.add(mnEdit);

		JMenuItem mntmScenarioForm = new JMenuItem("Scenario Form");
		mnEdit.add(mntmScenarioForm);
		mntmScenarioForm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (buttonEdit == false) {
					buttonEditor.setText("");
				}
				if (promptEdit == false) {
					promptTextField.setText("");
				}
				updateButton();
				updatePrompt();
				updateCell();
				updateResponseCell();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ScenarioForm window = new ScenarioForm(cards, numCells, numButtons, title);
							window.sCreatorFrame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				aViewFrame.dispose();
			}
		});

		JMenu mnView = new JMenu("VIEW");
		menuBar.add(mnView);

		JMenuItem mntmFullscreen = new JMenuItem("Fullscreen");
		mnView.add(mntmFullscreen);
		mntmFullscreen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		JMenu mnAudio = new JMenu("AUDIO");
		menuBar.add(mnAudio);

		JMenuItem mntmRecord = new JMenuItem("Record");
		mnAudio.add(mntmRecord);

		JMenu mnInsert = new JMenu("Insert...");
		mnAudio.add(mnInsert);

		JMenuItem mntmToPrompt = new JMenuItem("to Prompt");
		mnInsert.add(mntmToPrompt);
		mntmToPrompt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				promptTextField.requestFocus();
				promptTextField.transferFocus();
				addAudioToPrompt();
				updatePrompt();
			}
		});

		JMenuItem mntmToButton = new JMenuItem("to Button");
		mnInsert.add(mntmToButton);
		mntmToButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAudioToButton();
			}
		});

		mntmRecord.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RecorderFrame rf = new RecorderFrame();
				RecorderFrame.displayRecorder();
			}
		});

		JMenu mnHelp = new JMenu("HELP");
		menuBar.add(mnHelp);

		JMenuItem mntmTutorial = new JMenuItem("Tutorial");
		mnHelp.add(mntmTutorial);
		mntmTutorial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// launch browser to [INSERT YOUTUBE TUTORIAL LINK]
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("https://youtu.be/cIADGmL0yfI"));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		JMenu mnAbout = new JMenu("ABOUT");
		menuBar.add(mnAbout);

		JMenuItem mntmGithub = new JMenuItem("Github");
		mnAbout.add(mntmGithub);
		mntmGithub.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// launch browser to https://github.com/NS-01/forked_enamel
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("https://github.com/NS-01/forked_enamel"));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		JMenuItem mntmCourseWebsite = new JMenuItem("Course Website");
		mnAbout.add(mntmCourseWebsite);
		mntmCourseWebsite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// launch browser to
				// https://wiki.eecs.yorku.ca/course_archive/2017-18/W/2311/
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop()
								.browse(new URI("https://wiki.eecs.yorku.ca/course_archive/2017-18/W/2311/"));
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	private void createPrevNextButtons() {
		JButton btnPreviousCard = new JButton("Previous Card");
		btnPreviousCard.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnPreviousCard.getAccessibleContext().setAccessibleDescription("Click to switch to the previus card");
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

		JButton btnNextCard = new JButton("Next Card");
		btnNextCard.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNextCard.getAccessibleContext().setAccessibleDescription("Click to switch to the next card");
		btnNextCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				// buttonEditor.setEnabled(false);
				// buttonPanel.setVisible(false);
				// generalCellPanel.setVisible(false);
				// undoRedoPanel.setVisible(false);
				if (cards.size() > currCard + 1) {
					nextCard();
				} else {
					System.out.println(cards.size());
					Card temp = new Card(currCard + 1, "Card " + (currCard + 2), "", false);
					cards.add(temp);
					temp.getButtonList().add(new DataButton(0));
					temp.getCells().add(new BrailleCell());
					nextCard();
				}
			}
		});

		JPanel prevAndNextPanel = new JPanel();
		prevAndNextPanel.setBackground(new Color(217, 217, 217));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(10, 5, 10, 10);
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 5;
		prevAndNextPanel.setLayout(new BorderLayout(5, 5));

		JPanel secondaryPrevNextPanel = new JPanel();
		secondaryPrevNextPanel.setBackground(new Color(217, 217, 217));
		secondaryPrevNextPanel.setLayout(new BorderLayout(0, 0));
		secondaryPrevNextPanel.add(prevAndNextPanel, BorderLayout.SOUTH);

		prevAndNextPanel.add(btnPreviousCard, BorderLayout.NORTH);
		prevAndNextPanel.add(btnNextCard, BorderLayout.SOUTH);
		container.add(secondaryPrevNextPanel, gbc_panel);
	}

	private void createResponsePanel() {
		JPanel panel = new JPanel();
		GridBagConstraints responseText = new GridBagConstraints();
		responseText.insets = new Insets(10, 10, 10, 5);
		responseText.fill = GridBagConstraints.BOTH;
		responseText.gridx = 0;
		responseText.gridy = 5;

		buttonEditor = new JEditorPane();
		buttonEditor.setEnabled(false);
		buttonEditor.setBackground(new Color(230, 230, 230));
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

		JScrollPane buttonPane = new JScrollPane(buttonEditor, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		container.add(buttonPane, responseText);
	}

	private void createButtonLabelPanel() {

		JPanel cardNamePanel = new JPanel();
		cardNamePanel.setBackground(new Color(217, 217, 217));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(10, 5, 5, 10);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		container.add(cardNamePanel, gbc_panel_1);

		txtCardName = new JTextField();
		txtCardName.setColumns(3);
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
		cardNamePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		JLabel nameLabel = new JLabel("Card Name: ");
		nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		cardNamePanel.add(nameLabel);

		txtCardName.setToolTipText("Enter a name for the card");
		txtCardName.setText(cards.get(currCard).getName());
		txtCardName.setColumns(10);
		cardNamePanel.add(txtCardName);

		JPanel buttonLabelPanel = new JPanel();
		GridBagConstraints gbc_buttonLabelPanel = new GridBagConstraints();
		gbc_buttonLabelPanel.insets = new Insets(20, 10, 5, 5);
		gbc_buttonLabelPanel.fill = GridBagConstraints.BOTH;
		gbc_buttonLabelPanel.gridx = 0;
		gbc_buttonLabelPanel.gridy = 4;
		container.add(buttonLabelPanel, gbc_buttonLabelPanel);
		buttonLabelPanel.setLayout(new BorderLayout(0, 5));

		JLabel lblButtons = new JLabel("BUTTONS");
		lblButtons.setFont(new Font("Tahoma", Font.BOLD, 14));
		buttonLabelPanel.add(lblButtons, BorderLayout.NORTH);
		buttonLabelPanel.setBackground(new Color(217, 217, 217));

		buttonPanel = new JPanel();
		buttonPanel.setVisible(false);
		buttonLabelPanel.add(buttonPanel, BorderLayout.CENTER);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

		if (this.numButtons >= 1) {
			JButton button = new JButton("     1     ");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switchButton(0);
				}
			});
			buttonPanel.add(button);
		}
		if (this.numButtons >= 2) {
			JButton button_1 = new JButton("     2     ");
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switchButton(1);
				}

			});
			buttonPanel.add(button_1);
		}
		if (this.numButtons >= 3) {
			JButton button_2 = new JButton("     3     ");
			button_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switchButton(2);
				}
			});
			buttonPanel.add(button_2);
		}
		if (this.numButtons >= 4) {
			JButton button_3 = new JButton("     4     ");
			button_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switchButton(3);
				}
			});
			buttonPanel.add(button_3);
		}
		if (this.numButtons >= 5) {
			JButton button_4 = new JButton("     5     ");
			button_4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switchButton(4);
				}
			});
			buttonPanel.add(button_4);
		}
		if (this.numButtons >= 6) {
			JButton button_5 = new JButton("     6     ");
			button_5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switchButton(5);
				}
			});
			buttonPanel.add(button_5);
		}
	}

	private void createListPanel() {
		JPanel listPanel = new JPanel();
		listPanel.setBackground(new Color(217, 217, 217));
		// panel3.setBounds(50,10,200,100);
		GridBagConstraints gbc_listPanel = new GridBagConstraints();
		gbc_listPanel.insets = new Insets(10, 5, 5, 10);
		gbc_listPanel.fill = GridBagConstraints.BOTH;
		gbc_listPanel.gridx = 2;
		gbc_listPanel.gridy = 0;
		gbc_listPanel.gridheight = 2;
		container.add(listPanel, gbc_listPanel);

		listModel = new DefaultListModel();
		listModel.addElement("Hello:");
		list = new JList(listModel);
		list.getAccessibleContext().setAccessibleDescription("Card Order List");
		listPanel.setLayout(new BorderLayout(10, 10));
		list.setToolTipText("Card Order List");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				if (evt.getClickCount() == 2) {
					int index = list.locationToIndex(evt.getPoint());
					if (currCard < index) {
						for (int i = currCard; i < index; i++) {
							nextCard();
						}
					} else {
						for (int i = currCard; i > index; i--) {
							prevCard();
						}
					}

				} else if (evt.getClickCount() == 3) {

					// Triple-click detected
					int index = list.locationToIndex(evt.getPoint());
				}
			}
		});

		JScrollPane listScroller = new JScrollPane(list);
		listPanel.add(listScroller);
		JLabel lblOrder = new JLabel("ORDER");
		listScroller.setColumnHeaderView(lblOrder);

		JButton btnCardUp = new JButton("Card Up");
		btnCardUp.setFont(new Font("Tahoma", Font.BOLD, 14));
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

		JButton btnCardDown = new JButton("Card Down");
		btnCardDown.setFont(new Font("Tahoma", Font.BOLD, 14));
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

		JPanel listButtonPanel = new JPanel();
		listButtonPanel.setBackground(new Color(217, 217, 217));
		listButtonPanel.setLayout(new BorderLayout(5, 5));
		listPanel.add(listButtonPanel, BorderLayout.SOUTH);
		listButtonPanel.add(btnCardUp, BorderLayout.NORTH);
		listButtonPanel.add(btnCardDown, BorderLayout.SOUTH);
	}

	// TEST
	private void displayFrame() {
		setVisible(cards.get(currCard).getEnbled());
		aViewFrame.setBackground(new Color(255, 255, 255));
		aViewFrame.setTitle("AuthoringApp View: " + initialPrompt);
		aViewFrame.getAccessibleContext().setAccessibleDescription("Authoring App Editor");
		aViewFrame.setBounds(0, 0, 1170, 780);
		aViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		aViewFrame.addWindowListener(new confirmClose());
		aViewFrame.setLocationRelativeTo(null);
		aViewFrame.setVisible(true);
	}

	private void setUpFrame() {
		container = new JPanel();
		aViewFrame = new JFrame();
		aViewFrame.setResizable(true);
		this.aViewFrame.setLocationByPlatform(true);
		container.setBackground(new Color(217, 217, 217));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 500, 270, 300 };
		gridBagLayout.rowHeights = new int[] { 20, 250, 8, 50, 100, 250, 50 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE, 0.1 };
		gridBagLayout.rowWeights = new double[] { Double.MIN_VALUE, 1.0, Double.MIN_VALUE, Double.MIN_VALUE,
				Double.MIN_VALUE, 2.0, Double.MIN_VALUE };
		container.setLayout(gridBagLayout);
		JScrollPane jsp = new JScrollPane(container);
		// aViewFrame.getContentPane().add(jsp);
		aViewFrame.add(jsp);
	}

	private void createResponseCell() {
		generalCellPanel = new JPanel();
		generalCellPanel.repaint();
		generalCellPanel.setBackground(new Color(217, 217, 217));
		container.add(generalCellPanel);
		generalCellPanel.setLayout(null);
		// generalCellPanel.setEnabled(enable);

		JButton button_6 = new JButton("<");
		button_6.setFont(new Font("Tahoma", Font.BOLD, 14));
		button_6.getAccessibleContext().setAccessibleDescription("Go to previous cell to change raised pins");
		button_6.setToolTipText("Left Cell Button");
		// button_6.setEnabled(enable);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (responseCell == 0) {
					JOptionPane.showMessageDialog(null, "You are already at the first cell", "Alert",
							JOptionPane.ERROR_MESSAGE);
				} else {
					updateResponseCell();
					responseCell--;
					setResponseCellPins(
							cards.get(currCard).getButtonList().get(currButton).getCells().get(responseCell));
					responseCellLabel.setText("CELL: " + (responseCell + 1) + "/" + numCells);
				}
			}
		});
		button_6.setBounds(5, 70, 60, 40);
		generalCellPanel.add(button_6);

		JButton button_7 = new JButton(">");
		// button_7.setEnabled(enable);
		button_7.setFont(new Font("Tahoma", Font.BOLD, 14));
		button_7.getAccessibleContext().setAccessibleDescription("Go to Next cell within button to change raised pins");
		button_7.setToolTipText("Right Cell Button");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (responseCell + 1 == numCells) {
					JOptionPane.showMessageDialog(null, "You are already at the last cell", "Alert",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (cards.get(currCard).getButtonList().get(currButton).getCells().size() > responseCell + 1) {
						updateResponseCell();
						responseCell++;
						setResponseCellPins(
								cards.get(currCard).getButtonList().get(currButton).getCells().get(responseCell));
						responseCellLabel.setText("CELL: " + (responseCell + 1) + "/" + numCells);
					} else {
						BrailleCell temp = new BrailleCell();
						cards.get(currCard).getButtonList().get(currButton).getCells().add(temp);

						updateResponseCell();
						responseCell++;
						setResponseCellPins(
								cards.get(currCard).getButtonList().get(currButton).getCells().get(responseCell));
						responseCellLabel.setText("CELL: " + (responseCell + 1) + "/" + numCells);
					}
				}

			}
		});
		button_7.setBounds(150, 70, 60, 40);
		generalCellPanel.add(button_7);

		JPanel cellPanel = new JPanel();
		cellPanel.setBounds(70, 20, 75, 140);
		generalCellPanel.add(cellPanel);
		cellPanel.setLayout(null);
		// cellPanel.setEnabled(enable);

		rspOne = new JRadioButton("");
		rspOne.setToolTipText("Pin One");
		rspOne.setBounds(6, 6, 28, 23);
		cellPanel.add(rspOne);
		// rspOne.setEnabled(enable);

		rspFour = new JRadioButton("");
		rspFour.setToolTipText("Pin Four");
		rspFour.setBounds(46, 6, 28, 23);
		cellPanel.add(rspFour);
		// rspFour.setEnabled(enable);

		rspTwo = new JRadioButton("");
		rspTwo.setToolTipText("Pin  Two");
		rspTwo.setBounds(6, 41, 28, 23);
		cellPanel.add(rspTwo);
		// rspTwo.setEnabled(enable);

		rspFive = new JRadioButton("");
		rspFive.setToolTipText("Pin  Five");
		rspFive.setBounds(46, 41, 28, 23);
		cellPanel.add(rspFive);
		// rspFive.setEnabled(enable);

		rspThree = new JRadioButton("");
		rspThree.setToolTipText("Pin  Three");
		rspThree.setBounds(6, 76, 28, 23);
		cellPanel.add(rspThree);
		// rspThree.setEnabled(enable);

		rspSix = new JRadioButton("");
		rspSix.setToolTipText("Pin  Six");
		rspSix.setBounds(46, 76, 28, 23);
		cellPanel.add(rspSix);
		// rspSix.setEnabled(enable);

		rspSeven = new JRadioButton("");
		rspSeven.setToolTipText("Pin  Seven");
		rspSeven.setBounds(6, 111, 28, 23);
		cellPanel.add(rspSeven);
		// rspSeven.setEnabled(enable);

		rspEight = new JRadioButton("");
		rspEight.setToolTipText("Pin  Eight");
		rspEight.setBounds(46, 111, 28, 23);
		cellPanel.add(rspEight);
		// rspEight.setEnabled(enable);

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(10, 5, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 5;
		container.add(generalCellPanel, gbc_panel);
		responseCellLabel = new JLabel("CELL: 1/" + this.numCells);
		responseCellLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		responseCellLabel.setBounds(70, 0, 104, 15);
		generalCellPanel.add(responseCellLabel);
		generalCellPanel.setVisible(false);

		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<NEW: TESTING
		// REQUIRED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		JButton rspRaisePins = new JButton("Raise Pins");
		rspRaisePins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { ////////////////////////////////////////////////////////////////////////////////////////////////
				String inputValue = updateResponseCell();
				setButtonText(buttonEditor.getText() + "\n/Pins on " + (responseCell + 1) + ": " + inputValue);
				updateResponseCell();
				updateButton();
			}
		});
		rspRaisePins.setBounds(54, 165, 114, 23);
		generalCellPanel.add(rspRaisePins);

		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<NEW: TESTING
		// REQUIRED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		JButton rspReset = new JButton("Reset");
		rspReset.setBounds(54, 195, 114, 23);
		rspReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { ////////////////////////////////////////////////////////////////////////////////////////////////
				String inputValue = resetResponseCurrCellPins();
				setButtonText(buttonEditor.getText() + "\n/Clear all pins");
				updateCell();
				updatePrompt();
			}
		});
		generalCellPanel.add(rspReset);
	}

	private void createPromptCell() {
		JPanel generalCellPanel = new JPanel();
		generalCellPanel.setBackground(new Color(217, 217, 217));
		container.add(generalCellPanel);
		generalCellPanel.setLayout(null);

		JButton button_6 = new JButton("<");
		button_6.setFont(new Font("Tahoma", Font.BOLD, 14));
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
					promptCellLabel.setText("CELL: " + (currCell + 1) + "/" + numCells);
				}
			}
		});
		button_6.setBounds(5, 70, 60, 40);
		generalCellPanel.add(button_6);

		JButton button_7 = new JButton(">");
		button_7.setFont(new Font("Tahoma", Font.BOLD, 14));
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
						promptCellLabel.setText("CELL: " + (currCell + 1) + "/" + numCells);
					} else {
						BrailleCell temp = new BrailleCell();
						cards.get(currCard).getCells().add(temp);
						System.out.println(cards.get(currCard).getCells().size());
						updateCell();
						currCell++;
						setCurrCellPins(cards.get(currCard).getCells().get(currCell));
						promptCellLabel.setText("CELL: " + (currCell + 1) + "/" + numCells);
					}
				}

			}
		});
		button_7.setBounds(150, 70, 60, 40);
		generalCellPanel.add(button_7);

		JPanel cellPanel = new JPanel();
		cellPanel.setBounds(70, 20, 75, 140);
		generalCellPanel.add(cellPanel);
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

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(10, 5, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 1;
		container.add(generalCellPanel, gbc_panel);
		promptCellLabel = new JLabel("CELL: 1/" + this.numCells);
		promptCellLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		promptCellLabel.setBounds(70, 0, 98, 15);
		generalCellPanel.add(promptCellLabel);

		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<NEW: TESTING
		// REQUIRED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		JButton btnRaisePins = new JButton("Raise Pins");
		btnRaisePins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { ////////////////////////////////////////////////////////////////////////////////////////////////
				String inputValue = updateResponseCell();
				setPromptText(promptTextField.getText() + "\n/Pins on " + (currCell + 1) + ": " + inputValue);
				updateResponseCell();
				updatePrompt();
			}
		});
		btnRaisePins.setBounds(54, 165, 114, 23);
		generalCellPanel.add(btnRaisePins);

		// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<NEW: TESTING
		// REQUIRED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(54, 195, 114, 23);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { ////////////////////////////////////////////////////////////////////////////////////////////////
				String inputValue = resetCurrCellPins();
				setPromptText(promptTextField.getText() + "\n/Clear all pins");
				updateCell();
				updatePrompt();
			}
		});
		generalCellPanel.add(btnReset);
	}

	private void createPromptTextField() {
		GridBagConstraints promptText = new GridBagConstraints();
		promptText.insets = new Insets(10, 10, 5, 5);
		promptText.fill = GridBagConstraints.BOTH;
		promptText.gridx = 0;
		promptText.gridy = 1;
		promptTextField = new JEditorPane();
		promptTextField.setText(cards.get(currCard).getText());
		if (promptTextField.getText().equals(""))
			promptTextField.setText("Enter a Prompt for this card");
		promptTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				promptTextField.setText(cards.get(currCard).getText());
				promptEdit = true;
			}

			@Override
			public void focusLost(FocusEvent e) {
				updatePrompt();
			}
		});
		JScrollPane promptPane = new JScrollPane(promptTextField);
		promptPane.setBounds(10, 50, 450, 300);
		container.add(promptPane, promptText);

		JLabel lblPrompt = new JLabel("PROMPT");
		lblPrompt.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPrompt.setBounds(10, 40, 30, 30);
		promptPane.setColumnHeaderView(lblPrompt);
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
	 * Action for settings button. Method not in use since mid-term submission.
	 * 
	 * @param settingsButton
	 */
	private void settingsAction(JButton settingsButton) {
		Action buttonAction = new AbstractAction("Settings") {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (buttonEdit == false) {
					buttonEditor.setText("");
				}
				if (promptEdit == false) {
					promptTextField.setText("");
				}
				updateButton();
				updatePrompt();
				updateCell();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ScenarioForm window = new ScenarioForm(cards, numCells, numButtons, title);
							window.sCreatorFrame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				aViewFrame.dispose();
			}
		};
		settingsButton.setAction(buttonAction);
		settingsButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), "Settings");
		settingsButton.getActionMap().put("Settings", buttonAction);
	}

	/**
	 * Method to set Prompt text
	 *
	 * @param text
	 */
	public void setPromptText(String text) {
		promptTextField.setText(text);
	}

	/**
	 * Method to set the pins of braille cell
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

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<NEW: TESTING
	// REQUIRED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/**
	 * Reset the braille cell and type the string to prompt text view
	 * 
	 * @return
	 */
	public String resetCurrCellPins() {
		BrailleCell temp = new BrailleCell();
		temp.clear();
		String s = "";
		pOne.setSelected(false);
		pTwo.setSelected(false);
		pThree.setSelected(false);
		pFour.setSelected(false);
		pFive.setSelected(false);
		pSix.setSelected(false);
		pSeven.setSelected(false);
		pEight.setSelected(false);
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
		return s;
	}

	/**
	 * Reset the braille cell and type the string to prompt text view
	 * 
	 * @return
	 */
	public String resetResponseCurrCellPins() {
		BrailleCell temp = new BrailleCell();
		temp.clear();
		String s = "";
		rspOne.setSelected(false);
		rspTwo.setSelected(false);
		rspThree.setSelected(false);
		rspFour.setSelected(false);
		rspFive.setSelected(false);
		rspSix.setSelected(false);
		rspSeven.setSelected(false);
		rspEight.setSelected(false);
		s += rspOne.isSelected() ? "1" : "0";
		s += rspTwo.isSelected() ? "1" : "0";
		s += rspThree.isSelected() ? "1" : "0";
		s += rspFour.isSelected() ? "1" : "0";
		s += rspFive.isSelected() ? "1" : "0";
		s += rspSix.isSelected() ? "1" : "0";
		s += rspSeven.isSelected() ? "1" : "0";
		s += rspEight.isSelected() ? "1" : "0";
		temp.setPins(s);
		cards.get(currCard).getButtonList().get(currButton).getCells().set(currCell, temp);
		return s;
	}

	/**
	 * Method to set the pins of braille cell on response cell
	 *
	 * @param cell
	 */
	public void setResponseCellPins(BrailleCell cell) {
		rspOne.setSelected(cell.getPinState(0));
		rspTwo.setSelected(cell.getPinState(1));
		rspThree.setSelected(cell.getPinState(2));
		rspFour.setSelected(cell.getPinState(3));
		rspFive.setSelected(cell.getPinState(4));
		rspSix.setSelected(cell.getPinState(5));
		rspSeven.setSelected(cell.getPinState(6));
		rspEight.setSelected(cell.getPinState(7));
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
		cards.get(currCard).setText(promptTextField.getText());
	}

	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Changed: TESTING
	// REQUIRED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/**
	 * Method to update braille cell
	 */
	public String updateCell() {
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
		if (cards.get(currCard).getCells().isEmpty()) {
			cards.get(currCard).getCells().add(new BrailleCell());
		}
		cards.get(currCard).getCells().set(currCell, temp);
		return s;
	}

	/**
	 * Method to update response braille cell
	 */
	public String updateResponseCell() {
		BrailleCell temp = new BrailleCell();
		String s = "";
		s += rspOne.isSelected() ? "1" : "0";
		s += rspTwo.isSelected() ? "1" : "0";
		s += rspThree.isSelected() ? "1" : "0";
		s += rspFour.isSelected() ? "1" : "0";
		s += rspFive.isSelected() ? "1" : "0";
		s += rspSix.isSelected() ? "1" : "0";
		s += rspSeven.isSelected() ? "1" : "0";
		s += rspEight.isSelected() ? "1" : "0";
		temp.setPins(s);
		if (cards.get(currCard).getButtonList().get(currButton).getCells().isEmpty()) {
			cards.get(currCard).getButtonList().get(currButton).getCells().add(new BrailleCell());
		}
		cards.get(currCard).getButtonList().get(currButton).getCells().set(responseCell, temp);
		return s;
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
		if (buttonEdit == false) {
			buttonEditor.setText("");
		}
		if (promptEdit == false) {
			promptTextField.setText("");
		}
		updateButton();
		updatePrompt();
		updateCell();
		updateResponseCell();
		setCurrCellPins(cards.get(currCard).getCells().get(currCell));
		cards.get(currCard);
		cards.get(currCard).setName(txtCardName.getText());
		currCard++;
		currButton = 0;
		currCell = 0;
		responseCell = 0;
		if (cards.get(currCard).getButtonList().isEmpty()) {
			cards.get(currCard).getButtonList().add(new DataButton(0));
		}
		promptCellLabel.setText("CELL: 1/" + this.numCells);
		responseCellLabel.setText("CELL: 1/" + this.numCells);
		this.setButtonText(cards.get(currCard).getButtonList().get(0).getText());
		txtCardName.setText(cards.get(currCard).getName());
		showPrompt();
		setCurrCellPins(cards.get(currCard).getCells().get(currCell));
		setResponseCellPins(cards.get(currCard).getButtonList().get(currButton).getCells().get(0));
		setCardList();
		setVisible(cards.get(currCard).getEnbled());
	}

	private void setVisible(Boolean b) {
		buttonEditor.setEnabled(b);
		if (b)
			buttonEditor.setBackground(Color.WHITE);
		else
			buttonEditor.setBackground(new Color(230, 230, 230));
		buttonPanel.setVisible(b);
		generalCellPanel.setVisible(b);
		undoRedoResponsePanel.setVisible(b);
	}

	/**
	 * Method to go to previous card
	 */
	public void prevCard() {
		updateButton();
		updatePrompt();
		updateCell();
		updateResponseCell();
		setCurrCellPins(cards.get(currCard).getCells().get(currCell));
		cards.get(currCard).setName(txtCardName.getText());
		currCard--;
		currButton = 0;
		currCell = 0;
		responseCell = 0;
		promptCellLabel.setText("CELL: 1/" + this.numCells);
		responseCellLabel.setText("CELL: 1/" + this.numCells);
		this.setButtonText(cards.get(currCard).getButtonList().get(0).getText());
		txtCardName.setText(cards.get(currCard).getName());
		showPrompt();
		setCurrCellPins(cards.get(currCard).getCells().get(currCell));
		setResponseCellPins(cards.get(currCard).getButtonList().get(currButton).getCells().get(0));
		setCardList();
		setVisible(cards.get(currCard).getEnbled());
	}

	/**
	 * Method to set edited booleans
	 */
	public void setEdited() {
		this.buttonEdit = true;
		this.promptEdit = true;
	}

	private void switchButton(int buttonNum) {
		updateResponseCell();
		showButtonText(buttonNum);
		responseCell = 0;
		setResponseCellPins(cards.get(currCard).getButtonList().get(currButton).getCells().get(responseCell));
		responseCellLabel.setText("CELL: 1/" + numCells);
	}

	/**
	 * Adds action to insert an audio file to a button
	 */
	private void addAudioToButton() {
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
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
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
					setButtonText(buttonEditor.getText() + "\n/Play sound file " + (temp));
					updateButton();
					cards.get(currCard).getButtonList().get(currButton).setAudio(temp);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please select a .wav file", "Alert", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * Adds action to insert an audio file to Prompt
	 */
	private void addAudioToPrompt() {
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
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			String temp = fc.getSelectedFile().getName().toString();

			if (temp.length() > 4) {
				if (!temp.toLowerCase().endsWith(".wav")) {
					JOptionPane.showMessageDialog(null, "Please select a .wav file", "Alert",
							JOptionPane.ERROR_MESSAGE);
				} else {
					setPromptText(promptTextField.getText() + "\n/Play sound file " + (temp));
					cards.get(currCard).setSound(temp);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please select a .wav file", "Alert", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private void displayCharacter(JComboBox comboBox) {
		boolean checkChar = false;
		String input = null;
		while (!checkChar) {
			String inputValue = JOptionPane.showInputDialog("Enter a character");
			if (inputValue == null)
				checkChar = true;
			else {
				if (inputValue.length() == 1 && Character.isLetter(inputValue.charAt(0))) {
					checkChar = true;
					input = inputValue;
				} else {
					JOptionPane.showMessageDialog(null,
							"Error, enter one character. If you wish to display a string use that option");
				}
			}

		}
		if (input != null) {
			boolean checkNumber = false;
			int cellNum = -1;
			while (!checkNumber) {
				String inputValue = JOptionPane.showInputDialog("Please input which cell to dispay it on");
				if (inputValue == null)
					checkNumber = true;
				else {
					try {
						cellNum = Integer.parseInt(inputValue);
						if (cellNum >= 1 && cellNum <= numCells) {
							checkNumber = true;
						} else {
							JOptionPane.showMessageDialog(null,
									"Error, enter a number between 1 and the number of cells.");
						}
					} catch (NumberFormatException exception) {
						// error
						JOptionPane.showMessageDialog(null, "Error, not a integer. Please try again.");
					}
				}

			}
			if (cellNum != -1) {
				setPromptText(promptTextField.getText() + "\n/Display character " + input + " on cell " + cellNum);
			}
		}
		comboBox.setSelectedIndex(-1);
	}

	private void displayString(JComboBox comboBox) {
		boolean checkStr = false;
		String input = null;
		while (!checkStr) {
			checkStr = true;
			String inputValue = JOptionPane.showInputDialog("Enter a string");
			input = inputValue;
			if (inputValue == null) {
				checkStr = true;
			} else {
				for (int i = 0; i < inputValue.length(); i++) {
					if (!Character.isLetter(inputValue.charAt(i))) {
						checkStr = false;
						input = null;
						JOptionPane.showMessageDialog(null, "Error, string must consist of letters and numbers only");
					}
				}
			}

		}
		if (input != null) {
			setPromptText(promptTextField.getText() + "\n/Display string " + input);
		}
		comboBox.setSelectedIndex(-1);
	}

	class CustomComboBoxRenderer extends JLabel implements ListCellRenderer {
		private String _title;

		public CustomComboBoxRenderer(String title) {
			_title = title;
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean hasFocus) {
			if (index == -1 && value == null)
				setText(_title);
			else
				setText(value.toString());
			return this;
		}
	}
}