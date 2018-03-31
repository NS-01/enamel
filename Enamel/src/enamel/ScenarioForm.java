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
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

import java.awt.GridBagConstraints;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import java.awt.Insets;
import java.awt.ItemSelectable;

//import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.JPanel;
import java.awt.GridLayout;
import org.eclipse.wb.swing.FocusTraversalOnArray;

/**
 *
 * @author Jeremy, Nisha, Tyler
 *
 *         This Class allows user to do the initial setup before creating a
 *         scenario. The initial setup includes setting a tile, number of cells
 *         (1-10) number of buttons (1-6). User may also record audio or insert
 *         audio. Accessibility feature are added.
 *
 */
@SuppressWarnings({ "unused", "rawtypes", "serial" })
public class ScenarioForm {

	JFrame sCreatorFrame;
	private int numCells = 1; // assuming 1 selected by default. i.e. always non
	private int numButtons = 1; // assuming 1 selected by default. i.e. always
	private int selectedCells = 1;
	private int selectedButtons = 1;
	private JTextField titleTextField;
	private JTextField audioFileTextField;
	private JLabel lblNumberOfCells;
	private JComboBox<String> comboCellBox;
	private JLabel lblNumberOfButtons;
	private JComboBox<String> comboButtonBox;
	private ArrayList<Card> cards;
	// non zero

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Launch the application.
	 */
	public static void displayForm() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScenarioForm window = new ScenarioForm();
					window.sCreatorFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ScenarioForm() {
		this(new ArrayList<Card>(), 1, 1);
		initialize();
		// ConsoleHandler consoleHandler = new ConsoleHandler();
		// consoleHandler.setFormatter(new Formatter() {
		// private String format = "[%1$s] [%2$s] %3$s %n";
		// private SimpleDateFormat dateWithMillis = new
		// SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
		// @Override
		// public String format(LogRecord record) {
		// return String.format(format, dateWithMillis.format(new Date()),
		// record.getSourceClassName(), formatMessage(record));
		// }
		// });
		// logger.addHandler(consoleHandler);
		// logger.setUseParentHandlers(true);
	}

	/**
	 * @wbp.parser.constructor
	 * @param cards
	 * @param numCells
	 * @param numButtons
	 */
	public ScenarioForm(ArrayList<Card> cards, int numCells, int numButtons) {
		this.cards = cards;
		this.numButtons = numButtons;
		this.numCells = numCells;
		initialize();
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new Formatter() {
			private String format = "[%1$s] [%2$s] %3$s %n";
			private SimpleDateFormat dateWithMillis = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");

			@Override
			public String format(LogRecord record) {
				return String.format(format, dateWithMillis.format(new Date()), record.getSourceClassName(),
						formatMessage(record));
			}
		});

		/*
		 * logger.addHandler(consoleHandler); logger.setUseParentHandlers(true);
		 */
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		createFrame();

		// *****************************************************************************
		findDimensions();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 36, 144, 248, 130, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 30, 27, 0, 33, 21, 0, 21, 0, 27, 45, 0, 46, 11, 46, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, Double.MIN_VALUE };
		sCreatorFrame.getContentPane().setLayout(gridBagLayout);

		comboButtonBox = new JComboBox<String>();
		comboButtonBox.getAccessibleContext().setAccessibleDescription("Select number of buttons");

		// exit

		JLabel lblNewLabel = new JLabel("Initial Set Up");
		lblNewLabel.setFont(new Font("Cambria", Font.BOLD, 19));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 2;
		gbc_lblNewLabel.gridy = 1;
		sCreatorFrame.getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		// sCreatorFrame.getContentPane().setFocusTraversalPolicy(new
		// FocusTraversalOnArray(new Component[]{lblNewLabel, lblNumberOfCells,
		// comboCellBox, lblNumberOfButtons, comboButtonBox, lblScenarioTitle,
		// titleTextField, lblAddAudioFile, btnBrowse, audioFileTextField, lblS,
		// btnRecordAudio, btnSaveAndCreate, btnExitWithoutSaving}));
		// sCreatorFrame.getContentPane().setFocusTraversalPolicy(new
		// FocusTraversalOnArray(new Component[]{lblNewLabel, lblNumberOfCells,
		// comboCellBox, lblNumberOfButtons, comboButtonBox, lblScenarioTitle,
		// titleTextField, lblAddAudioFile, btnBrowse, audioFileTextField,
		// btnSaveAndCreate, btnExitWithoutSaving}));

		lblNumberOfCells = new JLabel("Number of Cells");
		lblNumberOfCells.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblNumberOfCells = new GridBagConstraints();
		gbc_lblNumberOfCells.fill = GridBagConstraints.BOTH;
		gbc_lblNumberOfCells.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfCells.gridx = 1;
		gbc_lblNumberOfCells.gridy = 4;
		sCreatorFrame.getContentPane().add(lblNumberOfCells, gbc_lblNumberOfCells);

		comboCellBox = new JComboBox<String>();
		comboCellBox.getAccessibleContext().setAccessibleDescription("Select number of cells");
		comboCellBox.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboCellBox.setModel(new DefaultComboBoxModel<String>(new String[] { "1 Cell", "2 Cells", "3 Cells", "4 Cells",
				"5 Cells", "6 Cells", "7 Cells", "8 Cells", "9 Cells", "10 Cells" }));
		GridBagConstraints gbc_comboCellBox = new GridBagConstraints();
		gbc_comboCellBox.fill = GridBagConstraints.BOTH;
		gbc_comboCellBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboCellBox.gridx = 3;
		gbc_comboCellBox.gridy = 4;
		sCreatorFrame.getContentPane().add(comboCellBox, gbc_comboCellBox);

		comboCellBox.addItemListener(new ItemListener() {
			int count = 0;

			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					count++;
					logger.log(Level.INFO, "Cell Combo Box was used.");
					logger.log(Level.INFO, "Cell Combo Box was used {0} times", count);
				}
				int state = itemEvent.getStateChange();
				ItemSelectable is = itemEvent.getItemSelectable();
				selectedCells = comboCellBox.getSelectedIndex() + 1;
				System.out.println("Selected: " + selectedString(is));
			}
		});
		lblNumberOfButtons = new JLabel("Number of Buttons");
		lblNumberOfButtons.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblNumberOfButtons = new GridBagConstraints();
		gbc_lblNumberOfButtons.fill = GridBagConstraints.BOTH;
		gbc_lblNumberOfButtons.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfButtons.gridx = 1;
		gbc_lblNumberOfButtons.gridy = 6;
		sCreatorFrame.getContentPane().add(lblNumberOfButtons, gbc_lblNumberOfButtons);
		comboButtonBox.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboButtonBox.setBackground(new Color(238, 238, 238));

		comboButtonBox.setModel(new DefaultComboBoxModel<String>(
				new String[] { "1 Button", "2 Buttons", "3 Buttons", "4 Buttons", "5 Buttons", "6 Buttons" }));
		GridBagConstraints gbc_comboButtonBox = new GridBagConstraints();
		gbc_comboButtonBox.fill = GridBagConstraints.BOTH;
		gbc_comboButtonBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboButtonBox.gridx = 3;
		gbc_comboButtonBox.gridy = 6;
		sCreatorFrame.getContentPane().add(comboButtonBox, gbc_comboButtonBox);

		comboButtonBox.addItemListener(new ItemListener() {
			int count = 0;

			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					count++;
					logger.log(Level.INFO, "Button Combo Box was used.");
					logger.log(Level.INFO, "Button Combo Box was used {0} times", count);
				}
				int state = itemEvent.getStateChange();
				ItemSelectable is = itemEvent.getItemSelectable();
				selectedButtons = comboButtonBox.getSelectedIndex() + 1;
				System.out.println("Selected: " + selectedString(is));
			}
		});

		JLabel lblScenarioTitle = new JLabel("Scenario Title");
		lblScenarioTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblScenarioTitle = new GridBagConstraints();
		gbc_lblScenarioTitle.fill = GridBagConstraints.BOTH;
		gbc_lblScenarioTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblScenarioTitle.gridx = 1;
		gbc_lblScenarioTitle.gridy = 8;
		sCreatorFrame.getContentPane().add(lblScenarioTitle, gbc_lblScenarioTitle);

		titleTextField = new JTextField();
		titleTextField.getAccessibleContext().setAccessibleDescription("Title of the scenario");
		titleTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		titleTextField.setToolTipText("Enter a Title for your Scenario");
		GridBagConstraints gbc_titleTextField = new GridBagConstraints();
		gbc_titleTextField.fill = GridBagConstraints.BOTH;
		gbc_titleTextField.insets = new Insets(0, 0, 5, 5);
		gbc_titleTextField.gridx = 3;
		gbc_titleTextField.gridy = 8;
		sCreatorFrame.getContentPane().add(titleTextField, gbc_titleTextField);
		titleTextField.setColumns(10);
		titleTextField.setText("New Scenario");


		JButton btnSaveAndCreate = new JButton("Create a Scenario");
		//btnSaveAndCreate.setEnabled(false);
		btnSaveAndCreate.getAccessibleContext().setAccessibleDescription("Saves information and opens editor");
		btnSaveAndCreate.setFont(new Font("Tahoma", Font.BOLD, 14));

		btnSaveAndCreate.setToolTipText("Saves information and opens editor");
		btnSaveAndCreate.setBackground(UIManager.getColor("CheckBox.background"));
		saveButtonListener(comboCellBox, comboButtonBox, btnSaveAndCreate);
		
/*		titleTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if (!titleTextField.getText().isEmpty()) {
					if (!titleTextField.getText().matches(".*\\w.*")) {
						btnSaveAndCreate.setEnabled(true);
						saveButtonListener(comboCellBox, comboButtonBox, btnSaveAndCreate);
					} else {
						btnSaveAndCreate.setEnabled(false);
					}
				}
				else {
					btnSaveAndCreate.setEnabled(false);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}
		});*/
//		titleTextField.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(jTextField1KeyPressed){
//					
//				}
//				/*// TODO Auto-generated method stub
//				if(!titleTextField.getText().isEmpty()){
//			//if(!titleTextField.getText().matches(".*\\w.*")){
//			btnSaveAndCreate.setEnabled(true);
//			saveButtonListener(comboCellBox, comboButtonBox, btnSaveAndCreate);
//		}else{btnSaveAndCreate.setEnabled(false);
//		//}
//		}*/
//				
//			}
//		});
//		
		GridBagConstraints gbc_btnSaveAndCreate = new GridBagConstraints();
		gbc_btnSaveAndCreate.fill = GridBagConstraints.BOTH;
		gbc_btnSaveAndCreate.insets = new Insets(0, 0, 5, 5);
		gbc_btnSaveAndCreate.gridx = 2;
		gbc_btnSaveAndCreate.gridy = 11;
		sCreatorFrame.getContentPane().add(btnSaveAndCreate, gbc_btnSaveAndCreate);
		// disable save if name not specified
		
		
		JButton btnExitWithoutSaving = new JButton("Exit Without Saving");
		btnExitWithoutSaving.getAccessibleContext().setAccessibleDescription("Doesn't save and closes current window");
		exitButtonListener(btnExitWithoutSaving);
		btnExitWithoutSaving.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnExitWithoutSaving.setToolTipText(" ");
		// =======
		//
		// >>>>>>> branch 'TestingUpdates' of
		// https://github.com/NS-01/forked_enamel
		GridBagConstraints gbc_btnExitWithoutSaving = new GridBagConstraints();
		gbc_btnExitWithoutSaving.fill = GridBagConstraints.BOTH;
		gbc_btnExitWithoutSaving.insets = new Insets(0, 0, 5, 5);
		gbc_btnExitWithoutSaving.gridx = 2;
		gbc_btnExitWithoutSaving.gridy = 13;
		sCreatorFrame.getContentPane().add(btnExitWithoutSaving, gbc_btnExitWithoutSaving);
		sCreatorFrame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblNewLabel, lblNumberOfCells, comboCellBox, lblNumberOfButtons, comboButtonBox, lblScenarioTitle, titleTextField, btnSaveAndCreate, btnExitWithoutSaving}));

		createCellLabelAndBox();

		createButtonLabelAndBox();
	}

	private void findDimensions() {
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();

		// find the dimensions of the screen and a dimension that is derive one
		// quarter of the size
		Dimension targetSize = new Dimension((int) thisScreen.getWidth() / 4, (int) thisScreen.getHeight() / 4);
		sCreatorFrame.setPreferredSize(targetSize);
		sCreatorFrame.setSize((int) thisScreen.getWidth() / 2, (int) thisScreen.getHeight() / 2);
		// .frmAutho(this.getClass().getName());
		this.sCreatorFrame.setLocationByPlatform(true);
		// *****************************************************************************
		// this methods asks the window manager to position the frame in the
		// centre of the screen
		this.sCreatorFrame.setLocationRelativeTo(null);
	}

	private boolean jTextField1KeyPressed(java.awt.event.KeyEvent evt) {
		  if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
		      // Enter was pressed. Your code goes here.
			  return true;
		   }
		  return false;
		} 
	private void createFrame() {
		sCreatorFrame = new JFrame();
		sCreatorFrame.getContentPane().setBackground(UIManager.getColor("CheckBox.background"));
		// sCreatorFrame.setResizable(false);
		sCreatorFrame.setBackground(new Color(255, 255, 255));
		sCreatorFrame.setTitle("Scenario Creator");
		sCreatorFrame.setBounds(100, 100, 490, 455);
		sCreatorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		sCreatorFrame.addWindowListener(new confirmClose());
	}

	private void createButtonLabelAndBox() {
		lblNumberOfCells.setLabelFor(comboCellBox);
	}

	private void createCellLabelAndBox() {
	}

	private JLabel createTitle() {
		JLabel lblScenarioTitle = new JLabel("Scenario Title");
		lblScenarioTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblScenarioTitle.setBounds(322, 200, 181, 16);
		sCreatorFrame.getContentPane().add(lblScenarioTitle);

		titleTextField = new JTextField();
		titleTextField.getAccessibleContext().setAccessibleDescription("Title of the scenario");
		titleTextField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		titleTextField.setBounds(513, 195, 130, 27);
		titleTextField.setToolTipText("Enter a Title for your Scenario");
		sCreatorFrame.getContentPane().add(titleTextField);
		titleTextField.setColumns(10);
		titleTextField.setText("New Scenario");
		return lblScenarioTitle;
	}

	private void saveButtonListener(JComboBox comboCellBox, JComboBox comboButtonBox, JButton btnSaveAndCreate) {
		//btnSaveAndCreate.setEnabled(true);
		System.out.println("Cells: " + numCells + " Buttons: " + numButtons);
		Action buttonAction = new AbstractAction("Create a Scenario") {
			int count = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				count++;
				logger.log(Level.INFO, "Create a Scenario Button was pressed.");
				logger.log(Level.INFO, "Create a Scenario Button was pressed {0} times", count);
				if(titleTextField.getText().isEmpty() || titleTextField.getText().trim().length() == 0 || !validName(titleTextField.getText())){
					//||validName(titleTextField.getText()) == false){
					Object[] buttons = {"OK"};
//					int option = JOptionPane.showConfirmDialog(null,
//							"Title Name is either empty or Invalid. Return to edit again",
//							"Confirm", JOptionPane.OK_OPTION);
					//int option == 
							JOptionPane.showOptionDialog(null,
			                   "Title Name is either empty or Invalid. Press OK to return and edit again.","Error!",
			                   JOptionPane.OK_OPTION,
			                   JOptionPane.CANCEL_OPTION, null, buttons , buttons[0]);
//					if (option == JOptionPane.OK_OPTION) {
//						removeExtra((comboCellBox.getSelectedIndex() + 1), (comboButtonBox.getSelectedIndex() + 1));
//						createAuthoringViewer(comboCellBox, comboButtonBox);
//					} else {
//						// do nothing
//					}
				}
				else if (cards.isEmpty()) {
					ArrayList<Card> cards = new ArrayList<Card>();
					Card temp = new Card(1, "Card 1", "", false);
					cards.add(temp);
					cards.get(0).getCells().add(new BrailleCell());
					cards.get(0).getButtonList().add(new DataButton(0));
					AuthoringApp ap = new AuthoringApp(comboCellBox.getSelectedIndex() + 1,
							comboButtonBox.getSelectedIndex() + 1, cards, getTitle(), "");
					ap.setCardList();
					sCreatorFrame.dispose();
				} else {
					System.out.println("Cells: " + numCells + " Buttons: " + numButtons + " " + selectedCells + " "
							+ selectedButtons + " ");
					if (selectedCells >= numCells && selectedButtons >= numButtons) {
						createAuthoringViewer(comboCellBox, comboButtonBox);
					} else {
						int option = JOptionPane.showConfirmDialog(null,
								"The number of cells or buttons is less than before. Continuing will delete the extra cells or buttons. Do you wish to continue?",
								"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (option == JOptionPane.YES_OPTION) {
							removeExtra((comboCellBox.getSelectedIndex() + 1), (comboButtonBox.getSelectedIndex() + 1));
							createAuthoringViewer(comboCellBox, comboButtonBox);
						} else {
							// do nothing
						}
					}

				}
				// >>>>>>> branch 'TestingUpdates' of
				// https://github.com/NS-01/forked_enamel
			}

		};
		btnSaveAndCreate.setAction(buttonAction);
		btnSaveAndCreate.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.SHIFT_DOWN_MASK + KeyEvent.CTRL_DOWN_MASK), "Create");
		btnSaveAndCreate.getActionMap().put("Create", buttonAction);
	}

	private void createAuthoringViewer(JComboBox comboCellBox, JComboBox comboButtonBox) {
		AuthoringApp ap = new AuthoringApp(comboCellBox.getSelectedIndex() + 1,
				comboButtonBox.getSelectedIndex() + 1, cards, getTitle(), "");
		ap.setCardList();
		ap.setPromptText(cards.get(0).getText());
		ap.setCurrCellPins(cards.get(0).getCells().get(0));
		ap.setResponseCellPins(cards.get(0).getButtonList().get(0).getCells().get(0));
		ap.setButtonText(cards.get(0).getButtonList().get(0).getText());
		ap.setCardList();
		ap.setEdited();
		sCreatorFrame.dispose();
	}

	private void removeExtra(int cells, int buttons) {
		for (Card temp : cards) {
			while (temp.getCells().size() > cells) {
				temp.getCells().remove(temp.getCells().size() - 1);
			}
			while (temp.getButtonList().size() > buttons) {
				temp.getButtonList().remove(temp.getButtonList().size() - 1);
			}
		}
	}

	private void exitButtonListener(JButton exitButton) {
		Action buttonAction = new AbstractAction("Exit Without Saving") {
			int count = 0;

			public void actionPerformed(ActionEvent e) {
				count++;
				logger.log(Level.INFO, "Exit Without Save was pressed.");
				logger.log(Level.INFO, "Exit Without Save was pressed {0} times", count);
				int option = JOptionPane.showConfirmDialog(null, "Do you want to EXIT? \nNo changes will be saved!!!",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					sCreatorFrame.dispose();
				} else {
					JOptionPane.getRootFrame().dispose();
				}
			}
		};
		exitButton.setAction(buttonAction);
		exitButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), "Exit");
		exitButton.getActionMap().put("Exit", buttonAction);
	}

	static private String selectedString(ItemSelectable is) {
		Object selected[] = is.getSelectedObjects();
		return ((selected.length == 0) ? "null" : (String) selected[0]);
	}

	public String getTitle() {
		System.out.println(this.titleTextField.getText());
		return this.titleTextField.getText();
	}

	private boolean validName(String s){
		Boolean valid = false;
		//if(s.matches("[0-9]+")||s.matches("[a-z,A-z]+")){
		if(s.matches("[a-zA-Z_0-9\\s]+")){
		valid = true;
		}
		return valid;
//		char ch;
//		for (int i = 0; i < s.length(); i++)
//		{
//		     ch = s.charAt(i);
//		       
//		     // We have detected a non letter or non whitespace, end this right now. 
//		     // Flip the flag to false, break out of the for loop, it will go back to the while loop, see valid is false
//		     // and kick out of the while loop.
//		     if (!Character.isLetter(ch) || Character.isWhitespace(ch) || !Character.isDigit(ch));
//		     {
//		        valid = false;
//		     } 
//		}  
//		return valid;
	}
	private class confirmClose extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showConfirmDialog(null, "Do you want to EXIT? \nNo changes will be saved!!!",
					"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				sCreatorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			} else {
				// JOptionPane.getRootFrame().dispose();
				// do nothing
			}
		}
	}
}