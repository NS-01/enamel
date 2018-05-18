package audioRecorder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JLabel;

public class audioTimer extends Thread {
	//private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");
	private boolean isRunning = false;
	private boolean isReset = false;
	private long startingTime;
	private JLabel labelAudioTimer;

	/**
	 * Creates a label with timer value.
	 * @param labelAudioTimer
	 */
	public audioTimer(JLabel labelAudioTimer) {
		this.labelAudioTimer = labelAudioTimer;
	}

	/**
	 * Run method for the audioTimer thread.
	 * Resets the timer if value of isReset true.
	 */
	public void run() {
		isRunning = true;

		startingTime = System.currentTimeMillis();

		while (isRunning) {
			try {
				Thread.sleep(1000);
				labelAudioTimer.setText("Record Time: " + timerString());
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				if (isReset) {
					labelAudioTimer.setText("Record Time: 00:00:00");
					isRunning = false;
					break;
				}
			}
		}
	}

	/**
	 * Stops the timer.
	 */
	void stopTimer() {
		isRunning = false;
	}

	/**
	 * Resets the timer to "00:00:00"
	 */
	void reset() {
		isReset = true;
		isRunning = false;
	}

	/**
	 * Generate a String for time counter in the format of "HH:mm:ss"
	 * 
	 * @return the time counter
	 */
	private String timerString() {
		//long startingTime = System.currentTimeMillis();
		long elapsedTime = System.currentTimeMillis() - startingTime;
		long elapsedSeconds = elapsedTime / 1000;
		long secondsDisplay = elapsedSeconds % 60;
		long elapsedMinutes = elapsedSeconds / 60;
		long elapsedHours = elapsedMinutes / 60;
		String timeCounter = elapsedHours + " : " + elapsedMinutes + " : " + secondsDisplay;
		//long now = System.currentTimeMillis();
		//Date current = new Date(now - startTime);
		//dateFormater.setTimeZone(TimeZone.getTimeZone("GMT"));
		//String timeCounter = dateFormater.format(current);
		return timeCounter;
	}
}
