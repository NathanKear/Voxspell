package VoxspellPrototype;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VoxspellPrototype extends Application {

	// Global constants (naughty public static!!)
	public static final String BACK_COLOR = "#96ceb4";//"#9ad3de";
	public static final String BUTTON_COLOR = "#ff6f69";//"#89bdd3";
	public static final String LIGHT_COLOR = "#e3e3e3";
	public static final String DARK_COLOR = "#c9c9c9";
	public static final String MEDIA_SOURCE = "/home/nathan/workspace/ccar-nkea_se206-a03/media/bunny.mp4";
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

