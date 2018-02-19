package audioRecorder;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextArea;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Color;

public class RecorderFrame {
	private JFrame recorderFrame;

	private JPanel contentPane;
	private JTextField textField;

	private Boolean isRecording;
	
	private static final int BUFFER_SIZE = 4096;
	private ByteArrayOutputStream recordBytes;
	private TargetDataLine audioLine;
	private AudioFormat format;

	private boolean isRunning;

	private String path; 

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
		recorderFrame.setResizable(false);
		recorderFrame.setTitle("Audio Recorder");
		recorderFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		recorderFrame.setBounds(100, 100, 773, 224);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		recorderFrame.setContentPane(contentPane);
		contentPane.setLayout(null);

		recorderFrame.addWindowListener(new confirmClose()); // pop up before
		// exit

		// JTextArea for instructions
		JTextArea txtrPressrecordTo = new JTextArea();
		txtrPressrecordTo.setEditable(false);
		txtrPressrecordTo.setFont(new Font("Times New Roman", Font.BOLD, 15));
		txtrPressrecordTo.setBounds(10, 5, 747, 120);
		txtrPressrecordTo.setText(" -     Press \"RECORD NEW\" to start recording\r\n"
				+ " -     Press \"STOP & SAVE\" to save audio as \".wav\" file\r\n"
				+ " -     \"TIMER\" indicates the record time\r\n" + " -     You may choose to \"DISCARD\" recording ");
		contentPane.add(txtrPressrecordTo);

		// Button for starting a new recording
		JButton recordNewButton = new JButton("RECORD NEW");
		recordNewButton.setForeground(Color.BLUE);
		recordNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		recordNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 recordAudio();
			}
		});
		recordNewButton.setBounds(27, 138, 150, 46);
		contentPane.add(recordNewButton);

		// Button for stopping and then saving the current recoding
		JButton stopRecordingButton = new JButton("STOP & SAVE");
		stopRecordingButton.setForeground(Color.RED);
		stopRecordingButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		stopRecordingButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				stopRecording();
			}
		});
		stopRecordingButton.setBounds(190, 138, 142, 46);
		contentPane.add(stopRecordingButton);

		// Label for record timer
		JLabel lblTimer = new JLabel("TIMER:");
		lblTimer.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTimer.setBounds(342, 140, 70, 44);
		contentPane.add(lblTimer);

		// textField to display the record time
		textField = new JTextField();
		textField.setForeground(Color.BLUE);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField.setText("00.00.00");
		textField.setEditable(false);
		textField.setBounds(416, 138, 150, 46);
		contentPane.add(textField);
		textField.setColumns(10);
	}
	
	/**
	 * Defines a default audio format used to record
	 * @return AudioFormat
	 */
	public AudioFormat getAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}
	
	/*
	 * Record audio as a separate thread
	 */
	public void recordAudio() {
		Thread record = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// set isRecording boolean to true
				isRecording = true;
				//while recording 
				while(isRecording){
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
	 * @throws LineUnavailableException if the system does not support the specified 
	 * audio format nor open the audio data line.
	 */
	public void start() throws LineUnavailableException {
		format = getAudioFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

		// checks if system supports the data line
		if (!AudioSystem.isLineSupported(info)) {
			throw new LineUnavailableException(
					"The system does not support the specified format.");
		}

		audioLine = AudioSystem.getTargetDataLine(format);

		audioLine.open(format);
		audioLine.start();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = 0;

		recordBytes = new ByteArrayOutputStream();
		isRunning = true;

		while (isRunning) {
			bytesRead = audioLine.read(buffer, 0, buffer.length);
			recordBytes.write(buffer, 0, bytesRead);
		}
	}

	/**
	 * Stop recording and save the sound into a WAV file
	 */
	private void stopRecording() {
		isRecording = false;
		try {
			stop();			
			saveAudioFile();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null,"Error",
					"Error stopping sound recording!",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}


	/**
	 * Stop recording sound.
	 * @throws IOException if any I/O error occurs.
	 */
	public void stop() throws IOException {
		isRunning = false;
		
		if (audioLine != null) {
			//audioLine.drain();
			audioLine.close();
			audioLine.drain();
		}
	}
	
	private void saveAudioFile() {
		// TODO Auto-generated method stub
		JFileChooser fileChooser = new JFileChooser();
		FileFilter wavFilter = new FileFilter(){
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

				JOptionPane.showMessageDialog(null,
						"Saved recorded sound to:\n" + path);

			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Error",
						"Error saving to sound file!",
						JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Save recorded sound data into a .wav file format.
	 * @param wavFile The file to be saved.
	 * @throws IOException if any I/O error occurs.
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
			int option = JOptionPane.showConfirmDialog(null, "Do want to EXIT? \nNo changes will be saved!!!",
					"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (option == JOptionPane.YES_OPTION) {
				// System.exit( 0 );
				recorderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} else {
				// do nothing
			}
		}
	}
	
	private class Timer {
		private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");	
		private Boolean running;
		private long startTime;
		String timer;
		/*public Timer(String s){
			this.timer = s;
		}*/
		
		public void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {

	            @Override
	            public void run() {
	            	running = true;
	            	startTime = System.currentTimeMillis();
	            	System.out.println("Start Time"+ startTime);
	                new Timer();
	            }
	        });
	    }
	}
}