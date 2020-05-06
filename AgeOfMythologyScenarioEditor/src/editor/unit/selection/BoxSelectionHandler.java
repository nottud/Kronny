
package editor.unit.selection;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import editor.EditorContext;
import editor.MainView;
import editor.unit.UnitBoundsFinder;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import mapmodel.unit.UnitModel;

public class BoxSelectionHandler {
   
   private EditorContext editorContext;
   private UnitBoundsFinder unitBoundsFinder;
   private BoxSelectionModel boxSelectionModel;
   private SelectedUnitsModel selectedUnitsModel;
   
   private EventHandler<MouseEvent> mousePressedHandler;
   private EventHandler<MouseEvent> mouseDraggedHandler;
   private EventHandler<MouseEvent> mouseReleasedHandler;
   
   private boolean isDragging;
   
   private StackPane stackPane;
   
   public BoxSelectionHandler(EditorContext editorContext, UnitBoundsFinder unitBoundsFinder, BoxSelectionModel boxSelectionModel,
         SelectedUnitsModel selectedUnitsModel) {
      this.editorContext = editorContext;
      this.unitBoundsFinder = unitBoundsFinder;
      this.boxSelectionModel = boxSelectionModel;
      this.selectedUnitsModel = selectedUnitsModel;
      MainView mainView = editorContext.getMainView();
      stackPane = mainView.getStackPane();
      
      mousePressedHandler = this::handleMousePressed;
      mouseDraggedHandler = this::handleMouseDragged;
      mouseReleasedHandler = this::handleMouseReleased;
      stackPane.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
      stackPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
      stackPane.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler);
   }
   
   private void handleMousePressed(MouseEvent mouseEvent) {
      if (MouseButton.PRIMARY.equals(mouseEvent.getButton())) {
         boxSelectionModel.setFromX(mouseEvent.getX());
         boxSelectionModel.setFromY(mouseEvent.getY());
         boxSelectionModel.setToX(mouseEvent.getX());
         boxSelectionModel.setToY(mouseEvent.getY());
         isDragging = true;
      }
   }
   
   private void handleMouseDragged(MouseEvent mouseEvent) {
      if (isDragging) {
         boxSelectionModel.setToX(mouseEvent.getX());
         boxSelectionModel.setToY(mouseEvent.getY());
         boxSelectionModel.setActive(true);
      }
   }
   
   private void handleMouseReleased(MouseEvent mouseEvent) {
      if (MouseButton.PRIMARY.equals(mouseEvent.getButton()) && isDragging) {
         isDragging = false;
         if (boxSelectionModel.isActive()) {
            boxSelectionModel.setActive(false);
            
            Bounds bounds = new BoundingBox(
                  Math.min(boxSelectionModel.getFromX(), boxSelectionModel.getToX()),
                  Math.min(boxSelectionModel.getFromY(), boxSelectionModel.getToY()),
                  Math.abs(boxSelectionModel.getToX() - boxSelectionModel.getFromX()),
                  Math.abs(boxSelectionModel.getToY() - boxSelectionModel.getFromY()));
            
            List<UnitModel> unitModels = editorContext.getRootModel().getAllUnitsModel().getUnitModels().getChildModels();
            Set<Integer> selection = new LinkedHashSet<>();
            for (int i = unitModels.size() - 1; i >= 0; i--) {
               UnitModel unitModel = unitModels.get(i);
               if (bounds.contains(unitBoundsFinder.screenBounds(unitModel))) {
                  selection.add(i);
               }
            }
            selectedUnitsModel.setSelectedUnits(selection);
         }
      }
   }
   
   public void destroy() {
      stackPane.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
      stackPane.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
      stackPane.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler);
   }
   
}
