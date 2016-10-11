package VoxspellPrototype.View;

import VoxspellPrototype.VoxspellPrototype;
import VoxspellPrototype.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HelpView extends Parent {

	private Window _window;
	
	private final String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final String TXT_RETURN = "Return to Menu";
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int BTN_HEIGHT = 70;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE_FINE;
	private final Insets INSETS = new Insets(20, 20, 20, 20);
	private final Insets TXT_INSETS = new Insets(20, 20, 20, 20);
	private final int VBX_GAP = 10;
	private final String TXT_HELP = 
			"\n"
			+ "\n"
			+ "Help:\n"
			+ "\n"
			+ "Quiz: Test on up to 10 unique words selected from the choosen spelling list. "
			+ "Words that are failed often are more likely to appear than words that are mostly spelled correctly.\n"
			+ "\n"
			+ "Time-Trial: Test on a choosen spelling list to spell as many words correctly in 60 seconds. "
			+ "Medals are awarded based on number of correctly spelled words.\n"
			+ "\n"
			+ "		10 - 14 = Bronze Medal\n"
			+ "		15 - 16 = Silver Medal\n"
			+ "		17 - 18 = Gold Medal\n"
			+ "\n"
			+ "Stats: View your statistics for each level including the success rate for spelling each word and "
			+ "the total attempts at spelling each word\n"
			+ "\n"
			+ "Swap List: Import a different list and its associated stats and medals into memory."
			+ "All stats and medals for the current list are saved."
			+ "The file to load must be a .txt file."
			+ "File must be formatted with list name on a new line starting with an '%', and words one to each line."
			+ "Each list is terminated by the start of another list or end of file.\n"
			+ "\n"
			+ "\n"
			+ "		For example:\n"
			+ "\n"
			+ "		%African Animals:\n"
			+ "		Elephant\n"
			+ "		Giraffe\n"
			+ "		Rhine\n"
			+ "		Zebra\n"
			+ "		%Countries:\n"
			+ "		America\n"
			+ "		Brazil\n"
			+ "		China\n"
			+ "\n"
			+ "\n"
			+ "Options: Can select several different speech voices and speeds.\n"
			+ "\n"
			+ "\n"
			+ "Sound Effects:\n"
			+ "		Correct noise from https://www.freesound.org/people/ertfelda/sounds/243701/\n"
			+ "		Incorrect noise from https://www.freesound.org/people/Bertrof/sounds/131657/\n"
			+ "		Cheering from https://www.freesound.org/people/Tomlija/sounds/99634/\n"
			+ "		Clock tick from https://www.freesound.org/people/Jagadamba/sounds/254316/\n"
			+ "";

	public HelpView(Window window) {
		this._window = window;
		
		ScrollPane scroll = new ScrollPane();
		scroll.setPrefSize(_window.GetWidth() - INSETS.getLeft() - INSETS.getRight(), _window.GetHeight() - BTN_HEIGHT - VBX_GAP - INSETS.getTop() - INSETS.getBottom());
		scroll.setStyle("-fx-background: " + BACK_COLOR + ";");
		
		Text helpText = new Text(TXT_HELP);
		
		helpText.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");
		helpText.prefWidth(_window.GetWidth() - INSETS.getLeft() - INSETS.getRight() - TXT_INSETS.getLeft() - TXT_INSETS.getRight() - 20);
		helpText.setWrappingWidth(_window.GetWidth() - INSETS.getLeft() - INSETS.getRight() - TXT_INSETS.getLeft() - TXT_INSETS.getRight() - 20);
		
		VBox textPane = new VBox();
		textPane.setPadding(TXT_INSETS);
		textPane.getChildren().add(helpText);
		
		scroll.setContent(textPane);
		
		VBox root = new VBox(VBX_GAP);
		root.setPadding(INSETS);

		Button returnToMenuBtn;
		returnToMenuBtn = new Button(TXT_RETURN);
		returnToMenuBtn.setPrefWidth(_window.GetWidth() - INSETS.getLeft() - INSETS.getRight());
		returnToMenuBtn.setPrefHeight(BTN_HEIGHT);
		returnToMenuBtn.setAlignment(Pos.CENTER);
		returnToMenuBtn.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		
		returnToMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new MainView(_window), _window.GetWidth(), _window.GetHeight()));
			}
			
		});

		root.setMinHeight(_window.GetHeight());
		root.setMinWidth(_window.GetWidth());
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
		
		root.getChildren().addAll(returnToMenuBtn, scroll);

		this.getChildren().add(root);
		
	}

}
