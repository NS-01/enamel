package audioRecorder;

//import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextArea;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextField;
//import javax.swing.SwingUtilities;
import javax.swing.KeyStroke;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

///////////////////////////////////////////////////////////////////////////////////////Save as

/**
 * 
 * @author Jeremy, Nisha, Tyler
 * 
 *         GUI class to implement Recording feature for Authoring app This class
 *         allows user record and save audio. User may discard the audio and
 *         play recently saved audio. Accessibility features are implemented.
 *
 */
public class RecorderFrame {
	// Fields
	private JFrame recorderFrame;

	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblTimer;
	private JLabel lblHelpMenu;
	JEditorPane tViewHelp;
	JButton recordNewButton;
	JButton stopRecordingButton;

	private Boolean isRecording;
	private audioTimer audioTimer;

	private static final int BUFFER_SIZE = 4096;
	private ByteArrayOutputStream recordBytes;
	private TargetDataLine audioLine;
	private AudioFormat format;

	private boolean isRunning;
	private boolean donePlaying;
	private boolean stopPlayBack;

	private String path;
	private JButton resetButton;
	private JButton btnPlay = new JButton("PLAY");
	WavPlayer wavePlayer;
	
	//Menu
	private JMenuItem mntmRecordNew;
	private JMenuItem mntmInstructions;
	private JMenu mnTools;
	private JMenuItem mntmPlay;
	private JMenuBar menuBar;
	private JMenu mntmHelp;
	private JMenuItem mntmSave;
	
	/**
	 * Launch the application.
	 */
	public static void displayRecorder() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecorderFrame frame = new RecorderFrame();
					frame.recorderFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RecorderFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// settings for the frame
		recorderFrame = new JFrame();
		//recorderFrame.setResizable(false);
		recorderFrame.setTitle("Audio Recorder");
		recorderFrame.getAccessibleContext().setAccessibleDescription("Use this tool to record and save an audio file");
		recorderFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		recorderFrame.setBounds(100, 100, 913, 245);
		
		//Menu Bar
		menuBar = new JMenuBar();
		recorderFrame.setJMenuBar(menuBar);
		
		//Tools menu
		mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		// MenuItem Record
		mntmRecordNew = new JMenuItem("Record New");
		mnTools.add(mntmRecordNew);
		mntmRecordNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recordAudio();
			}
		});
		
		// Menu Item Save
		mntmSave = new JMenuItem("Save");
		mnTools.add(mntmSave);
		mntmSave.setEnabled(false);
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				stopRecording();
			}
		});
		
		mntmPlay = new JMenuItem("Play");
		mnTools.add(mntmPlay);
		mntmSave.setEnabled(false);
		
		mntmHelp = new JMenu("Help");
		menuBar.add(mntmHelp);
		
		mntmInstructions = new JMenuItem("Instructions");
		mntmHelp.add(mntmInstructions);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		recorderFrame.setContentPane(contentPane);
		contentPane.setLayout(null);

		
		//*****************************************************************************
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();

		// find the dimensions of the screen and a dimension that is derive one
		// quarter of the size
		Dimension targetSize = new Dimension((int) thisScreen.getWidth() / 4, (int) thisScreen.getHeight() / 4);
		recorderFrame.setPreferredSize(targetSize);
		//recorderFrame.setSize((int) thisScreen.getWidth() / 2, (int) thisScreen.getHeight() / 2);
		//.frmAutho(this.getClass().getName());
		this.recorderFrame.setLocationByPlatform(true);
		//*****************************************************************************
		// this methods asks the window manager to position the frame in the
		// centre of the screen
		this.recorderFrame.setLocationRelativeTo(null);
		
		// Button for starting a new recording
		recordNewButton = new JButton("RECORD");
		recordNewButton.getAccessibleContext().setAccessibleDescription("Click to record a new audio");
		recordNewButton.setForeground(new Color(0, 0, 128));
		recordNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		recordNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recordAudio();
			}
		});
		recordNewButton.setBounds(20, 148, 125, 36);
		contentPane.add(recordNewButton);

		// Button for stopping and then saving the current recoding
		stopRecordingButton = new JButton("STOP & SAVE");
		stopRecordingButton.getAccessibleContext()
				.setAccessibleDescription("Click to stop recording and save audio file");
		stopRecordingButton.setEnabled(false);
		stopRecordingButton.setForeground(new Color(139, 0, 0));
		stopRecordingButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		stopRecordingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				stopRecording();
			}
		});
		stopRecordingButton.setBounds(197, 148, 125, 36);
		contentPane.add(stopRecordingButton);

		// Label for record timer
		//JLabel 
		lblTimer = new JLabel("STATUS:");
		lblTimer.getAccessibleContext().setAccessibleDescription("Indicates whether the recorder is recording or not");
		lblTimer.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTimer.setBounds(350, 152, 54, 29);
		contentPane.add(lblTimer);

		// textField to display the record time
		textField = new JTextField();
		textField.getAccessibleContext().setAccessibleDescription("Recording or not");
		textField.setForeground(Color.BLUE);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField.setText("Ready");
		textField.setEditable(false);
		textField.setBounds(412, 148, 137, 36);
		contentPane.add(textField);
		textField.setColumns(10);

		// button for discarding current recording and resetting the recorder
		resetButton = new JButton("RESET");
		resetButton.getAccessibleContext().setAccessibleDescription("Click to Discard a Recording");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetRecorder();
				// playSound(path);
			}
		});
		resetButton.setForeground(new Color(0, 128, 128));
		resetButton.setEnabled(false);
		resetButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		resetButton.setBounds(592, 148, 125, 36);
		contentPane.add(resetButton);

		// button to play the recorded audio
		btnPlay.getAccessibleContext().setAccessibleDescription("Click to play recently saved audio");
		btnPlay.setBounds(772, 148, 125, 36);
		contentPane.add(btnPlay);
		btnPlay.setForeground(new Color(85, 107, 47));
		btnPlay.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnPlay.setEnabled(false);
				
				
		
				// JTextArea for instructions
				tViewHelp = new JEditorPane();
				tViewHelp.setEditable(false);
				tViewHelp.setFont(new Font("Times New Roman", Font.PLAIN, 14));
				tViewHelp.setBounds(109, 20, 592, 149);
				tViewHelp.setText("Welcome to \"Audio Recorder\". Below are the instructions to use it: \r\n -     Press \"RECORD\" or \"CTRL+SHIFT+R\" to start a new recording\r\n -     Press \"STOP & SAVE\" or  \"CTRL+SHIFT+S\" to save recorded audio as \".wav\" file\r\n -     \"STATUS\" indicates that audio is being recorded\r\n\t- (record timer will be added in next build)\r\n -     You can \"RESET\" the recorder to start a new recording\r\n\t- NOTE: Unsaved Recording will be deiscarded, while the saved files will not be deleted\r\n -     Press \"PLAY\" or \"CTRL+SHIFT+P\" to play the recently saved audio file \r\n\t- (you can not play a previously saved audio file yet)");
				contentPane.add(tViewHelp);
		
			btnPlay.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					playSound(path);
					/*wavePlayer = new WavPlayer("file:"+path);
					if(btnPlay.getText() == "Play"){					
						wavePlayer.play();
						btnPlay.setText("Stop");
					}
					if(btnPlay.getText() == "Stop"){
						wavePlayer.stop();
						btnPlay.setText("Play");
					}*/
				}
			});
		
		JScrollPane scrollPane = new JScrollPane(tViewHelp);
		scrollPane.setBounds(20, 5, 877, 132);
		contentPane.add(scrollPane);
		
		lblHelpMenu = new JLabel("Instructions To Use");
		scrollPane.setColumnHeaderView(lblHelpMenu);

		if (textField.getText() == "Ready") {
			recorderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		recorderFrame.addWindowListener(new confirmClose());
		// pop up before exit
		
		mntmRecordNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
		mntmPlay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
		mntmInstructions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
	}

	/**
	 * Defines a default audio format used to record
	 * 
	 * @return AudioFormat
	 */
	public AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	/*
	 * Record audio as a separate thread
	 */
	public void recordAudio() {
		Thread record = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// set isRecording boolean to true
				isRecording = true;
				recordNewButton.setEnabled(false);//new button
				mntmRecordNew.setEnabled(false);//new menu
				recordNewButton.setEnabled(false);
				stopRecordingButton.setEnabled(true);//save button
				mntmSave.setEnabled(true);//save menu
				resetButton.setEnabled(true);//reset button
				//reset menu <_-------------------------------------------------------------------------
				mntmPlay.setEnabled(false);//************
				// while recording
				while (isRecording) {
					try {
						start();
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});
		record.start();
	}

	/**
	 * Start recording sound.
	 * 
	 * @throws LineUnavailableException
	 *             if the system does not support the specified audio format nor
	 *             open the audio data line.
	 */
	@SuppressWarnings("deprecation")
	public void start() throws LineUnavailableException {
		format = getAudioFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		// checks if system supports the data line
		if (!AudioSystem.isLineSupported(info)) {
			throw new LineUnavailableException("The system does not support the specified format.");
		}

		audioLine = AudioSystem.getTargetDataLine(format);

		audioLine.open(format);
		audioLine.start();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = 0;

		recordBytes = new ByteArrayOutputStream();
		isRunning = true;
		long start = System.currentTimeMillis();
		
		while (isRunning) {
			bytesRead = audioLine.read(buffer, 0, buffer.length);
			recordBytes.write(buffer, 0, bytesRead);
			textField.setText(new Time(System.currentTimeMillis() - start).toString().substring(3));
		}
	}

	/**
	 * Stop recording and save the sound into a WAV file
	 */
	private void stopRecording() {
		isRecording = false;
		//recordNewButton.setEnabled(true);//-------------------------------------------------------------------------------
		// stopRecordingButton.setEnabled(false);
		try {
			stop();
			saveAudioFile();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Error", "Error stopping sound recording!", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	/**
	 * Stop recording and reset recorder
	 */
	private void resetRecorder() {
		isRecording = false;
		textField.setText("Ready");
		
		recordNewButton.setEnabled(true);
		mntmRecordNew.setEnabled(true);
		
		stopRecordingButton.setEnabled(false);
		mntmSave.setEnabled(false);
		
		btnPlay.setEnabled(false);
		mntmPlay.setEnabled(false);////////*******************
		try {
			stop();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Error", "Error discarding sound recording!",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	/**
	 * Stop recording sound.
	 * 
	 * @throws IOException
	 *             if any I/O error occurs.
	 */
	public void stop() throws IOException {
		isRunning = false;

		if (audioLine != null) {
			// audioLine.drain();
			audioLine.close();
			audioLine.drain();
		}
	}

	private void saveAudioFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("./FactoryScenarios/AudioFiles"));
		fileChooser.setDialogTitle("Save as");
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

		fileChooser.setFileFilter(wavFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);

		int userChoice = fileChooser.showSaveDialog(null);
		if (userChoice == JFileChooser.APPROVE_OPTION) {
			path = fileChooser.getSelectedFile().getAbsolutePath();
			if (!path.toLowerCase().endsWith(".wav")) {
				path += ".wav";
			}

			File wavFile = new File(path);

			try {
				save(wavFile);
				recordNewButton.setEnabled(true);
				mntmRecordNew.setEnabled(true);
				stopRecordingButton.setEnabled(false);
				mntmSave.setEnabled(false);
				JOptionPane.showMessageDialog(null, "Saved recorded sound to:\n" + path);
				btnPlay.setEnabled(true);
				mntmPlay.setEnabled(true);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error", "Error saving to sound file!", JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Save recorded sound data into a .wav file format.
	 * 
	 * @param wavFile
	 *            The file to be saved.
	 * @throws IOException
	 *             if any I/O error occurs.
	 * @throws UnsupportedAudioFileException
	 */
	public void save(File wavFile) throws IOException {
		byte[] audioData = recordBytes.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
		AudioInputStream audioInputStream = new AudioInputStream(bais, format,
				audioData.length / format.getFrameSize());
		AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);

		audioInputStream.close();
		recordBytes.close();
	}

	// Class with method to display confirmation dialog box when user tries to
	// close the JFrame
	private class confirmClose extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			if (!(textField.getText().contains("Ready"))) {
				int option = JOptionPane.showConfirmDialog(null, "Do you want to EXIT? \nNo changes will be saved!!!",
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					// System.exit( 0 );
					recorderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				} else {
					// do nothing
				}
			} else {
				// normal exit if no recording started
				recorderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		}
	}

	/*
	 * This method corresponds to the /~sound: key phrase in the scenario file,
	 * and it plays the sound files where the argument is the name of the sound
	 * file.
	 */
	private void playSound(String sound) {
		//btnPlay.setEnabled(false);
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(sound)));
			clip.start();
			
			donePlaying = false;
			// This while loop is to check if the audio file has played or not,
			// and if it has not then it will
			// continue to wait until it does.
			while (!clip.isRunning()) {
				Thread.sleep(10);
			}
			
			/*while (!donePlaying) {
				// wait for the audio playback to be done
				btnPlay.setText("Stop");
				btnPlay.setEnabled(true);
				stopPlayBack = false;
				btnPlay.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						btnPlay.setText("Play");
						stopPlayBack = true;
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
					if (stopPlayBack) {
						clip.stop();
						break;
					}
				}
			}*/
			while (clip.isRunning()){
				//Thread.sleep(10);
				btnPlay.repaint();
			/*	btnPlay.setText("Stop");
				btnPlay.setEnabled(true);
				btnPlay.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						btnPlay.setText("Play");
						try {
							clip.close();
							stop();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});*/
			}
			clip.close();
			

		} catch (Exception e) {
			System.out.println("Exception error: " + e.toString()
					+ "Expected the name of the file (including extension) but instead got: " + sound
					+ "\n Perhaps you forgot to include the extension of the sound file with the name? Other "
					+ "possibilities include: \n Incorrect name of the file, the file not being in the same location "
					+ "as the project folder, or an attempt to play an unsupported sound file. (only .wav files"
					+ "are supported at this time)");
		}
	}
	
	public class RecordTimer extends Thread {
		private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");	
		private boolean isRunning = false;
		private boolean isReset = false;
		private long startTime;
		private JLabel labelRecordTime;
		
		RecordTimer(JLabel labelRecordTime) {
			this.labelRecordTime = labelRecordTime;
		}
		
		public void run() {
			isRunning = true;
			
			startTime = System.currentTimeMillis();
			
			while (isRunning) {
				try {
					Thread.sleep(1000);
					labelRecordTime.setText("Record Time: " + toTimeString());
				} catch (InterruptedException ex) {
					ex.printStackTrace();
					if (isReset) {
						labelRecordTime.setText("Record Time: 00:00:00");
						isRunning = false;		
						break;
					}
				}
			}
		}
		
		/**
		 * Cancel counting record/play time.
		 */
		void cancel() {
			isRunning = false;		
		}
		
		/**
		 * Reset counting to "00:00:00"
		 */
		void reset() {
			isReset = true;
			isRunning = false;
		}
		
		/**
		 * Generate a String for time counter in the format of "HH:mm:ss"
		 * @return the time counter
		 */
		private String toTimeString() {
			long now = System.currentTimeMillis();
			Date current = new Date(now - startTime);
			dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
			String timeCounter = dateFormater.format(current);
			return timeCounter;
		}
	}
}
