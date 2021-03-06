
package graphics.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;

public class MessageDisplay {
   
   public void showError(Window parent, Throwable throwable, String content) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.initOwner(parent);
      alert.setTitle("Danzhu has struck again!");
      alert.setHeaderText(throwable == null ? null : throwable.toString());
      alert.setContentText(content);
      alert.show();
   }
   
}
