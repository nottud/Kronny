
package editor.unit.selection;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import editor.EditorContext;
import editor.MainView;
import editor.unit.UnitBoundsFinder;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import mapmodel.unit.UnitModel;

public class ClickSelectionHandler {
   
   private EditorContext editorContext;
   private UnitBoundsFinder unitBoundsFinder;
   private SelectedUnitsModel selectedUnitsModel;
   
   private EventHandler<MouseEvent> mouseHandler;
   
   private StackPane stackPane;
   
   public ClickSelectionHandler(EditorContext editorContext, UnitBoundsFinder unitBoundsFinder, SelectedUnitsModel selectedUnitsModel) {
      this.editorContext = editorContext;
      this.unitBoundsFinder = unitBoundsFinder;
      this.selectedUnitsModel = selectedUnitsModel;
      MainView mainView = editorContext.getMainView();
      stackPane = mainView.getStackPane();
      
      mouseHandler = this::handleClick;
      stackPane.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseHandler);
   }
   
   private void handleClick(MouseEvent mouseEvent) {
      if (!MouseButton.PRIMARY.equals(mouseEvent.getButton()) || !mouseEvent.isStillSincePress()) {
         return;
      }
      List<UnitModel> unitModels = editorContext.getRootModel().getAllUnitsModel().getUnitModels().getChildModels();
      for (int i = unitModels.size() - 1; i >= 0; i--) {
         UnitModel unitModel = unitModels.get(i);
         if (!unitBoundsFinder.screenBounds(unitModel).contains(mouseEvent.getX(), mouseEvent.getY())) {
            continue;
         }
         if (mouseEvent.isControlDown()) {
            Set<Integer> selectedUnits = new LinkedHashSet<>(selectedUnitsModel.getSelectedUnits());
            if (selectedUnits.contains(i)) {
               selectedUnits.remove(i);
            } else {
               selectedUnits.add(i);
            }
            selectedUnitsModel.setSelectedUnits(selectedUnits);
         } else {
            selectedUnitsModel.setSelectedUnit(i);
         }
         return;
      }
      selectedUnitsModel.setSelectedUnit(null);
   }
   
   public void destroy() {
      stackPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseHandler);
   }
   
}
