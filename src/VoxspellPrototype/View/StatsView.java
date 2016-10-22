package VoxspellPrototype.View;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import VoxspellPrototype.VoxspellPrototype;
import VoxspellPrototype.Window;
import VoxspellPrototype.Model.LevelModel;
import VoxspellPrototype.Model.WordListModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class StatsView extends Parent {
	
	private Window _window;
	
	private final String BTN_RETURN_TEXT = "Return";
	private final String BTN_CLEAR_TEXT = "Clear All";
	private final String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final int BTN_HEIGHT = 70;
	private final String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE;

	private final Image MEDAL_GOLD = new Image(getClass().getResourceAsStream("/media/images/goldIcon.png"));
	private final Image MEDAL_SILVER = new Image(getClass().getResourceAsStream("/media/images/silverIcon.png"));
	private final Image MEDAL_BRONZE = new Image(getClass().getResourceAsStream("/media/images/bronzeIcon.png"));
	private final Image MEDAL_NONE = new Image(getClass().getResourceAsStream("/media/images/noMedalIcon.png"));
	
	public StatsView(Window window) {
		
		this._window = window;
		
		VBox root = new VBox();
		
		//Getting the WordList
		WordListModel wordlist = WordListModel.GetWordList();
		
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

		HBox medalRow = new HBox(10);
		buttonRow.setPrefWidth(_window.GetWidth());
		buttonRow.setPadding(new Insets(10, 10, 10, 10));
		
		final Button btnReturn;
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
				_window.SetWindowScene(new Scene(new MainView(_window), _window.GetWidth(), _window.GetHeight()));
			}
		});
		
		final Button btnClear;
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
				
				btnClear.setDisable(true);
				btnReturn.setDisable(true);
				
				PopupWindow.DeployConfirmationWindow("Are you sure you wan't to clear your stats?", 
						new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								WordListModel.GetWordList().ClearStats();
								_window.SetWindowScene(new Scene(new StatsView(_window), _window.GetWidth(), _window.GetHeight()));
								btnClear.setDisable(false);
								btnReturn.setDisable(false);
							}
					}, new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								btnClear.setDisable(false);
								btnReturn.setDisable(false);
							}	
				});
			}
		});

		int[] medalCount = WordListModel.GetWordList().GetMedalCount();

//		Text bronzeCount = new Text("x" + Integer.toString(medalCount[0]) + "           ");
//		Text silverCount = new Text("x" + Integer.toString(medalCount[1]) + "           ");
//		Text goldCount = new Text("x" + Integer.toString(medalCount[2]));
//
//		bronzeCount.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
//				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");
//		silverCount.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
//				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");
//		goldCount.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
//				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");
//
//		ImageView bronzeMedal = new ImageView(MEDAL_BRONZE);
//		ImageView silverMedal = new ImageView(MEDAL_SILVER);
//		ImageView goldMedal = new ImageView(MEDAL_GOLD);
//
//		bronzeMedal.setFitWidth(50); bronzeMedal.setFitHeight(50);
//		silverMedal.setFitWidth(50); silverMedal.setFitHeight(50);
//		goldMedal.setFitWidth(50); goldMedal.setFitHeight(50);
//
//		medalRow.setAlignment(Pos.BASELINE_CENTER);
//
//		medalRow.getChildren().addAll(bronzeMedal, bronzeCount, silverMedal, silverCount, goldMedal, goldCount);
//		
		buttonRow.getChildren().addAll(btnReturn, btnClear);
		
		root.getChildren().addAll(buttonRow,/* medalRow,*/ statsTabPane);
		
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
	private void populateStatsTable(final LevelModel level, Tab tab) {
	
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
		
		ImageView levelMedal;
		
		switch (level.GetMedal()) {
		case Bronze:
			levelMedal = new ImageView(MEDAL_BRONZE);
			break;
		case Gold:
			levelMedal = new ImageView(MEDAL_GOLD);
			break;
		case None:
			levelMedal = new ImageView(MEDAL_NONE);
			break;
		case Silver:
			levelMedal = new ImageView(MEDAL_SILVER);
			break;
		default:
			levelMedal = new ImageView(MEDAL_NONE);
			break;
		}
		
		levelMedal.setFitWidth(50);
		levelMedal.setFitHeight(50);
		
		String totalPercent = level.GetStatSuccessRateFormattedOverall();
		
		Text statsText = new Text(totalPercent);
		statsText.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + VoxspellPrototype.DARK_COLOR + ";");
		
		HBox hbox = new HBox(40);
		hbox.getChildren().addAll(levelMedal, statsText);
		hbox.setPrefWidth(_window.GetWidth());
		hbox.setAlignment(Pos.CENTER);
		
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(hbox, table);

		tab.setContent(vbox);

	}

}
