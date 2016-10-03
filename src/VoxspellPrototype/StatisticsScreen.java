package VoxspellPrototype;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class StatisticsScreen extends Parent {
	
	private Window _window;
	
	private final String BTN_RETURN_TEXT = "Return";
	private final String BTN_CLEAR_TEXT = "Clear";
	private final String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int BTN_HEIGHT = 70;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	
	public StatisticsScreen(Window window) {
		
		this._window = window;
		
		VBox root = new VBox();
		
		//Getting the WordList
		WordList wordlist = WordList.GetWordList();
		
		//Getting the number of tabs to create
		int numOfTabs = wordlist.size();
		
		//This will be the name of each tab
		String tabName;

		BorderPane bp = new BorderPane();
				
		//Creating the pane to store the tabs
		TabPane statsTabPane = new TabPane();

		bp.setCenter(statsTabPane);
		
		int tabWidth = _window.GetWidth()/(numOfTabs + 3);
		statsTabPane.setTabMinWidth(tabWidth);
		
		statsTabPane.setStyle("-fx-base: " + BACK_COLOR);
		
		//Looping through the number of levels and creating a tab for each one
		for(int i = 0; i < numOfTabs; i++) {
			tabName = wordlist.get(i).levelName();
			Tab t = new Tab(tabName);
			t.setClosable(false);
			statsTabPane.getTabs().add(t);

			populateStatsTable(wordlist.get(i), t);
		}

		statsTabPane.setMinHeight(_window.GetHeight());
		statsTabPane.setMinWidth(_window.GetWidth());
		
		HBox buttonRow = new HBox(10);
		buttonRow.setPrefWidth(_window.GetWidth());
		buttonRow.setPadding(new Insets(10, 10, 10, 10));
		
		Button btnReturn;
		btnReturn = new Button(BTN_RETURN_TEXT);
		btnReturn.setPrefWidth((_window.GetWidth() - 30) / 2);
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
		
		Button btnClear;
		btnClear = new Button(BTN_CLEAR_TEXT);
		btnClear.setPrefWidth((_window.GetWidth() - 30) / 2);
		btnClear.setPrefHeight(BTN_HEIGHT);
		btnClear.setAlignment(Pos.CENTER);
		btnClear.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");	
		btnClear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				WordList.GetWordList().ClearStats();
				_window.SetWindowScene(new Scene(new StatisticsScreen(_window), _window.GetWidth(), _window.GetHeight()));
			}
		});
		
		buttonRow.getChildren().addAll(btnReturn, btnClear);
		
		root.getChildren().addAll(buttonRow, statsTabPane);
		
		//Adding the statspane
		this.getChildren().add(root);
		
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
		this.setStyle("-fx-background-color: " + BACK_COLOR + ";");
		return;
	}

	@SuppressWarnings("unchecked")
	/**
	 * This method populates the stats table for each level
	 * 
	 * @param level - the Level containing the stats for its table
	 * @param tab - the tab where the stats table is held
	 */
	private void populateStatsTable(final Level level, Tab tab) {
	
		//Getting the levels words and stats for each word
		HashMap<String, List<Character>> levelHashMap = level.getMap();

		//Creating the cell value factory for the words
		TableColumn<Map.Entry<String, List<Character>>, String> statsWordCol = new TableColumn<>("Word");
		statsWordCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, List<Character>>, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, List<Character>>, String> p) {
				return new SimpleStringProperty(p.getValue().getKey());
			}
		});
		
		//Creating the cell value factory for the failed stats
		TableColumn<Map.Entry<String, List<Character>>, String> totalAttempts = new TableColumn<>("Correct");
		totalAttempts.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, List<Character>>, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, List<Character>>, String> p) {
				// this callback returns property for just one cell, you can't use a loop here
				// for first column we use key
				return new SimpleStringProperty(level.GetStatSuccessRateFormatted(p.getValue().getKey()));
			}
		});

		//Creating the cell value factory for the faulted stats
		TableColumn<Map.Entry<String, List<Character>>, String> successRate = new TableColumn<>("Total Attempts");
		successRate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, List<Character>>, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, List<Character>>, String> p) {
				// this callback returns property for just one cell, you can't use a loop here
				// for first column we use key				
				return new SimpleStringProperty(Integer.toString(level.GetAttemptCount(p.getValue().getKey())));
			}
		});
		
		//Setting the data for the table
		ObservableList<Map.Entry<String, List<Character>>> items = FXCollections.observableArrayList(levelHashMap.entrySet());
		final TableView<Map.Entry<String, List<Character>>> table = new TableView<>(items);
		
		table.getColumns().setAll(statsWordCol, totalAttempts, successRate);

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		tab.setContent(table);

	}

}
