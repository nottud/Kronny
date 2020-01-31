
package editor;

import camera.CameraConverter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class MainView {
   
   private CameraConverter cameraConverter;
   private StackPane stackPane;
   
   public MainView() {
      cameraConverter = new CameraConverter();
      stackPane = new StackPane();
      stackPane.setFocusTraversable(true);
      stackPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> stackPane.requestFocus());
   }
   
   public CameraConverter getCameraConverter() {
      return cameraConverter;
   }
   
   public StackPane getStackPane() {
      return stackPane;
   }
   
}
