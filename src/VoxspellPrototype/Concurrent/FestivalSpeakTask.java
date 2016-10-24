package VoxspellPrototype.Concurrent;

import javafx.concurrent.Task;

/**
 * 
 * @author nathan kear & charles carey
 *
 */
public class FestivalSpeakTask extends Task<Void> {

	private String _word;
	private static double _speed = 1.0;
	private static String _voice = "kal_diphone";
	
	/**
	 * Change the speaking rate.
	 * @param speed 1.0 = normal speech rate, < 1.0 = slower, > 1.0 = faster.
	 */
	public static void SetSpeed(double speed) {
		if (speed > 0.0) {
			_speed = speed;
		}
	}
	
	/**
	 * Get the current speaking rate.
	 * @return speed 1.0 = normal speech rate, < 1.0 = slower, > 1.0 = faster.
	 */
	public static double getSpeed() {
		return _speed;
	}
	
	/**
	 * Create new speak task for festival to say the given word/phrase.
	 * @param word Phrase to speak.
	 */
	public FestivalSpeakTask(String word) {
		this._word = word;
	}

	/**
	 * Change the voice.
	 * @param voice.
	 */
	public static void changeVoice(String voice) {
		_voice = voice;
	}

	/**
	 * Get the currently set voice.
	 * @param voice currently set.
	 */
	public static String getVoice() {
		return _voice;
	}

	@Override
	protected Void call() throws Exception {

		// Set the voice in festival
		String setVoice = "(voice_" + _voice + ")";
		// Set the rate of speech
		String setSpeed = "(Parameter.set 'Duration_Stretch " + _speed + ")";
		// Say the specified phrase
		String sayPhrase = "(SayText \"" + this._word + "\")";
		
		// Run festival command and then exit festival
		new ProcessBuilder("/usr/bin/festival", setVoice, setSpeed, sayPhrase, "(exit)").start();
		
		return null;
	}
}
