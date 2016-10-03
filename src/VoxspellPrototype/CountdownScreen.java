package VoxspellPrototype;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class CountdownScreen extends Parent {

	private Window _window;
	
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final String TXT_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final Text _txtCountDown;
	private final Timeline _timeline;
	private int _count = 5;
	private final String _wordlist;

	public CountdownScreen(Window window, String wordlist) {
		super();

		_window = window;
		_wordlist = wordlist;
		
		VBox root = new VBox();

		_txtCountDown = new Text("\n\n\nReady in\n\n" + _count + "\n\n\n\n\n\n\n\n\n\n\n");
		_txtCountDown.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		_txtCountDown.setWrappingWidth(_window.GetWidth());
		_txtCountDown.setTextAlignment(TextAlignment.CENTER);
		
		root.getChildren().add(_txtCountDown);
		
		//Adding it to the window
		this.getChildren().add(root);
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
		
		// Create timer to keep track of mouse position
		_timeline = new Timeline(new KeyFrame(Duration.millis(1000), _tick));
		_timeline.setCycleCount(Timeline.INDEFINITE);		
		_timeline.play();
	}
	
	private EventHandler<ActionEvent> _tick = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent e) {
			_txtCountDown.setText("\n\n\nReady in\n\n" + --_count + "\n\n\n\n\n\n\n\n\n\n\n");
			
			if (_count <= 0) {
				_window.SetWindowScene(new Scene(new TrialScreen(_window, _wordlist), _window.GetWidth(), _window.GetHeight()));
				_timeline.stop();
			}
		}	
	};
}
