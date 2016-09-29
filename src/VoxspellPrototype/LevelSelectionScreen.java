package VoxspellPrototype;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.ChoiceBox;
import javafx.util.Duration;

public class LevelSelectionScreen extends Parent {

	private Window _window;
	private List<Button> _btnLevels;	

	private final String TXT_SELECT_LEVEL = "\nSelect a level\n";
	private final int BUTTON_SEPERATION = 6;
	private final int SELECTION_BAR_PADDING = 10;
	private final double SELECTIONBAR_SCREENWIDTH_RATIO = 0.666;	
	private final double BUTTONS_PER_SCREEN = 6;
	private final double BTN_HEIGHT;
	private final double BTN_WIDTH;
	private final double SCROLL_EDGE_SIZE = 200;
	private final double SCROLL_SENSITIVITY = 400;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final String BTN_COLOR = VoxspellPrototype.DARK_BLUE;
	private final String BTN_LOCKED_COLOR = VoxspellPrototype.DARK;
	private final String BACK_COLOR = VoxspellPrototype.LIGHT_BLUE;
	private final String BTN_FONT_COLOR = VoxspellPrototype.WHITE;
	private final String TXT_FONT_COLOR = VoxspellPrototype.WHITE;
	private final double TMR_TICK_RATE = 60.0;

	private VBox _levelButtons;
	private double _scrollPosition = 0;
	private Timeline _timeline;

	private QuizType _quizType;

	public enum QuizType {
		REVIEW_QUIZ, NORMAL_QUIZ
	}

	public LevelSelectionScreen(Window window, String quizType) {
		super();

		//Checking what type of quiz the user wants
		if(quizType.equals("Review_Quiz")) {
			_quizType = QuizType.REVIEW_QUIZ;
		} else if (quizType.equals("Normal_Quiz")) {
			_quizType = QuizType.NORMAL_QUIZ;
		}

		this._window = window;

		BTN_HEIGHT = _window.GetHeight() / BUTTONS_PER_SCREEN;
		BTN_WIDTH = _window.GetWidth() * SELECTIONBAR_SCREENWIDTH_RATIO;

		//If the user is opening the application for the first time...
		File wordlog = new File("Word-Log");

		//If the user has unlocked levels then go straight to the Level Selection Screen
		if(WordList.GetWordList().size() > 0 && WordList.GetWordList().get(0).isUnlocked()) {
			GenerateLevelSelectionScreen();
			
		//Else if the word log doesn't exist then let the user choose what level they want to start at
		} else if(!wordlog.exists()) {
			ChooseLevelScreen();
		}

		//Or if the Word-Log file is there, then check if it is empty or not to decide which screen to show
		try {
			BufferedReader r = new BufferedReader(new FileReader(wordlog));

			if(r.readLine() == null) {
				ChooseLevelScreen();
			} else {
				GenerateLevelSelectionScreen();
			}

			r.close();
		} catch (IOException e) {

		}


	}

	/**
	 * This generates the screen where users can choose which level to start at
	 */
	private void ChooseLevelScreen() {
		
		//Get the WordList
		final WordList wordlist = WordList.GetWordList();

		//Create the data structure for the level ComboBox
		ObservableList<String> options = FXCollections.observableArrayList();
		
		//Getting the names of each level and adding it to the options list
		for(int i = 0; i < wordlist.size(); i++) {
			Level level = wordlist.get(i);
			String levelName = level.levelName();
			options.add(levelName);
		}

		//Creating the ComboBox
		final ChoiceBox<String> levelSelect = new ChoiceBox<String>(options);

		VBox root = new VBox(BUTTON_SEPERATION);

		// Set root node size
		root.setPrefWidth(_window.GetWidth());

		//Creating the label to tell the user what to do
		Text levelSelectLabel = new Text("Please select which level you wish to start at. All levels below "
				+ "the level you choose, and the level itself, will be unlocked!");
		levelSelectLabel.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		levelSelectLabel.setWrappingWidth(_window.GetWidth());
		
		root.setStyle("-fx-background-color: " + BACK_COLOR);
		root.setPadding(new Insets(SELECTION_BAR_PADDING));
		root.setPrefHeight(_window.GetHeight());
		root.setPrefWidth(_window.GetWidth());
		root.getChildren().addAll(levelSelectLabel, levelSelect);

		this.getChildren().add(root);

		//levelSelect.setPromptText("Select a level");
		levelSelect.setStyle("-fx-base: " + BTN_COLOR + "; -fx-font: " + BTN_FONT_SIZE + " arial; -fx-text-fill: " + TXT_FONT_COLOR + ";");
		//levelSelect.setPrefWidth(_window.GetWidth() - (SELECTION_BAR_PADDING * 2));
		levelSelect.setPrefHeight(30);
		levelSelect.autosize();

		//Adding a listener to see what level the user selects
		levelSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				//Unlocking every level up to and including the level the user selected
				boolean levelFound = false;
				for(int i = 0; i < wordlist.size(); i++) {
					Level level = wordlist.get(i);
					String levelName = level.levelName();

					if(!levelFound) {
						level.unlockLevel();
					}

					if(levelName.equals(newValue)) {
						levelFound = true;
					}
				}
				//Showing the level selection screen once the appropriate levels have been unlocked
				GenerateLevelSelectionScreen();
			}


		});

	}

	/**
	 * This method generates the screen where users can choose which level to do a quiz from
	 */
	private void GenerateLevelSelectionScreen() {

		//Getting the WordList
		WordList wordlist = WordList.GetWordList();

		// Create vbox, add all level buttons
		VBox root = new VBox(BUTTON_SEPERATION);

		//Creating and adding functionality to a return to menu button
		Button returnToMenuBtn = new Button("Return To Main Menu");
		returnToMenuBtn.setPrefWidth(BTN_WIDTH);
		returnToMenuBtn.setPrefHeight(BTN_HEIGHT);

		returnToMenuBtn.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");

		returnToMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new MainScreen(_window), _window.GetWidth(), _window.GetHeight()));
			}
		});

		// Set root node size
		root.setPrefWidth(_window.GetWidth());

		_btnLevels = new ArrayList<Button>();

		_btnLevels.add(returnToMenuBtn);

		//Creating text to tell the user what this screen is
		Text txtSelection = new Text(TXT_SELECT_LEVEL);
		txtSelection.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		root.getChildren().add(txtSelection);

		//Looping through all the levels and getting their name and adding a button for them
		ArrayList<String> levelName = new ArrayList<String>();

		for(int i = 0; i < wordlist.size(); i++) {
			levelName.add(wordlist.get(i).levelName());
			Level level = wordlist.get(i);
			final String listName = level.levelName();

			final Button btn = new Button();
			
			//Showing current mastered or failed stats depending on type of quiz selected
			switch (_quizType) {
			case NORMAL_QUIZ:
				btn.setText(listName + " " + WordList.GetWordList().getLevelFromName(listName).getMasteredWords().size() + "/" + WordList.GetWordList().getLevelFromName(listName).Size());
				break;
			case REVIEW_QUIZ:
				btn.setText(listName + " (" + WordList.GetWordList().getLevelFromName(listName).getFailedWords().size() + ")");
				break;
			default:
				break;
			}
			
			btn.setPrefWidth(BTN_WIDTH);
			btn.setPrefHeight(BTN_HEIGHT);

			if(_quizType == QuizType.NORMAL_QUIZ) {
				//If the level is unlocked then make a button that allows a normal quiz to be played
				if(level.isUnlocked()) {
					btn.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
							" -fx-base: " + BTN_COLOR + ";" + 
							" -fx-text-fill: " + BTN_FONT_COLOR + ";");

					btn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							_window.SetWindowScene(new Scene(new QuizScreen(_window, listName, _quizType), _window.GetWidth(), _window.GetHeight()));
						}
					});
					
				//Else this level is locked so grey out the button and tell the user it is locked if they try to click on it
				} else {
					btn.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
							" -fx-base: " + BTN_LOCKED_COLOR + ";" + 
							" -fx-text-fill: " + BTN_FONT_COLOR + ";");

					btn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							PopupWindow.DeployPopupWindow("You need to unlock this level before you can use play it!");
						}
					});
				}
			} else if(_quizType == QuizType.REVIEW_QUIZ) {
				//If the level is unlocked then create a normal button for it showing number of failed words
				if(level.isUnlocked()) {
					btn.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
							" -fx-base: " + BTN_COLOR + ";" + 
							" -fx-text-fill: " + BTN_FONT_COLOR + ";");

					//If there are no words to review then let the user know this if they click on the level
					if(WordList.GetWordList().GetRandomFailedWords(listName, 10).size() == 0) {
						btn.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								PopupWindow.DeployPopupWindow("You currently have no words to review!");
							}
						});
					
					//Else let the button have full functionality for a review quiz
					} else {
						btn.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								_window.SetWindowScene(new Scene(new QuizScreen(_window, listName, _quizType), _window.GetWidth(), _window.GetHeight()));
							}
						});
					}
					
				//Else the level is not unlocked so grey it out and let the user know it is locked if they click on it
				} else {
					btn.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
							" -fx-base: " + BTN_LOCKED_COLOR + ";" + 
							" -fx-text-fill: " + BTN_FONT_COLOR + ";");

					btn.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							PopupWindow.DeployPopupWindow("You need to unlock this level before you can use play it!");
						}
					});
				}
			}
			_btnLevels.add(btn);
		}

		root.getChildren().addAll(_btnLevels);
		root.setPrefWidth(_window.GetWidth());
		root.setAlignment(Pos.CENTER);
		root.setTranslateY(_scrollPosition);

		// Add padding around vbox (so buttons don't touch screen edge)
		root.setPadding(new Insets(SELECTION_BAR_PADDING));

		// Create timer to keep track of mouse position
		_timeline = new Timeline(new KeyFrame(Duration.millis(1000 / TMR_TICK_RATE), _scrollTimer));
		_timeline.setCycleCount(Timeline.INDEFINITE);		
		_timeline.play();

		this.getChildren().add(root);

		this._levelButtons = root;		

		// Set root node color
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
	}

	// Check mouse position for scroll action
	private EventHandler<ActionEvent> _scrollTimer = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent e) {
			Point mousePoint = MouseInfo.getPointerInfo().getLocation();
			Rectangle windowBounds = _window.GetBounds();

			// Check if window contains mouse.
			if (windowBounds.contains(mousePoint)) {
				
				// Get mouse coordinates relative to the windows space.
				Point relMousePoint = new Point(
						(int) (mousePoint.getY() - windowBounds.getY() - 30), 
						(int) (mousePoint.getX() - windowBounds.getX()));

				// Get distance to top or bottom of screen.
				double dist = relMousePoint.getX() < (_window.GetHeight() / 2) ? 
						relMousePoint.getX() : _window.GetHeight() - relMousePoint.getX();
						dist = Math.abs(dist);		
						
				// Only scroll if within certain range of screen edge.
				if (Math.abs(dist) < SCROLL_EDGE_SIZE) {
					double scrollSpeed = SCROLL_SENSITIVITY / dist;	
					
					// Get distance from top or bottom edge
					if (relMousePoint.getX() < _window.GetHeight() / 2) {
						_scrollPosition = _scrollPosition + scrollSpeed;
					} else {
						_scrollPosition = _scrollPosition - scrollSpeed;
					}
					
					// Calculate scroll position
					_scrollPosition = Math.min(0, _scrollPosition);
					_scrollPosition = Math.max(-(_levelButtons.getHeight() - _window.GetHeight()), _scrollPosition);
					
					// Translate scroll buttons
					_levelButtons.setTranslateY(_scrollPosition);
				}
			}
		}	
	};
}
