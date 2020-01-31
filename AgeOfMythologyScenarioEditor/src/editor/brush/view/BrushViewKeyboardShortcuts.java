package editor.brush.view;

import editor.MainView;
import editor.brush.BrushModel;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class BrushViewKeyboardShortcuts {
	
	private MainView mainView;
	private BrushModel brushModel;
	
	private EventHandler<KeyEvent> keyPressHandler;

	public BrushViewKeyboardShortcuts(MainView mainView, BrushModel brushModel) {
		this.mainView = mainView;
		this.brushModel = brushModel;
		
		keyPressHandler = this::handleKeyPress; 
		mainView.getStackPane().addEventFilter(KeyEvent.KEY_PRESSED, keyPressHandler);
	}
	
	private void handleKeyPress(KeyEvent keyEvent) {
		switch(keyEvent.getCode()) {
			case DIGIT1:
				setBrushSize(1);
				break;
			case DIGIT2: 
				setBrushSize(2);
				break;
			case DIGIT3:
				setBrushSize(3);
				break;
			case DIGIT4:
				setBrushSize(4);
				break;
			case DIGIT5: 
				setBrushSize(5);
				break;
			case DIGIT6:
				setBrushSize(6);
				break;
			case DIGIT7: 
				setBrushSize(7);
				break;
			case DIGIT8:
				setBrushSize(8);
				break;
			case DIGIT9: 
				setBrushSize(9);
				break;
			case DIGIT0: 
				setBrushSize(10);
				break;
			default:
				break;
		}
	}
	
	private void setBrushSize(int size) {
		brushModel.setWidth(size);
		brushModel.setHeight(size);
	}
	
	public void destroy() {
		mainView.getStackPane().removeEventFilter(KeyEvent.KEY_PRESSED, keyPressHandler);
	}

}
