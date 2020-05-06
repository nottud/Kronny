
package editor.unit.hover;

import camera.CameraConverter;
import editor.EditorContext;
import editor.MainView;
import editor.unit.UnitBoundsFinder;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import utility.observable.Observer;

public class UnitHoverShower {
   
   private EditorContext editorContext;
   private UnitBoundsFinder unitBoundsFinder;
   
   private EventHandler<MouseEvent> mouseHandler;
   private Observer<Void> cameraHandler;
   
   private double mouseScreenX;
   private double mouseScreenY;
   
   private StackPane stackPane;
   private CameraConverter cameraConverter;
   
   public UnitHoverShower(EditorContext editorContext, UnitBoundsFinder unitBoundsFinder) {
      this.editorContext = editorContext;
      this.unitBoundsFinder = unitBoundsFinder;
      MainView mainView = editorContext.getMainView();
      stackPane = mainView.getStackPane();
      
      mouseHandler = this::updateMouseMoved;
      stackPane.addEventFilter(MouseEvent.MOUSE_MOVED, mouseHandler);
      stackPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseHandler);
      
      cameraConverter = mainView.getCameraConverter();
      cameraHandler = value -> update();
      cameraConverter.getObservableManager().addObserver(CameraConverter.VIEW_CHANGED, cameraHandler);
   }
   
   private void updateMouseMoved(MouseEvent mouseEvent) {
      mouseScreenX = mouseEvent.getX();
      mouseScreenY = mouseEvent.getY();
      update();
   }
   
   private void update() {
      System.out.println(editorContext.getRootModel().getAllUnitsModel().getUnitModels().getChildModels().stream()
            .filter(unitModel -> unitBoundsFinder.screenBounds(unitModel).contains(mouseScreenX, mouseScreenY))
            .count());
   }
   
   public void destroy() {
      stackPane.removeEventFilter(MouseEvent.MOUSE_MOVED, mouseHandler);
      stackPane.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseHandler);
      
      cameraConverter.getObservableManager().removeObserver(CameraConverter.VIEW_CHANGED, cameraHandler);
   }
   
}
