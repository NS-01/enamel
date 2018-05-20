package enamel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class AudioPlayer extends Player {
	private Voice voice;
	private VoiceManager vm;
	private JFrame keyListenerFrame;
	private JPanel buttonPanel = new JPanel();
	LinkedList<JButton> buttonList = new LinkedList<JButton>();
	
	public AudioPlayer(int cellNum, int buttonNum) {
		super(cellNum, buttonNum);
		System.out.println("Hello");
		vm = VoiceManager.getInstance();
		voice = vm.getVoice("kevin16");
		voice.allocate();
		keyListenerFrame = new JFrame();
		keyListenerFrame.setTitle("Simulator");
		keyListenerFrame.setBounds(100, 100, 627, 459);
		keyListenerFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//(JFrame.DISPOSE_ON_CLOSE);
		keyListenerFrame.addWindowListener(new confirmClose());
		keyListenerFrame.getContentPane().setLayout(new BorderLayout());
		
		keyListenerFrame = new JFrame();
		for (int i = 0; i < buttonNumber; i++) {
			JButton button = new JButton("" + (i + 1));
			buttonList.add(button);
			buttonPanel.add(button);
		}
		keyListenerFrame.add(buttonPanel, BorderLayout.SOUTH);
		keyListenerFrame.pack();
		keyListenerFrame.setLocationRelativeTo(null);
		keyListenerFrame.setVisible(true);
//		refresh();
	}
	
	private JButton getButton(int index) {
		if (index >= this.buttonNumber || index < 0) {
			throw new IllegalArgumentException("Invalid button index.");
		}
		return this.buttonList.get(index);
	}

	@Override
	public void refresh() {
		String say = "";
		for(int i = 0; i < brailleList.size(); i++) {
			BrailleCell temp = brailleList.get(i);
			say += "Cell " + (i+1) + " has pins ";
			for (int j = 0; j < 8; j++) {
				if (temp.getPinState(j)) {
					say += (j+1) + " ";
				}
			}
			say += " raised";
		}
		//Refresh gets called before pins get raised, 23 is length if nothing was raised
		if (say.length() > 23)
			voice.speak(say);
	}

	@Override
	public void addSkipButtonListener(int index, String param, ScenarioParser sp) {
		
		
		Action buttonAction = new AbstractAction("Button " + (index+1)) {
			int count = 0;

			@Override
			// >>>>>>> branch 'TestingUpdates' of https://github.com/NS-01/forked_enamel
			public void actionPerformed(ActionEvent e) {
				if (sp.userInput) {
					sp.skip(param);
					sp.userInput = false;
				}
			}
		};
		getButton(index).setAction(buttonAction);
		if (index == 0 ) {
			getButton(index).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke('1'), "Button 1");
			getButton(index).getActionMap().put("Button 1", buttonAction);
		} else if (index == 1) {
			getButton(index).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke('2'), "Button 2");
			getButton(index).getActionMap().put("Button 2", buttonAction);
		} else if (index == 2) {
			getButton(index).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke('3'), "Button 3");
			getButton(index).getActionMap().put("Button 3", buttonAction);
		} else if (index == 3) {
			getButton(index).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke('4'), "Button 4");
			getButton(index).getActionMap().put("Button 4", buttonAction);
		}
		
		
		
	}

	@Override
	public void removeButtonListener(int index) {
		if (index >= this.buttonNumber || index < 0) {
            throw new IllegalArgumentException("Invalid index.");
        }
		ActionListener[] aList = getButton(index).getActionListeners();
		if (aList.length > 0) {
			for (int x = 0; x < aList.length; x++) {
				getButton(index).removeActionListener(getButton(index).getActionListeners()[x]);
			}
		}
		
	}

	@Override
	public void addRepeatButtonListener(int index, ScenarioParser sp) {
		Action buttonAction = new AbstractAction("Button " + (index+1)) {
			int count = 0;

			@Override
			// >>>>>>> branch 'TestingUpdates' of https://github.com/NS-01/forked_enamel
			public void actionPerformed(ActionEvent e) {
				if (sp.userInput) {
					repeat++;
					logger.log(Level.INFO, "Repeat Button was pressed.");
					logger.log(Level.INFO, "Repeat Button was pressed {0} times", repeat);
					sp.repeatText();
				}
			}
		};
		getButton(index).setAction(buttonAction);
		if (index == 0 ) {
			getButton(index).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke('1'), "Button 1");
			getButton(index).getActionMap().put("Button 1", buttonAction);
		} else if (index == 1) {
			getButton(index).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke('2'), "Button 2");
			getButton(index).getActionMap().put("Button 2", buttonAction);
		} else if (index == 2) {
			getButton(index).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke('3'), "Button 3");
			getButton(index).getActionMap().put("Button 3", buttonAction);
		} else if (index == 3) {
			getButton(index).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
			.put(KeyStroke.getKeyStroke('4'), "Button 4");
			getButton(index).getActionMap().put("Button 4", buttonAction);
		}
	
		
	}
	
	private class confirmClose extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			ScenarioParser.exit = true;
			keyListenerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	}

}
