package VoxspellPrototype.View;

import java.io.File;

import VoxspellPrototype.VoxspellPrototype;
import VoxspellPrototype.Window;
import VoxspellPrototype.Model.QuizType;
import VoxspellPrototype.Model.WordListModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

public class MainView extends Parent {

	private Window _window;
		
	// Constants gone wild!
	private final String TXT_WELCOME = "Welcome to VoxSpell";
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final String BTN_NEW_TEXT = "Quiz";
	private final String BTN_TRIAL_TEXT = "Time-\nTrial";
	private final String BTN_STATS_TEXT = "Stats";
	private final String BTN_ADDLIST_TEXT = "Swap\nList";
	private final String BTN_HELP_TEXT = "?";
	private final String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final int BTN_LRG_WIDTH = 250;
	private final int BTN_LRG_HEIGHT = 250;
	private final int BTN_SML_WIDTH = 75;
	private final int BTN_SML_HEIGHT = 65;
	private final Insets INSETS = new Insets(30, 30, 30, 30);
	private final int GRD_HGAP = 30;
	private final int GRD_VGAP = 30;
	
	private final Image IMG_NEW = new Image(getClass().getResourceAsStream("/media/images/btnNewIcon.png"));
	private final Image IMG_OPTIONS = new Image(getClass().getResourceAsStream("/media/images/btnOptionsIcon50x50.png"));
	private final Image IMG_ADDLIST = new Image(getClass().getResourceAsStream("/media/images/btnImportQuiz.png"));
	private final Image IMG_STATS = new Image(getClass().getResourceAsStream("/media/images/btnStatsIcon.png"));
	private final Image IMG_TRIAL = new Image(getClass().getResourceAsStream("/media/images/btnTrialIcon.png"));
	
	
	public MainView(Window window) {
		super();
		
		this._window = window;
		
		// Create root node and set its size
		GridPane root = new GridPane();
		root.setPrefWidth(_window.GetWidth());
		root.setPrefHeight(_window.GetHeight());
		root.setHgap(GRD_HGAP);
		root.setVgap(GRD_VGAP);
		root.getColumnConstraints().add(new ColumnConstraints(75));
		root.setGridLinesVisible(false);
		root.setPadding(INSETS);
		
		// Create welcome window text
		Text welcomeText = new Text(TXT_WELCOME);
		// Center align text
		welcomeText.setWrappingWidth((BTN_LRG_WIDTH * 2) + GRD_HGAP);
		welcomeText.setTextAlignment(TextAlignment.CENTER);	
		welcomeText.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");
		
		// Add menu bar and text to root node
		root.add(welcomeText, 1, 1, 3, 1);
		
		Button btnNew, btnTrial, btnStats, btnAddList, btnOptions, btnHelp;
		
		ImageView imgOptions = new ImageView(IMG_OPTIONS);
		
		imgOptions.setFitHeight(45);
		imgOptions.setFitWidth(45);
		
		// Create buttons		
		btnNew = new Button(BTN_NEW_TEXT, new ImageView(IMG_NEW));
		btnTrial = new Button(BTN_TRIAL_TEXT, new ImageView(IMG_TRIAL));
		btnStats = new Button(BTN_STATS_TEXT, new ImageView(IMG_STATS));
		btnAddList = new Button(BTN_ADDLIST_TEXT, new ImageView(IMG_ADDLIST));
		btnHelp = new Button(BTN_HELP_TEXT);
		btnOptions = new Button("", imgOptions);
//		btnQuit = new Button("", imgQuit);
		
		// Set button style properties
		btnNew.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnTrial.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnStats.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnAddList.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");		
		btnOptions.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		btnHelp.setStyle("-fx-font: " + (BTN_FONT_SIZE + 10) + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
//		btnQuit.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
//				" -fx-base: " + BTN_COLOR + ";" + 
//				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		
		// Set width and height of buttons
		btnNew.setPrefWidth(BTN_LRG_WIDTH); 
		btnNew.setPrefHeight(BTN_LRG_HEIGHT);
		
		btnTrial.setPrefWidth(BTN_LRG_WIDTH); 
		btnTrial.setPrefHeight(BTN_LRG_HEIGHT);
		
		btnStats.setPrefWidth(BTN_LRG_WIDTH); 
		btnStats.setPrefHeight(BTN_LRG_HEIGHT);
		
		btnAddList.setPrefWidth(BTN_LRG_WIDTH); 
		btnAddList.setPrefHeight(BTN_LRG_HEIGHT);
		
//		btnQuit.setPrefWidth(BTN_SML_WIDTH); 
//		btnQuit.setPrefHeight(BTN_SML_HEIGHT);
		
		btnOptions.setPrefWidth(BTN_SML_WIDTH); 
		btnOptions.setPrefHeight(BTN_SML_HEIGHT);
		
		btnHelp.setPrefWidth(BTN_SML_WIDTH);
		btnHelp.setPrefHeight(BTN_SML_HEIGHT);
		
		// Add buttons to pane
		root.add(btnNew, 1, 2);
		root.add(btnTrial, 1, 3);
		root.add(btnAddList, 2, 2);
		root.add(btnStats, 2, 3);
//		root.add(btnQuit, 3, 3);
//		GridPane.setValignment(btnQuit, VPos.BOTTOM);
		root.add(btnOptions, 0, 3);
		GridPane.setValignment(btnOptions, VPos.BOTTOM);
		root.add(btnHelp, 3, 3);
		GridPane.setValignment(btnHelp, VPos.BOTTOM);

		// Define button actions
		btnNew.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new LevelSelectionView(_window, QuizType.Normal), _window.GetWidth(), _window.GetHeight()));
			}	
		});
		
		btnTrial.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new LevelSelectionView(_window, QuizType.Trial), _window.GetWidth(), _window.GetHeight()));
			}	
		});
		
		btnStats.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new StatsView(_window), _window.GetWidth(), _window.GetHeight()));
			}	
		});
		
		btnAddList.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				FileChooser chooser = new FileChooser();
				
				File wordlistDir = new File("WordLists");
				
				if (wordlistDir.exists()) {
					chooser.setInitialDirectory(wordlistDir);
				}
				
				chooser.setTitle("Select a new list to add");
				chooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter(".txt", "*.txt"),
						new FileChooser.ExtensionFilter("All", "*.*"));
				File file = chooser.showOpenDialog(_window.GetWindowStage());
				
				if (file != null) {
					WordListModel.SetWordFile(file.getPath());
					WordListModel.GetWordList().ReloadWordList();
				}
			}	
		});
		
//		btnQuit.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent arg0) {
//				WordList wordList = WordList.GetWordList();
//				wordList.saveWordListToDisk();
//				Platform.exit();
//			}	
//		});
		
		btnHelp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new HelpView(_window), _window.GetWidth(), _window.GetHeight()));
			}	
		});
		
		btnOptions.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new OptionsView(_window), _window.GetWidth(), _window.GetHeight()));
			}	
		});
		
		// Set root node color
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
		
		this.getChildren().add(root);
	}
}
