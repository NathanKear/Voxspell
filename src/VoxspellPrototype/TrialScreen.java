package VoxspellPrototype;

import java.util.HashMap;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TrialScreen extends Parent {

	private Window _window;

	private final String BTN_SPEAK_TEXT = "Speak";
	private final String BTN_ENTER_TEXT = "Enter";
	private final int HBX_SPACING = 10;
	private final int VBX_SPACING = 80;
	private final String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final String TXT_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final String TFD_FONT_COLOR = VoxspellPrototype.DARK_COLOR;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int TFD_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int SIDE_PADDING = 10;
	private final int TOP_BOTTOM_PADDING = 60;
	private final int BTN_WIDTH = 200;
	private final int BTN_HEIGHT = 70;
	private final int TFD_WIDTH = 300;
	private final int PROGBAR_HEIGHT = 20;
	private final int PROGBAR_GAP = 5;
	
	private final Image GREY_BLOCK = new Image(getClass().getResourceAsStream("/media/images/grey.png"));
	private final Image RED_BLOCK = new Image(getClass().getResourceAsStream("/media/images/red.png"));
	private final Image GREEN_BLOCK = new Image(getClass().getResourceAsStream("/media/images/green.png"));

	private final Text _txtQuiz;
	private TextField _tfdAttempt;
	private String _level;
	private HBox _progressBar;
	private ImageView[] _progressBarBlocks;

	private List<String> _words;
	private int _wordIndex = 0;
	private int _correctWords = 0;
	private HashMap<String, String> _userAttempts = new HashMap<String, String>();
	

	public TrialScreen(Window window, String wordlistName) {
		this._window = window;

		_level = wordlistName;
		
		Level currentLevel =  WordList.GetWordList().getLevelFromName(wordlistName);
		_words = currentLevel.GetWordsNonBias(currentLevel.Size());
		
		// Create root pane and set its size to whole window
		VBox root = new VBox(VBX_SPACING);
		root.setPrefWidth(_window.GetWidth());
		root.setPrefHeight(_window.GetHeight());
		root.setPadding(new Insets(TOP_BOTTOM_PADDING, SIDE_PADDING, TOP_BOTTOM_PADDING, SIDE_PADDING));


		// Create quiz title text
		_txtQuiz = new Text("Quiz\n");
		_txtQuiz.prefWidth(_window.GetWidth());
		_txtQuiz.setTextAlignment(TextAlignment.CENTER);
		_txtQuiz.setWrappingWidth(_window.GetWidth());
		_txtQuiz.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		
		_progressBar = new HBox(PROGBAR_GAP);
		_progressBar.setPrefWidth(_window.GetWidth() - (2 * PROGBAR_GAP));
		_progressBar.setPrefHeight(PROGBAR_HEIGHT);
		
		_progressBarBlocks = new ImageView[currentLevel.GetGoldThreshold()];
		
		for (int i = 0; i < _progressBarBlocks.length; i++) {
			_progressBarBlocks[i] = new ImageView(GREY_BLOCK);
			_progressBarBlocks[i].setFitHeight(PROGBAR_HEIGHT);
			_progressBarBlocks[i].setFitWidth((_window.GetWidth() - (_words.size() + 1) * PROGBAR_GAP) / _words.size());
			_progressBar.getChildren().add(_progressBarBlocks[i]);
		}

		// Add all nodes to root pane
		root.getChildren().addAll(_txtQuiz, buildCenterPane(BTN_HEIGHT), _progressBar);

		// Add root pane to parent
		this.getChildren().addAll(root);

		// Color background
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");

		new FestivalSpeakTask("Spell " + currentWord()).run();
	}

	private Pane buildCenterPane(double desiredHeight) {
		// Build center pane
		HBox centerPane = new HBox(HBX_SPACING);

		// Create center pane nodes
		Button btnSpeak = new Button(BTN_SPEAK_TEXT);
		Button btnEnter = new Button(BTN_ENTER_TEXT);
		_tfdAttempt = new TextField();

		// Add nodes to center pane
		centerPane.getChildren().addAll(btnSpeak, _tfdAttempt, btnEnter);

		// Set node styles
		btnSpeak.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnEnter.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		_tfdAttempt.setStyle("-fx-font: " + TFD_FONT_SIZE + " arial;" +
				"-fx-text-fill: " + TFD_FONT_COLOR + ";");

		// Center text in text-field
		_tfdAttempt.setAlignment(Pos.CENTER);

		// Set node dimensions
		btnEnter.setPrefWidth(BTN_WIDTH);
		btnEnter.setPrefHeight(BTN_HEIGHT);
		btnSpeak.setPrefWidth(BTN_WIDTH);
		btnSpeak.setPrefHeight(BTN_HEIGHT);
		_tfdAttempt.setPrefWidth(TFD_WIDTH);
		_tfdAttempt.setPrefHeight(BTN_HEIGHT);

		centerPane.setAlignment(Pos.CENTER);

		// Set action for speak button
		btnSpeak.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				new FestivalSpeakTask(currentWord()).run();
			}
		});

		btnEnter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {				
				attemptWord(_tfdAttempt.getText());
			}
		});

		_tfdAttempt.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					attemptWord(_tfdAttempt.getText());
				}
			}	
		});

		return centerPane;
	}

	/**
	 * Test entered word for correctness and update the GUI
	 * @param word
	 * @return whether word is correct or not
	 */
	private boolean attemptWord(String word) {

		_txtQuiz.setText("Quiz\n");

		word = word.trim();

		if (word.equals("")) {
			// Word attempt must contain some characters		
			_txtQuiz.setText("Quiz\nEnter a word"); 

			return false;
		}

		if (word.contains(" ")) {
			// Word attempt may not contain white space
			_txtQuiz.setText("Quiz\nMay not contain spaces"); 

			return false;
		}

		if (!word.matches("[a-zA-Z]+")) {
			// Word attempt may only contain alphabet characters.

			_txtQuiz.setText("Quiz\nMay only contain letters"); 

			return false;
		}


		boolean correct = (word.toLowerCase().equals(currentWord().toLowerCase()));
		String speechOutput = "";
		
		WordList.GetWordList().AddWordStat(currentWord(), _level, correct);
		_userAttempts.put(currentWord(), word);

		if (correct) {
			speechOutput = speechOutput + "Correct..";
			_progressBarBlocks[_wordIndex].setImage(GREEN_BLOCK);
		} else {
			speechOutput = speechOutput + "Incorrect..";
			_progressBarBlocks[_wordIndex].setImage(RED_BLOCK);
		}

		if (nextWord()) {
			speechOutput = speechOutput + " Spell " + currentWord();
		}

		new FestivalSpeakTask(speechOutput).run();
		_tfdAttempt.clear();

		return correct;
	}

	/**
	 * Move on to next word
	 * @return true if a next word is available.
	 */
	private boolean nextWord() {
		if (_wordIndex + 1 < _words.size()) {
			// There are words left to spell
			_wordIndex++;
			
			return true;
		} else {
			// No words left to spell
			_window.SetWindowScene(new Scene(new ResultsScreen(_window, _masteredWords, _words.size(), _level, _userAttempts), _window.GetWidth(), _window.GetHeight()));

			return false;
		}
	}

	private String currentWord() {
		return _words.get(_wordIndex);
	}
}
