
package editor.tool.unit;

import camera.CameraConverter;
import editor.EditorContext;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import editor.unit.UnitBoundsFinder;
import editor.unit.UnitView;
import editor.unit.selection.BoxSelectionHandler;
import editor.unit.selection.BoxSelectionModel;
import editor.unit.selection.ClickSelectionHandler;
import editor.unit.selection.SelectedUnitsModel;
import editor.unit.selection.view.UnitSelectionView;
import editor.uniteditor.UnitEditor;
import javafx.scene.Node;

public class UnitTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private EditorContext editorContext;
   
   private UnitEditor unitEditor;
   private UnitView unitView;
   private UnitSelectionView unitSelectionView;
   
   private SelectedUnitsModel selectedUnitsModel;
   private BoxSelectionModel boxSelectionModel;
   
   private ClickSelectionHandler clickSelectionHandler;
   private BoxSelectionHandler boxSelectionHandler;
   
   public UnitTool(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      selectedUnitsModel = new SelectedUnitsModel();
      boxSelectionModel = new BoxSelectionModel();
      
      unitEditor = new UnitEditor(editorContext, selectedUnitsModel);
      
      CameraConverter cameraConverter = editorContext.getMainView().getCameraConverter();
      unitView = new UnitView(editorContext, cameraConverter);
      editorContext.getMainView().getStackPane().getChildren().add(unitView.getNode());
      
      UnitBoundsFinder unitBoundsFinder = new UnitBoundsFinder(cameraConverter);
      unitSelectionView = new UnitSelectionView(editorContext, cameraConverter, unitBoundsFinder, selectedUnitsModel, boxSelectionModel);
      editorContext.getMainView().getStackPane().getChildren().add(unitSelectionView.getNode());
      
      clickSelectionHandler = new ClickSelectionHandler(editorContext, unitBoundsFinder, selectedUnitsModel);
      boxSelectionHandler = new BoxSelectionHandler(editorContext, unitBoundsFinder, boxSelectionModel, selectedUnitsModel);
   }
   
   @Override
   public Node getLeftGraphics() {
      return unitEditor.getNode();
   }
   
   @Override
   public void destroy() {
      unitEditor.destroy();
      
      editorContext.getMainView().getStackPane().getChildren().remove(unitView.getNode());
      unitView.destroy();
      
      editorContext.getMainView().getStackPane().getChildren().remove(unitSelectionView.getNode());
      unitSelectionView.destroy();
      
      clickSelectionHandler.destroy();
      boxSelectionHandler.destroy();
   }
   
}
