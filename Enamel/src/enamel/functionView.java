package enamel;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;
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
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import java.awt.GridBagConstraints;
import javax.swing.DefaultComboBoxModel;
import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.Component;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JRadioButton;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
/**
 * 
 * @author Jeremy, Nisha, Tyler
 * 
 *         Class to set function view. Coming soon...
 *
 */
@SuppressWarnings({ "unused", "rawtypes" })
public class functionView {
	private JFrame funcFrame;
	private int numCells = 1;
	private int numButtons = 1;
	private BrailleCell cell;
	private boolean saveState;
	public static final String pins = "";
	// non zero

	/**
	 * This Window is still under construction
	 */

	/**
	 * Create the application.
	 */
	public functionView() {// BrailleCell cell2) {
		initialize();// cell2);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {// BrailleCell cell2) {
		// saveState = false;
		// cell = //cell2;
		funcFrame = new JFrame();
		funcFrame.setResizable(false);
		funcFrame.setBackground(new Color(255, 255, 255));
		funcFrame.setTitle("AuthoringApp view");
		funcFrame.setBounds(100, 100, 300, 400);
		funcFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		funcFrame.addWindowListener(new confirmClose());
		funcFrame.setVisible(true);
	}

	private class confirmClose extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			int option = JOptionPane.showConfirmDialog(null, "Do want to EXIT? \nNo changes will be saved!!!",
					"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				// System.exit( 0 );
				funcFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} else {
				//  do nothing
			}
		}
	}

	public BrailleCell getCell() {
		return this.cell;
	}

	public boolean getSaveState() {
		return this.saveState;
	}

	public static String returnPins() {
		return pins;
	}
}
