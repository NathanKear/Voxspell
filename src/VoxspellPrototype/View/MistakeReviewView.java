package VoxspellPrototype.View;

import java.util.HashMap;
import java.util.Map;

import VoxspellPrototype.Window;
import VoxspellPrototype.VoxspellPrototype;
import VoxspellPrototype.View.QuizResultsView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * 
 * @author nathan kear & charles carey
 *
 */
public class MistakeReviewView extends Parent {

	private Window _window;
	
	private final String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int BTN_HEIGHT = 70;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;

	private final HashMap<String, String> userAttempts;

	/**
	 * Create new mistakes view
	 * @param window
	 * @param attempts Map of word attempt to actual word spelling
	 * @param correctWords Number of words the user got correct
	 * @param wordListLength Length of wordlist the user is playing
	 * @param listName Name of wordlist the user is playing
	 */
	public MistakeReviewView(Window window, HashMap<String, String> attempts, int correctWords, int wordListLength, String listName) {
		this._window = window;
		
		for (int i = 0; i < attempts.keySet().size(); i++) {
			String key = attempts.keySet().toArray()[i].toString();
			
			// Remove any correct word spellings, we only need to show incorrect spellings
			if (key.toLowerCase().equals(attempts.get(key).toLowerCase())) {
				attempts.remove(key);
				i--;
			}
		}
		
		this.userAttempts = attempts;
		
		VBox root = new VBox();
		
		Button returnToMenuBtn;
		returnToMenuBtn = new Button("Return");
		returnToMenuBtn.setPrefWidth(_window.GetWidth());
		returnToMenuBtn.setPrefHeight(BTN_HEIGHT);
		returnToMenuBtn.setAlignment(Pos.CENTER);
		returnToMenuBtn.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		
		final int mastered = correctWords;
		final int size = wordListLength;
		final String levelName = listName;
		
		returnToMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// Return back to results screen
				_window.SetWindowScene(new Scene(new QuizResultsView(_window, mastered, size, levelName, userAttempts, true), _window.GetWidth(), _window.GetHeight()));
			}
			
		});
		
		// Create table to view words
		TableColumn<Map.Entry<String, String>, String> wordsCol = new TableColumn<Map.Entry<String, String>, String>("Correct Spelling");
		TableColumn<Map.Entry<String, String>, String> attemptsCol = new TableColumn<Map.Entry<String, String>, String>("Your Spelling");

		wordsCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
				return new SimpleStringProperty(p.getValue().getKey());
			}
		});
		
		attemptsCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
				return new SimpleStringProperty(p.getValue().getValue());
			}
		});
		
		//Setting the data for the table
		ObservableList<Map.Entry<String, String>> data = FXCollections.observableArrayList(attempts.entrySet());
		final TableView<Map.Entry<String,String>> resultsTable = new TableView<>(data);

		resultsTable.getColumns().addAll(wordsCol, attemptsCol);
		resultsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		resultsTable.setStyle("-fx-base: " + BACK_COLOR);
		resultsTable.setMinHeight(_window.GetHeight());
		resultsTable.setMinWidth(_window.GetWidth());
		
		root.setMinHeight(_window.GetHeight());
		root.setMinWidth(_window.GetWidth());
		root.getChildren().addAll(returnToMenuBtn, resultsTable);
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");

		this.getChildren().add(root);
		
	}

}