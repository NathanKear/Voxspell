package VoxspellPrototype.View;

import VoxspellPrototype.VoxspellPrototype;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Create pop-up window that displays simple text.
 * @author nathan kear & charles carey
 *
 */
public class PopupWindow {
	private final static int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE_FINE;
	private final static String TXT_FONT_COLOR = VoxspellPrototype.DARK_COLOR;
	private final static String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final static int BTN_FONT_SIZE = VoxspellPrototype.BTN_FONT_SIZE;
	private final static String BTN_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final static String BTN_COLOR = VoxspellPrototype.BUTTON_COLOR;
	private final static int POPWINDOW_WIDTH = 400;
	private final static int POPWINDOW_HEIGHT = 150;
	
	/**
	 * Deploy single pop-up window.
	 * @param message Message to display.
	 * @return Stage that is displayed.
	 */
	public static Stage DeployPopupWindow(String message) {
		Stage popupStage = new Stage();
		
		VBox root = new VBox();
		
		// Build text to display.
		Text popupText = new Text(message);
		popupText.setTextAlignment(TextAlignment.CENTER);
		popupText.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		popupText.setWrappingWidth(POPWINDOW_WIDTH);
		root.setAlignment(Pos.CENTER);
		root.getChildren().add(popupText);
		Scene popupScene = new Scene(root, POPWINDOW_WIDTH, POPWINDOW_HEIGHT);
		
		// Format window.
		popupStage.setScene(popupScene);
		popupStage.show();
		popupStage.requestFocus();
		popupStage.toFront();
		
		// Set root node color
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
		
		return popupStage;
	}
	
	/**
	 * Deploy single pop-up window.
	 * @param message Message to display.
	 * @return Stage that is displayed.
	 */
	public static Stage DeployConfirmationWindow(String message, EventHandler<ActionEvent> confirmHandler, EventHandler<ActionEvent> cancelHandler) {
		final Stage popupStage = new Stage();
		
		VBox root = new VBox();
		
		HBox buttons = new HBox(20);
		buttons.setPadding(new Insets(20, 20, 20, 20));
		
		Button btnConfirm = new Button("Confirm");
		Button btnCancel = new Button("Cancel");
		
		btnConfirm.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		
		btnCancel.setStyle("-fx-font: " + BTN_FONT_SIZE + " arial;" + 
				" -fx-base: " + BTN_COLOR + ";" + 
				" -fx-text-fill: " + BTN_FONT_COLOR + ";");
		
		btnConfirm.setPrefWidth(180);
		btnConfirm.setPrefHeight(40);
		btnCancel.setPrefWidth(180);
		btnCancel.setPrefHeight(40);
		
		btnConfirm.addEventHandler(ActionEvent.ACTION, confirmHandler);
		btnCancel.addEventHandler(ActionEvent.ACTION, cancelHandler);
		btnConfirm.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				popupStage.close();
			}			
		});
		btnCancel.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				popupStage.close();
			}			
		});
		
		buttons.getChildren().addAll(btnCancel, btnConfirm);
		
		// Build text to display.
		Text popupText = new Text(message);
		popupText.setTextAlignment(TextAlignment.CENTER);
		popupText.setStyle("-fx-font: " + TXT_FONT_SIZE + " arial;" +
				" -fx-fill: " + TXT_FONT_COLOR + ";");
		popupText.setWrappingWidth(POPWINDOW_WIDTH);
		root.setAlignment(Pos.CENTER);
		root.getChildren().addAll(popupText, buttons);
		Scene popupScene = new Scene(root, POPWINDOW_WIDTH, POPWINDOW_HEIGHT);
		
		// Format window.
		popupStage.setScene(popupScene);
		popupStage.show();
		popupStage.requestFocus();
		popupStage.toFront();
		
		// Set root node color
		root.setStyle("-fx-background-color: " + BACK_COLOR + ";");
		
		return popupStage;
	}
}

