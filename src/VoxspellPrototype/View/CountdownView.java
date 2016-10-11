package VoxspellPrototype.View;

import VoxspellPrototype.VoxspellPrototype;
import VoxspellPrototype.Window;
import VoxspellPrototype.Concurrent.FFPlayTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class CountdownView extends Parent {

	private Window _window;
	
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final Text _txtCountDown;
	private final Timeline _timeline;
	private int _count = 5;
	private final String _wordlist;

	public CountdownView(Window window, String wordlist) {
		super();

		_window = window;
		_wordlist = wordlist;
		
		VBox root = new VBox();

		_txtCountDown = new Text("\n\n\nReady in\n\n" + _count + "\n\n\n\n\n\n\n\n\n\n\n");
		_txtCountDown.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");
		_txtCountDown.setWrappingWidth(_window.GetWidth());
		_txtCountDown.setTextAlignment(TextAlignment.CENTER);
		
		root.getChildren().add(_txtCountDown);
		
		//Adding it to the window
		this.getChildren().add(root);
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
		
		// Create timer to keep track of countdown
		_timeline = new Timeline(new KeyFrame(Duration.millis(1000), _tick));
		_timeline.setCycleCount(Timeline.INDEFINITE);		
		_timeline.play();
		
		new FFPlayTask(VoxspellPrototype.TICK_SOURCE).run();
	}
	
	private EventHandler<ActionEvent> _tick = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent e) {
			_txtCountDown.setText("\n\n\nReady in\n\n" + --_count + "\n\n\n\n\n\n\n\n\n\n\n");
			
			new FFPlayTask(VoxspellPrototype.TICK_SOURCE).run();
			
			if (_count <= 0) {
				_window.SetWindowScene(new Scene(new TrialView(_window, _wordlist), _window.GetWidth(), _window.GetHeight()));
				_timeline.stop();
			}
		}	
	};
}
