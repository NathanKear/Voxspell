package VoxspellPrototype.View;

import VoxspellPrototype.VoxspellPrototype;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Create pop-up window that displays simple text.
 * @author nathan
 *
 */
public class PopupWindow {
	private final static int TXT_FONT_SIZE = VoxspellPrototype.TXT_FONT_SIZE_FINE;
	private final static String TXT_FONT_COLOR = VoxspellPrototype.LIGHT_COLOR;
	private final static String BACK_COLOR = VoxspellPrototype.BACK_COLOR;
	private final static int POPWINDOW_WIDTH = 400;
	private final static int POPWINDOW_HEIGHT = 100;
	
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
}

