package VoxspellPrototype;

import java.awt.Rectangle;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX window wrapper
 * @author nathan
 *
 */
public class Window {
	
	private int _width, _height;
	private Stage _window;
	
	public int GetWidth() {
		return _width;
	}
	
	public int GetHeight() {
		return _height;
	}
	
	/**
	 * X position of window relative to monitor
	 * @return
	 */
	public int GetPosX() {
		return (int)this._window.getX();
	}
	
	/**
	 * Y position of window relative to monitor
	 * @return
	 */
	public int GetPosY() {
		return (int)this._window.getY();
	}
	
	/**
	 * Get underlying stage object
	 * @return
	 */
	public Stage GetWindowStage() {
		return _window;
	}
	
	public Window(Stage window, int width, int height) {
		this._window = window;

		this._height = height;
		this._width = width;
	}
	
	/**
	 * Change displayed content to scene
	 * @param scene
	 */
	public void SetWindowScene(Scene scene) {
		_window.setScene(scene);
	}
	
	/**
	 * Set window position relative to the monitor
	 * @param x
	 * @param y
	 */
	public void SetWindowPosition(int x, int y) {
		_window.setX(x);
		_window.setY(y);
	}
	
	public void SetWindowTitle(String title) {
		_window.setTitle(title);
	}
	
	/**
	 * Display or hide the window
	 * @param show
	 */
	public void Show(boolean show) {
		if (show) {
			_window.show();
		} else {
			_window.hide();
		}
	}
	
	/**
	 * Position monitor so it lies on the center of the screen
	 */
	public void CenterOnScreen() {
		_window.centerOnScreen();
	}
	
	/**
	 * Get window bounds relative to monitor
	 * @return
	 */
	public Rectangle GetBounds() {
		return new Rectangle(
				(int)this._window.getX(),
				(int)this._window.getY(),
				(int)this._window.getWidth(),
				(int)this._window.getHeight());
	}
}
