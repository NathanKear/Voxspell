package VoxspellPrototype.View;

import VoxspellPrototype.VoxspellPrototype;
import VoxspellPrototype.Window;
import VoxspellPrototype.Concurrent.FFPlayTask;
import VoxspellPrototype.Model.LevelModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Display results of quiz for user to see and also present reward option
 * @author nathan kear & charles carey
 *
 */
public class TrialResultsView extends Parent {

	private Window _window;

	// Content styling constants
	private final String BTN_RETURN_TEXT = "Return";
	private final String BTN_REWARD_TEXT = "Reward";
	private final int VBX_SPACING = 20;
	private final String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int SIDE_PADDING = 10;
	private final int TOP_BOTTOM_PADDING = 60;
	private final double BTNWIDTH_SCREENWIDTH_RATIO = 0.666;
	private final int BTN_HEIGHT = 70;
	
	private final Image MEDAL_NONE = new Image(getClass().getResourceAsStream("/media/images/noMedalIcon.png"));
	private final Image MEDAL_GOLD = new Image(getClass().getResourceAsStream("/media/images/goldIcon.png"));
	private final Image MEDAL_SILVER = new Image(getClass().getResourceAsStream("/media/images/silverIcon.png"));
	private final Image MEDAL_BRONZE = new Image(getClass().getResourceAsStream("/media/images/bronzeIcon.png"));

	public TrialResultsView(Window window, int correctWords, boolean isRecord, LevelModel level) {
		this._window = window;

		// Create root pane and set its size to whole window
		VBox root = new VBox(VBX_SPACING);
		root.setPrefWidth(_window.GetWidth());
		root.setPrefHeight(_window.GetHeight());
		root.setPadding(new Insets(TOP_BOTTOM_PADDING, SIDE_PADDING, TOP_BOTTOM_PADDING, SIDE_PADDING));

		ImageView imgMedal = new ImageView();
		
		// Create quiz title text
		Text txtResults;
		txtResults = new Text();
		txtResults.prefWidth(_window.GetWidth());
		txtResults.setTextAlignment(TextAlignment.CENTER);
		txtResults.setWrappingWidth(_window.GetWidth());
		txtResults.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");
		
		if (correctWords >= level.GetGoldThreshold()) {
			new FFPlayTask(VoxspellPrototype.CHEER_SOURCE).run();
			txtResults.setText("Gold! Congratulations.\n");
			imgMedal.setImage(MEDAL_GOLD);
		} else if (correctWords >= level.GetSilverThreshold()) {
			txtResults.setText("Silver! Good job.\n");
			imgMedal.setImage(MEDAL_SILVER);
		} else if (correctWords >= level.GetBronzeThreshold()) {
			txtResults.setText("Bronze! Not bad.\n");
			imgMedal.setImage(MEDAL_BRONZE);
		} else {
			txtResults.setText("No medal, sorry.\n");
			imgMedal.setImage(MEDAL_NONE);
		}
		
		if (isRecord) {
			txtResults.setText(txtResults.getText() + "New Record, " + correctWords + " correct.");
		} else {
			txtResults.setText(txtResults.getText() + "You got: " + correctWords + ". Record: " + level.GetCurrentRecord());
		}

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
		final boolean specialReward = isRecord;

		// Open media screen if button clicked.
		btnReward.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new MediaView(_window, specialReward), _window.GetWidth(), _window.GetHeight()));
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
				_window.SetWindowScene(new Scene(new MainView(_window), _window.GetWidth(), _window.GetHeight()));
			}
		});

		// Add root node to current scene.
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(imgMedal, txtResults, btnReward, btnReturn);		
		this.getChildren().addAll(root);		
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");

	}
}
