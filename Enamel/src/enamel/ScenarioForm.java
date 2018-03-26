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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.GridBagConstraints;
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

@SuppressWarnings({ "unused", "rawtypes" })
public class ScenarioForm {

	private JFrame sCreatorFrame;
	private int numCells = 1; // assuming 1 selected by default. i.e. always non
								// zero
	private int numButtons = 1; // assuming 1 selected by default. i.e. always
	private JTextField titleTextField;
	private JTextField audioFileTextField;
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
		initialize();
		ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new Formatter() {
    		private String format = "[%1$s] [%2$s] %3$s %n";
			private SimpleDateFormat dateWithMillis = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
			@Override
			public String format(LogRecord record) {
				return String.format(format, dateWithMillis.format(new Date()), record.getSourceClassName(), formatMessage(record));
			}
    	});
    	logger.addHandler(consoleHandler);
    	logger.setUseParentHandlers(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("unchecked")
	private void initialize() {
		sCreatorFrame = new JFrame();
		sCreatorFrame.getContentPane().setBackground(UIManager.getColor("CheckBox.background"));
		// sCreatorFrame.setResizable(false);
		sCreatorFrame.setBackground(new Color(255, 255, 255));
		sCreatorFrame.setTitle("Scenario Creator");
		sCreatorFrame.setBounds(100, 100, 490, 455);
		sCreatorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		sCreatorFrame.addWindowListener(new confirmClose());
		sCreatorFrame.getContentPane().setLayout(null);

		// *****************************************************************************
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

		// exit

		JLabel lblNewLabel = new JLabel("Initial Set Up");
		lblNewLabel.setBounds(456, 30, 151, 27);
		lblNewLabel.setFont(new Font("Cambria", Font.BOLD, 16));
		sCreatorFrame.getContentPane().add(lblNewLabel);

		JLabel lblNumberOfCells = new JLabel("Number of Cells");
		lblNumberOfCells.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNumberOfCells.setBounds(322, 100, 181, 16);
		sCreatorFrame.getContentPane().add(lblNumberOfCells);

		JComboBox comboCellBox = new JComboBox();
		comboCellBox.getAccessibleContext().setAccessibleDescription("Select number of cells");
		comboCellBox.setFont(new Font("Tahoma", Font.BOLD, 12));
		comboCellBox.setBounds(513, 98, 64, 21);
		comboCellBox
				.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
		sCreatorFrame.getContentPane().add(comboCellBox);

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
				numButtons = Integer.parseInt(selectedString(is).toString());
				System.out.println("Selected: " + selectedString(is));
			}
		});
		JLabel lblNumberOfButtons = new JLabel("Number of Buttons");
		lblNumberOfButtons.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNumberOfButtons.setBounds(322, 152, 181, 16);
		sCreatorFrame.getContentPane().add(lblNumberOfButtons);
		lblNumberOfCells.setLabelFor(comboCellBox);
		JComboBox comboButtonBox = new JComboBox();
		comboButtonBox.getAccessibleContext().setAccessibleDescription("Select number of buttons");
		comboButtonBox.setFont(new Font("Tahoma", Font.BOLD, 12));
		comboButtonBox.setBounds(513, 150, 64, 21);
		comboButtonBox.setBackground(new Color(238, 238, 238));

		comboButtonBox.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6" }));
		sCreatorFrame.getContentPane().add(comboButtonBox);

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
				numCells = Integer.parseInt(selectedString(is).toString());
				System.out.println("Selected: " + selectedString(is));
			}
		});

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

		/*
		 * JLabel lblAddAudioFile = new JLabel("Add Audio File (Optional)");
		 * lblAddAudioFile.setFont(new Font("Tahoma", Font.BOLD, 12));
		 * lblAddAudioFile.setBounds(321, 248, 182, 16);
		 * sCreatorFrame.getContentPane().add(lblAddAudioFile);
		 */

		/*
		 * audioFileTextField = new JTextField(); audioFileTextField.setFont(new
		 * Font("Tahoma", Font.PLAIN, 12));
		 * 
		 * audioFileTextField.setEditable(false);
		 * audioFileTextField.getAccessibleContext().
		 * setAccessibleDescription("Selected audio file");
		 * audioFileTextField.setBounds(513, 243, 130, 27);
		 * audioFileTextField.setColumns(10);
		 * sCreatorFrame.getContentPane().add(audioFileTextField);
		 */

		/*
		 * JButton btnBrowse = new JButton("Browse"); btnBrowse.setFont(new
		 * Font("Tahoma", Font.BOLD, 12)); btnBrowse.getAccessibleContext().
		 * setAccessibleDescription("Search for sound file");
		 * btnBrowse.setBounds(680, 243, 94, 27);
		 * btnBrowse.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { JFileChooser fc = new
		 * JFileChooser(); FileFilter wavFilter = new FileFilter() {
		 * 
		 * @Override public String getDescription() { return
		 * "Sound file (*.WAV)"; }
		 * 
		 * @Override public boolean accept(File file) { if (file.isDirectory())
		 * { return true; } else { return
		 * file.getName().toLowerCase().endsWith(".wav"); } } };
		 * 
		 * fc.setFileFilter(wavFilter); fc.setAcceptAllFileFilterUsed(false);
		 * fc.setCurrentDirectory(new
		 * java.io.File("./FactoryScenarios/AudioFiles"));
		 * fc.setDialogTitle("Please Choose File to Open");
		 * fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); if
		 * (fc.showOpenDialog(btnBrowse) == JFileChooser.APPROVE_OPTION) {
		 * audioFileTextField.setText(fc.getSelectedFile().getName().toString())
		 * ; } } }); sCreatorFrame.getContentPane().add(btnBrowse);
		 */

		/*
		 * JLabel lblS = new
		 * JLabel("Record and Save a New Audio \".wav\" File");
		 * lblS.setBounds(33, 212, 278, 19);
		 * sCreatorFrame.getContentPane().add(lblS); lblS.setFont(new
		 * Font("Tahoma", Font.BOLD, 12));
		 * 
		 * JButton btnRecordAudio = new JButton("Record Audio");
		 * btnRecordAudio.getAccessibleContext().
		 * setAccessibleDescription("Click to record new audio");
		 * btnRecordAudio.setBounds(321, 210, 111, 21);
		 * sCreatorFrame.getContentPane().add(btnRecordAudio);
		 * btnRecordAudio.setFont(new Font("Tahoma", Font.BOLD, 11));
		 * btnRecordAudio.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { RecorderFrame
		 * rf = new RecorderFrame(); rf.displayRecorder(); } });
		 */

		JButton btnSaveAndCreate = new JButton("Create a Scenario");
		btnSaveAndCreate.getAccessibleContext().setAccessibleDescription("Saves information and opens editor");
		btnSaveAndCreate.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnSaveAndCreate.setBounds(406, 336, 201, 29);
		saveButtonListener(comboCellBox, comboButtonBox, btnSaveAndCreate);

		/*
		 * btnSaveAndCreate.setForeground(Color.BLACK);
		 * btnSaveAndCreate.setContentAreaFilled(false);
		 * btnSaveAndCreate.setOpaque(true);
		 */
		btnSaveAndCreate.setBackground(UIManager.getColor("CheckBox.background"));
		sCreatorFrame.getContentPane().add(btnSaveAndCreate);

		JButton btnExitWithoutSaving = new JButton("Exit Without Saving");
		btnExitWithoutSaving.getAccessibleContext().setAccessibleDescription("Doesn't save and closes current window");
		btnExitWithoutSaving.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnExitWithoutSaving.setBounds(406, 376, 201, 29);
		/*
		 * btnExitWithoutSaving.setForeground(Color.BLACK);
		 * btnExitWithoutSaving.setContentAreaFilled(false);
		 * btnExitWithoutSaving.setOpaque(true);
		 * btnExitWithoutSaving.setBackground(UIManager.getColor(
		 * "CheckBox.background"));
		 */
		btnExitWithoutSaving.addActionListener(new ActionListener() {
			int count = 0;
			public void actionPerformed(ActionEvent e) {
				count ++;
				logger.log(Level.INFO, "Exit Without Save was pressed.");
				logger.log(Level.INFO, "Exit Without Save was pressed {0} times", count);
				// sCreatorFrame.setVisible(false);
				int option = JOptionPane.showConfirmDialog(null, "Do you want to EXIT? \nNo changes will be saved!!!",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					sCreatorFrame.dispose();
				} else {
					// do nothing
					JOptionPane.getRootFrame().dispose();
				}
			}
		});
		sCreatorFrame.getContentPane().add(btnExitWithoutSaving);
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
		sCreatorFrame.getContentPane()
				.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { lblNewLabel, lblNumberOfCells,
						comboCellBox, lblNumberOfButtons, comboButtonBox, lblScenarioTitle, titleTextField,
						btnSaveAndCreate, btnExitWithoutSaving }));
	}

	private void saveButtonListener(JComboBox comboCellBox, JComboBox comboButtonBox, JButton btnSaveAndCreate) {
		btnSaveAndCreate.addActionListener(new ActionListener() {
			int count = 0;
			public void actionPerformed(ActionEvent e) {
				count ++;
				logger.log(Level.INFO, "Create a Scenario Button was pressed.");
				logger.log(Level.INFO, "Create a Scenario Button was pressed {0} times", count);
				ArrayList<Card> cards = new ArrayList<Card>();
				Card temp = new Card(1, "Card 1", "");
				cards.add(temp);
				cards.get(0).getCells().add(new BrailleCell());
				AuthoringViewer av = new AuthoringViewer(comboCellBox.getSelectedIndex() + 1,
						comboButtonBox.getSelectedIndex() + 1, cards, getTitle(), "");
				av.setCardList();
				sCreatorFrame.dispose();
			}
		});
	}

	static private String selectedString(ItemSelectable is) {
		Object selected[] = is.getSelectedObjects();
		return ((selected.length == 0) ? "null" : (String) selected[0]);
	}

	public String getTitle() {
		System.out.println(this.titleTextField.getText());
		return this.titleTextField.getText();
	}

	private class confirmClose extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showConfirmDialog(null, "Do you want to EXIT? \nNo changes will be saved!!!",
					"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				sCreatorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} else {
				JOptionPane.getRootFrame().dispose();
				// do nothing
			}
		}
	}
}
