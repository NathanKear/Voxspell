package VoxspellPrototype;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Display results of quiz for user to see and also present reward option
 * @author nathan
 *
 */
public class ResultsScreen extends Parent {

	private Window _window;

	// Content styling constants
	private final String BTN_RETURN_TEXT = "Return to Menu";
	private final String BTN_TESTED_WORDS_TEXT = "View Mistakes";
	private final String BTN_REWARD_TEXT = "Watch Reward";
	private final int VBX_SPACING = 20;
	private final String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final String TXT_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int SIDE_PADDING = 10;
	private final int TOP_BOTTOM_PADDING = 60;
	private final double BTNWIDTH_SCREENWIDTH_RATIO = 0.666;
	private final int BTN_HEIGHT = 70;

	public ResultsScreen(Window window, int correctWords, int wordListLength, String listName, HashMap<String, String> userAttempts) {
		this._window = window;

		// Create root pane and set its size to whole window
		VBox root = new VBox(VBX_SPACING);
		root.setPrefWidth(_window.GetWidth());
		root.setPrefHeight(_window.GetHeight());
		root.setPadding(new Insets(TOP_BOTTOM_PADDING, SIDE_PADDING, TOP_BOTTOM_PADDING, SIDE_PADDING));

		// Create quiz title text
		Text txtResults;
		txtResults = new Text("You Got " + correctWords + "/" + wordListLength + " Correct");
		txtResults.prefWidth(_window.GetWidth());
		txtResults.setTextAlignment(TextAlignment.CENTER);
		txtResults.setWrappingWidth(_window.GetWidth());
		txtResults.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");

		// Create button that links to reward video
		Button btnReward;
		btnReward = new Button(BTN_REWARD_TEXT);
		btnReward.setPrefWidth(BTNWIDTH_SCREENWIDTH_RATIO * _window.GetWidth());
		btnReward.setPrefHeight(BTN_HEIGHT);
		btnReward.setAlignment(Pos.CENTER);
		btnReward.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		// Does user get special reward (inverts video colors).
		final boolean specialReward = correctWords == wordListLength;

		// Open media screen if button clicked.
		btnReward.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new MediaScreen(_window, specialReward), _window.GetWidth(), _window.GetHeight()));
			}
		});

		// Button to show users what words were tested
		Button btnTestedWords;
		btnTestedWords = new Button(BTN_TESTED_WORDS_TEXT);
		btnTestedWords.setPrefWidth(BTNWIDTH_SCREENWIDTH_RATIO * _window.GetWidth());
		btnTestedWords.setPrefHeight(BTN_HEIGHT);
		btnTestedWords.setAlignment(Pos.CENTER);
		btnTestedWords.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		
		if (correctWords == wordListLength) {
			btnTestedWords.setDisable(true);
		}
		
		final int words = correctWords;
		final int length = wordListLength;
		final String name = listName;
		final HashMap<String, String> attempts = userAttempts;
		
		btnTestedWords.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new TestedWordsScreen(_window, attempts, words, length, name)));
			}
		});

		// Button to return to the main menu.
		Button btnReturn;
		btnReturn = new Button(BTN_RETURN_TEXT);
		btnReturn.setPrefWidth(BTNWIDTH_SCREENWIDTH_RATIO * _window.GetWidth());
		btnReturn.setPrefHeight(BTN_HEIGHT);
		btnReturn.setAlignment(Pos.CENTER);
		btnReturn.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");	

		btnReturn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new MainScreen(_window), _window.GetWidth(), _window.GetHeight()));
			}
		});

		// Add root node to current scene.
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(txtResults, btnReward, btnTestedWords, btnReturn);		
		this.getChildren().addAll(root);		
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");

		if (correctWords < wordListLength - 1) {
			// Dont unlock reward or next level.
			btnReward.setDisable(true);	
		} else {
			
			new FFPlayTask(VoxspellPrototype.CHEER_SOURCE).run();
			
			// Unlock reward and next level.
			if (listName == WordList.GetWordList().HighestUnlockedLevel().levelName()) {
				String level = "";

				// Deploy popup to inform user of new quiz level.
				if ((level = WordList.GetWordList().UnlockNextLevel()) != null) {
					if (level != null && !level.equals(""))
						PopupWindow.DeployPopupWindow(level + " unlocked!");
				}
			}
		}	

	}
}
