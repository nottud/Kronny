package editor.messagedisplay;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;

public class MessageDisplay {
	
	private Window parent;
	
	public MessageDisplay(Window parent) {
		this.parent = parent;
	}
	
	public void showError(Throwable throwable, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(parent);
		alert.setTitle("Skult has struck again!");
		alert.setHeaderText(throwable == null ? null : throwable.toString());
		alert.setContentText(content);
		alert.show();
	}

}
