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
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.TextField;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import java.awt.GridBagConstraints;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JRadioButton;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.lang.Object;
import java.net.URI;
import java.net.URISyntaxException;

import enamel.ScenarioParser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import java.awt.Panel;
import java.awt.SystemColor;
import java.awt.ScrollPane;

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
 *
 *
 *
 *         private JFrame aViewFrame; private int numCells = 1; private int
 *         numButtons = 1; private JLabel lblCurrCell; private JTextField
 *         txtCardName; private JTextField txtAudiofilenamemp; private
 *         JTextField textField; private JEditorPane dtrpnEnterAPrompt; private
 *         JRadioButton pOne; private JRadioButton pTwo; private JRadioButton
 *         pThree; private JRadioButton pFour; private JRadioButton pFive;
 *         private JRadioButton pSix; private JRadioButton pSeven; private
 *         JRadioButton pEight; private JEditorPane buttonEditor; private JList
 *         list; private DefaultListModel<String> listModel; private static
 *         String testPins;
 * 
 *         private String title;
 * 
 *         private int currButton; private int currCell; private int currCard;
 *         private ArrayList<Card> cards; private String initialPrompt; private
 *         String endingPrompt; private String path; private boolean promptEdit
 *         = false; private boolean buttonEdit = false;
 */
@SuppressWarnings({ "unused", "rawtypes", "unchecked", "static-access", "serial" })
public class AuthoringApp {

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
	private JRadioButton rspOne;
	private JRadioButton rspTwo;
	private JRadioButton rspThree;
	private JRadioButton rspFour;
	private JRadioButton rspFive;
	private JRadioButton rspSix;
	private JRadioButton rspSeven;
	private JRadioButton rspEight;
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
	private JScrollPane listScroller;
	// non zero

	/**
	 * Create the application.
	 */
	public AuthoringApp(int numCells, int numButtons, ArrayList<Card> cards, String initialPrompt,
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
		this.currCard = 0;

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		aViewFrame = new JFrame();
		aViewFrame.getContentPane().setBackground(new Color(217, 217, 217));
		aViewFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		aViewFrame.setResizable(true);
		aViewFrame.setBackground(new Color(255, 255, 5));
		aViewFrame.setTitle("Authoring App Editor");
		aViewFrame.getAccessibleContext().setAccessibleDescription("Authoring App Editor");
		aViewFrame.setBounds(100, 100, 1000, 612);
		aViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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
							AuthoringApp ap = new AuthoringApp(f.getCells(), f.getButtons(), f.getCards(),
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
				ScenarioForm sf = new ScenarioForm();
				sf.displayForm();
				// reload app with updated values
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

		JMenuItem mntmToCard = new JMenuItem("to Card");
		mnInsert.add(mntmToCard);

		JMenuItem mntmToButton = new JMenuItem("to Button");
		mnInsert.add(mntmToButton);
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
						Desktop.getDesktop().browse(new URI("https://youtube.com"));
					} catch (IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
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
						// TODO Auto-generated catch block
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
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		aViewFrame.addWindowListener(new confirmClose());

		aViewFrame.setVisible(true);

		JPanel center = new JPanel();
		aViewFrame.getContentPane().add(center, BorderLayout.CENTER);
		center.setLayout(new BorderLayout(0, 0));

		JPanel center_center = new JPanel();
		center.add(center_center, BorderLayout.CENTER);

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
		center_center.setLayout(new GridLayout(0, 1, 0, 0));
		JScrollPane promptPane = new JScrollPane(dtrpnEnterAPrompt);
		center_center.add(promptPane);
		promptPane.setBounds(250, 30, 478, 128);
		JLabel lblPrompt = new JLabel("PROMPT");
		promptPane.setColumnHeaderView(lblPrompt);

		JPanel center_south = new JPanel();
		center_center.add(center_south);
		center_south.setLayout(new GridLayout(3, 1, 0, 0));

		JLabel lblResponses = new JLabel("RESPONSES");
		lblResponses.setVerticalAlignment(SwingConstants.BOTTOM);
		center_south.add(lblResponses);

		JPanel buttonPanel = new JPanel();
		center_south.add(buttonPanel);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

		JLabel label = new JLabel("");
		buttonPanel.add(label);

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
		center_south.add(buttonPane);

		// determine how many buttons to display
		if (this.numButtons >= 1) {
			JButton button = new JButton("1");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(0);
				}
			});
			button.setSize(75, 30);
			buttonPanel.add(button);
		}
		if (this.numButtons >= 2) {
			JButton button_1 = new JButton("2");
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(1);
				}

			});
			button_1.setSize(75, 30);
			buttonPanel.add(button_1);
		}
		if (this.numButtons >= 3) {
			JButton button_2 = new JButton("3");
			button_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(2);
				}
			});
			button_2.setSize(75, 30);
			buttonPanel.add(button_2);
		}
		if (this.numButtons >= 4) {
			JButton button_3 = new JButton("4");
			button_3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(3);
				}
			});
			button_3.setSize(75, 30);
			buttonPanel.add(button_3);
		}
		if (this.numButtons >= 5) {
			JButton button_4 = new JButton("5");
			button_4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(4);
				}
			});
			button_4.setSize(75, 30);
			buttonPanel.add(button_4);
		}
		if (this.numButtons >= 6) {
			JButton button_5 = new JButton("6");
			button_5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showButtonText(5);
				}
			});
			button_5.setSize(75, 30);
			buttonPanel.add(button_5);
		}

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

		JPanel center_east = new JPanel();
		center.add(center_east, BorderLayout.EAST);
		center_east.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel cellPane = new JPanel();
		center_east.add(cellPane);

		JButton button_6 = new JButton("<");
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
		button_6.setToolTipText("Left Cell Button");
		cellPane.add(button_6);

		JPanel cellPanel = new JPanel();
		cellPanel.setBackground(SystemColor.inactiveCaption);
		cellPanel.setSize(70, 38);
		cellPane.add(cellPanel);
		cellPanel.setLayout(new GridLayout(0, 2, 0, 0));

		pOne = new JRadioButton("");
		pOne.setToolTipText("Pin One");
		cellPanel.add(pOne);

		pFour = new JRadioButton("");
		pFour.setToolTipText("Pin Four");
		cellPanel.add(pFour);

		pTwo = new JRadioButton("");
		pTwo.setToolTipText("Pin  Two");
		cellPanel.add(pTwo);

		pFive = new JRadioButton("");
		pFive.setToolTipText("Pin  Five");
		cellPanel.add(pFive);

		pThree = new JRadioButton("");
		pThree.setToolTipText("Pin  Three");
		cellPanel.add(pThree);

		pSix = new JRadioButton("");
		pSix.setToolTipText("Pin  Six");
		cellPanel.add(pSix);

		pSeven = new JRadioButton("");
		pSeven.setToolTipText("Pin  Seven");
		cellPanel.add(pSeven);

		pEight = new JRadioButton("");
		pEight.setToolTipText("Pin  Eight");
		cellPanel.add(pEight);

		JButton button_7 = new JButton(">");
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
		button_7.setToolTipText("Right Cell Button");
		cellPane.add(button_7);

		lblCurrCell = new JLabel("1/" + this.numCells);
		cellPane.add(lblCurrCell);
		lblCurrCell.setBounds(105, 189, 31, 16);
		Component verticalStrut = Box.createVerticalStrut(20);
		center_east.add(verticalStrut);

		JPanel responsePane = new JPanel();
		center_east.add(responsePane);
		responsePane.setLayout(new BorderLayout(0, 0));

		JPanel rspCellBorder = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) rspCellBorder.getLayout();
		flowLayout_1.setAlignOnBaseline(true);
		responsePane.add(rspCellBorder);

		JPanel rspCellPanel = new JPanel();
		rspCellBorder.add(rspCellPanel);
		rspCellPanel.setBackground(SystemColor.inactiveCaption);
		rspCellPanel.setSize(70, 38);
		rspCellPanel.setLayout(new GridLayout(0, 2, 0, 0));

		rspOne = new JRadioButton("");
		rspOne.setToolTipText("Pin One");
		rspCellPanel.add(rspOne);

		rspFour = new JRadioButton("");
		rspFour.setToolTipText("Pin Four");
		rspCellPanel.add(rspFour);

		rspTwo = new JRadioButton("");
		rspTwo.setToolTipText("Pin  Two");
		rspCellPanel.add(rspTwo);

		rspFive = new JRadioButton("");
		rspFive.setToolTipText("Pin  Five");
		rspCellPanel.add(rspFive);

		rspThree = new JRadioButton("");
		rspThree.setToolTipText("Pin  Three");
		rspCellPanel.add(rspThree);

		rspSix = new JRadioButton("");
		rspSix.setToolTipText("Pin  Six");
		rspCellPanel.add(rspSix);

		rspSeven = new JRadioButton("");
		rspSeven.setToolTipText("Pin  Seven");
		rspCellPanel.add(rspSeven);

		rspEight = new JRadioButton("");
		rspEight.setToolTipText("Pin  Eight");
		rspCellPanel.add(rspEight);

		JPanel functionPane = new JPanel();
		responsePane.add(functionPane, BorderLayout.SOUTH);
		functionPane.setLayout(new BoxLayout(functionPane, BoxLayout.X_AXIS));

		JButton bttnFunction = new JButton("Function");
		bttnFunction.setToolTipText("[INSERT TOOL TIP HERE]");
		functionPane.add(bttnFunction);

		JPanel east = new JPanel();
		aViewFrame.getContentPane().add(east, BorderLayout.EAST);
		east.setLayout(new GridLayout(4, 0, 0, 0));

		listModel = new DefaultListModel();
		list = new JList();
		list = new JList(listModel);
		list.getAccessibleContext().setAccessibleDescription("Card Order List");
		list.setToolTipText("Card Order List");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		listScroller = new JScrollPane(list);
		listScroller.setViewportView(list);
		listScroller.setAlignmentX(0.0f);
		east.add(listScroller);

		JLabel lblOrder = new JLabel("ORDER");
		listScroller.setColumnHeaderView(lblOrder);
		lblOrder.setVerticalAlignment(SwingConstants.BOTTOM);
		lblOrder.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel east_buttons = new JPanel();
		east.add(east_buttons);
		east_buttons.setLayout(new GridLayout(3, 3, 0, 0));

		JButton btnCardUp = new JButton("Card Up");
		btnCardUp.getAccessibleContext().setAccessibleDescription("Press to move selected card up in card list order");
		btnCardUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected = list.getSelectedIndex();
				System.out.println("****************************************************\n" + selected);
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
		east_buttons.add(btnCardUp);

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
		east_buttons.add(btnCardDown);

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
		east_buttons.add(txtCardName);

		JPanel south = new JPanel();
		aViewFrame.getContentPane().add(south, BorderLayout.SOUTH);
		south.setLayout(new BorderLayout(0, 0));

		Panel south_east = new Panel();
		FlowLayout flowLayout = (FlowLayout) south_east.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		south.add(south_east);

		JButton btnPrevCard = new JButton("Previous Card");
		btnPrevCard.getAccessibleContext().setAccessibleDescription(
				"Click to go back to the previus card u edited. By doing so your view will change to next card");
		btnPrevCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currCard == 0) {
					JOptionPane.showMessageDialog(null, "You are already at the first card", "Alert",
							JOptionPane.ERROR_MESSAGE);
				} else {
					prevCard();
				}
			}
		});
		south_east.add(btnPrevCard);

		JButton btnNextCard = new JButton("Next Card");
		btnNextCard.getAccessibleContext().setAccessibleDescription(
				"Click to add a new card to the list. By doing so your view will change to next card");
		btnNextCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cards.size() > currCard + 1) {
					nextCard();
				} else {
					System.out.println(cards.size());
					Card temp = new Card(currCard + 1, "Card " + (currCard + 2), "");
					cards.add(temp);
					System.out.println("New Size: " + cards.size());
					temp.getButtonList().add(new DataButton(0));
					temp.getCells().add(new BrailleCell());
					nextCard();
				}
			}
		});
		south_east.add(btnNextCard);

		Panel south_west = new Panel();
		south.add(south_west, BorderLayout.WEST);
		FlowLayout fl_south_west = (FlowLayout) south_west.getLayout();
		fl_south_west.setAlignment(FlowLayout.LEFT);

		JButton btnExit = new JButton("Exit");
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
		south_west.add(btnExit);

		JButton btnSave = new JButton("Save");
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
		south_west.add(btnSave);

		JButton btnTest = new JButton("Test");
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
		south_west.add(btnTest);
		JButton settingsButton = new JButton("Settings");
		settingsButton.getAccessibleContext().setAccessibleDescription("Creates a new file");
		settingsButton.setBounds(260, 525, 75, 50);
		settingsAction(settingsButton);
		south_west.add(settingsButton);

		txtCardName.requestFocus();

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
	 * Action for settings button
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
					dtrpnEnterAPrompt.setText("");
				}
				updateButton();
				updatePrompt();
				updateCell();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							ScenarioForm window = new ScenarioForm(cards, numCells, numButtons);
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