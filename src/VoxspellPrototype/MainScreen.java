package VoxspellPrototype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MainScreen extends Parent {

	private Window _window;
		
	// Constants gone wild!
	private final String TXT_WELCOME = "Hello World!\n\n&\n\nWelcome to VoxSpell";
	private final int BUTTON_SEPERATION = 6; 
	private final int MENU_BAR_PADDING = 10;
	private final double MENUBAR_SCREENWIDTH_RATIO = 0.333;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final String BTN_NEW_TEXT = "New Quiz";
	private final String BTN_REVIEW_TEXT = "Review Mistakes";
	private final String BTN_STATS_TEXT = "View Stats";
	private final String BTN_CLEAR_TEXT = "Clear Stats";
	private final String BTN_QUIT_TEXT = "Quit";
	private final String BTN_OPTIONS_TEXT = "Options";
	private final String BTN_COLOR = VoxspellPrototype.DARK_BLUE;
	private final String BACK_COLOR = VoxspellPrototype.LIGHT_BLUE;
	private final String BTN_FONT_COLOR = VoxspellPrototype.WHITE;
	private final String TXT_FONT_COLOR = VoxspellPrototype.WHITE;
	
	private final int TEXT_CEILING_SEPERATION = 160;
	
	
	public MainScreen(Window window) {
		super();
		
		this._window = window;
		
		// Create root node and set its size
		HBox root = new HBox(0);
		root.setPrefWidth(_window.GetWidth());
		root.setPrefHeight(_window.GetHeight());
		
		// Create menu bar
		double menuBarWidth = _window.GetWidth() * MENUBAR_SCREENWIDTH_RATIO;
		Pane menuPane = buildMenuBar(menuBarWidth);
		
		Text welcomeText = new Text(TXT_WELCOME);
		
		// Set text area width to that remaining of windows width after
		// menu bar width and padding is removed
		welcomeText.setWrappingWidth( root.getPrefWidth()
				- menuPane.getPrefWidth() 
				- menuPane.getPadding().getLeft() 
				- menuPane.getPadding().getRight());
		
		// Center align text
		welcomeText.setTextAlignment(TextAlignment.CENTER);
		
		// Set text y translation (distance from top of window to text)
		welcomeText.setTranslateY(TEXT_CEILING_SEPERATION);
		
		welcomeText.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		
		// Add menu bar and text to root node
		root.getChildren().addAll(menuPane, welcomeText);

		this.getChildren().add(root);
		
		// Set root node color
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
	}
	
	private Pane buildMenuBar(double desiredWidth) {
		Button btnNew, btnReview, btnStats, btnClear, btnQuit, btnOptions;

		// Create vbox with specific dimensions
		VBox menuButtons = new VBox(BUTTON_SEPERATION);
		menuButtons.setPrefWidth(desiredWidth);
		menuButtons.setPrefHeight(_window.GetHeight());

		// Create buttons
		btnNew = new Button(BTN_NEW_TEXT);
		btnReview = new Button(BTN_REVIEW_TEXT);
		btnStats = new Button(BTN_STATS_TEXT);
		btnClear = new Button(BTN_CLEAR_TEXT);
		btnQuit = new Button(BTN_QUIT_TEXT);
		btnOptions = new Button(BTN_OPTIONS_TEXT);
		
		// Set button style properties
		btnNew.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnReview.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnStats.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnClear.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnQuit.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnOptions.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		
		// Set width and height of buttons
		btnNew.setMinWidth(menuButtons.getPrefWidth()); 
		btnNew.setPrefHeight(Integer.MAX_VALUE);
		
		btnReview.setMinWidth(menuButtons.getPrefWidth()); 
		btnReview.setPrefHeight(Integer.MAX_VALUE);
		
		btnStats.setMinWidth(menuButtons.getPrefWidth()); 
		btnStats.setPrefHeight(Integer.MAX_VALUE);
		
		btnClear.setMinWidth(menuButtons.getPrefWidth()); 
		btnClear.setPrefHeight(Integer.MAX_VALUE);
		
		btnQuit.setMinWidth(menuButtons.getPrefWidth()); 
		btnQuit.setPrefHeight(Integer.MAX_VALUE);
		
		btnOptions.setMinWidth(menuButtons.getPrefWidth()); 
		btnOptions.setPrefHeight(Integer.MAX_VALUE);
		
		// Add buttons to pane
		menuButtons.getChildren().addAll(btnNew, btnReview, btnStats, btnClear, btnOptions, btnQuit);
		
		// Add padding around vbox (so buttons don't touch screen edge)
		menuButtons.setPadding(new Insets(MENU_BAR_PADDING));
		
		// Define button actions
		btnNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new LevelSelectionScreen(_window, "Normal_Quiz"), _window.GetWidth(), _window.GetHeight()));
			}	
		});
		
		btnReview.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new LevelSelectionScreen(_window, "Review_Quiz"), _window.GetWidth(), _window.GetHeight()));
			}	
		});
		
		btnStats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new StatisticsScreen(_window), _window.GetWidth(), _window.GetHeight()));
			}	
		});
		
		btnClear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				PopupWindow.DeployPopupWindow("Cleared Statistics");
				WordList.GetWordList().ClearStats();
			}	
		});
		
		btnQuit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				WordList wordList = WordList.GetWordList();
				wordList.saveWordListToDisk();
				Platform.exit();
			}	
		});
		
		btnOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new OptionsScreen(_window), _window.GetWidth(), _window.GetHeight()));
			}	
		});
		
		return menuButtons;
	}
}
