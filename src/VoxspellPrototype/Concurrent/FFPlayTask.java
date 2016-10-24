package VoxspellPrototype.Concurrent;

import javafx.concurrent.Task;

/**
 * Play audio file through ffplay.
 * @author nathan kear
 *
 */
public class FFPlayTask extends Task<Void> {

	private String _fileName;

	/**
	 * 
	 * @param file identification of file to play.
	 */
	public FFPlayTask(String fileName) {
		this._fileName = fileName;
	}

	@Override
	protected Void call() throws Exception {

		// Run command through ffplay, suppress any windows being created.
		new ProcessBuilder("/usr/bin/ffplay", "-autoexit", "-nodisp", "-loglevel", "panic" ,this._fileName).start();
		
		return null;
	}
}
