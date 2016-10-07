package VoxspellPrototype;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VoxspellPrototype extends Application {

	// Global constants (naughty public static!!)
	public static final String BACK_COLOR = "#3299BB";//"#96ceb4";
	public static final String BUTTON_COLOR = "#FF9900";//"#ff6f69";
	public static final String LIGHT_COLOR = "#E9E9E9";
	public static final String MIDDLE_COLOR = "#BCBCBC";
	public static final String DARK_COLOR = "#424242";
	public static final String MEDIA_SOURCE = "/home/nathan/workspace/ccar-nkea_se206-a03/media/bunny.mp4";
	public static final String CORRECT_SOURCE = "media/audio/correct.wav";
	public static final String INCORRECT_SOURCE = "media/audio/incorrect.wav";
	public static final String CHEER_SOURCE = "media/audio/cheer.wav";
	public static final String TICK_SOURCE = "media/audio/tick.wav";
	public static final int BTN_FONT_SIZE = 22;
	public static final int TXT_FONT_SIZE = 46;
	public static final int TXT_FONT_SIZE_FINE = 18;
	public static final int QUIZ_LENGTH = 10;
	public static final String TXT_FILE = ".spellinglist.txt";
	
	private Window _window;
	
	private final String WINDOW_TITLE = "VoxSpell";
	private final int WINDOW_WIDTH = 800;
	private final int WINDOW_HEIGHT = 500;

	public VoxspellPrototype() {
	}

	@Override
	public void start(Stage stage) throws Exception {
		Platform.setImplicitExit(false);
		
		// Fix window size
		stage.setResizable(false);
		
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent arg0) {
				Platform.exit();
			}
			
		});
		
		// Create and format window and set to intial screen
		_window = new Window(stage, WINDOW_WIDTH, WINDOW_HEIGHT);
		_window.SetWindowScene(new Scene(new MainScreen(_window), _window.GetWidth(), _window.GetHeight()));
		_window.SetWindowTitle(WINDOW_TITLE);	
		_window.CenterOnScreen();
		_window.Show(true);
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	/**
	 * This is overriding the method that is called when the platform exits and is used to save the stats to file
	 */
	public void stop(){
	    //Save words to disk
		WordList wordList = WordList.GetWordList();
		wordList.saveWordListToDisk();
	}
	
}

