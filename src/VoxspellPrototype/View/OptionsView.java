package VoxspellPrototype.View;

import VoxspellPrototype.VoxspellPrototype;
import VoxspellPrototype.Window;
import VoxspellPrototype.Concurrent.FestivalSpeakTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * 
 * @author nathan kear & charles carey
 *
 */
public class OptionsView extends Parent {

	private Window _window;
	
	private final int TXT_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final String TXT_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final int OPTIONS_PADDING = 10;
	private final String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final Insets TXT_INSETS = new Insets(10, 10, 10, 30);
	private final int HBX_SPACING = 30;
	private final int TXT_WIDTH = 300;
	private final int CMB_WIDTH = 400;
	private final int CMB_HEIGHT = 40;

	/**
	 * Create new options screen for user to change text to speech parameters
	 * @param window
	 */
	public OptionsView(Window window) {
		super();

		_window = window;

		//Getting a vbox to put everything in
		VBox root = new VBox(40);
		root.setPadding(new Insets(OPTIONS_PADDING));
		
		//Adding it to the window
		this.getChildren().add(root);

		//Creating the two HBoxes for the voice speed and voice type options
		HBox voiceSpeedBox = new HBox(HBX_SPACING);
		voiceSpeedBox.setPadding(TXT_INSETS);

		HBox voiceTypeBox = new HBox(HBX_SPACING);
		voiceTypeBox.setPadding(TXT_INSETS);
		
		//Setting up the options label
		Text optionsLabel = new Text("Options");

		optionsLabel.prefWidth(_window.GetWidth());
		optionsLabel.setTextAlignment(TextAlignment.CENTER);
		optionsLabel.setWrappingWidth(_window.GetWidth());
		
		optionsLabel.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" + 
				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");
		
		//Creating a button to return to menu
		Button returnToMenuBtn = new Button("Return To Main Menu");
		
		returnToMenuBtn.setPrefWidth(_window.GetWidth() - (OPTIONS_PADDING * 2));
		returnToMenuBtn.setPrefHeight(_window.GetHeight()/8);
		returnToMenuBtn.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");

		returnToMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				_window.SetWindowScene(new Scene(new MainView(_window), _window.GetWidth(), _window.GetHeight()));
			}
		});


		root.getChildren().addAll(returnToMenuBtn, optionsLabel, voiceSpeedBox, voiceTypeBox);
		
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
		
		root.setPrefHeight(_window.GetHeight());
		root.setPrefWidth(_window.GetWidth());
		
		//Creating the ComboBox for voice speed
		ObservableList<String> voiceSpeedOptions = FXCollections.observableArrayList(
				"Normal",
				"Slow",
				"Very Slow                                                                        "
				);

		

		final ChoiceBox<String> voiceSpeedComboBox = new ChoiceBox<String>(voiceSpeedOptions);
		voiceSpeedComboBox.setStyle("-fx-base: " + BTN_COLOR + "; -fx-font: " + BTN_FONT_SIZE + " arial; -fx-text-fill: " + TXT_FONT_COLOR + ";");
		voiceSpeedComboBox.setPrefHeight(CMB_HEIGHT);
		voiceSpeedComboBox.setPrefWidth(CMB_WIDTH);

		// Load current voice speed
		double currentSpeed = FestivalSpeakTask.getSpeed();

		int indexToGet = 0;
		if (currentSpeed == 1.0) {
			indexToGet = 0;
		} else if (currentSpeed == 1.5) {
			indexToGet = 1;
		} else {
			indexToGet = 2;
		}

		voiceSpeedComboBox.setValue(voiceSpeedOptions.get(indexToGet));

		Label voiceSpeedLabel = new Label("Select voice speed");
		voiceSpeedLabel.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + VoxspellPrototype.DARK_COLOR + ";");
		voiceSpeedLabel.setPrefWidth(TXT_WIDTH);

		voiceSpeedBox.getChildren().addAll(voiceSpeedLabel, voiceSpeedComboBox);

		//Creating the ComboBox for voice type
		ObservableList<String> voiceTypeOptions = FXCollections.observableArrayList(
				"Male Voice 1",
				"Male Voice 2",
				"New Zealand Male Voice                                                 "
				);

		final ChoiceBox<String> voiceTypeComboBox = new ChoiceBox<String>(voiceTypeOptions);
		voiceTypeComboBox.setStyle("-fx-base: " + BTN_COLOR + "; -fx-font: " + BTN_FONT_SIZE + " arial; -fx-text-fill: " + TXT_FONT_COLOR + ";");
		
		//voiceTypeComboBox.setPromptText("Choose a voice");
		voiceTypeComboBox.setPrefHeight(CMB_HEIGHT);
		voiceTypeComboBox.setPrefWidth(CMB_WIDTH);
		
		// Load current voice type
		String currentVoice = FestivalSpeakTask.getVoice();

		indexToGet = 0;
		if (currentVoice.equals("kal_diphone")) {
			indexToGet = 0;
		} else if (currentVoice.equals("rab_diphone")) {
			indexToGet = 1;
		} else {
			indexToGet = 2;
		}

		voiceTypeComboBox.setValue(voiceTypeOptions.get(indexToGet));
			

		Label voiceTypeLabel = new Label("Select voice type");
		voiceTypeLabel.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + VoxspellPrototype.DARK_COLOR + ";");
		voiceTypeLabel.setPrefWidth(TXT_WIDTH);
		
		voiceTypeBox.getChildren().addAll(voiceTypeLabel, voiceTypeComboBox);
		
		//Adding the listeners for the two ComboBoxes so that when the user selects an option it will take effect
		voiceTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
					String voice = "";
					// Set new voice type and then play example of new voice
				if(newValue.trim().equals("Male Voice 1")) {
					voice = "kal_diphone";
				} else if (newValue.trim().equals("Male Voice 2")) {
					voice = "rab_diphone";
				} else if (newValue.trim().equals("New Zealand Male Voice")) {
					voice = "akl_nz_jdt_diphone";
				}
				FestivalSpeakTask.changeVoice(voice);
				new FestivalSpeakTask("Hello World!").run();
			}

		});
		
		voiceSpeedComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
					// Set new voice speed and then play example of new voice
				if (newValue.trim().equals("Normal")) {
					FestivalSpeakTask.SetSpeed(1.0);
					new FestivalSpeakTask("Normal speed").run();
				} else if (newValue.trim().equals("Slow")) {
					FestivalSpeakTask.SetSpeed(1.5);
					new FestivalSpeakTask("Slow speed").run();
				} else {
					FestivalSpeakTask.SetSpeed(2.0);
					new FestivalSpeakTask("Very slow speed").run();
				}
			}

		});

	}

}
