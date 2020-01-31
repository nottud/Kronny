
package editor.tool.covermap;

import command.CommandExecutor;
import command.RedrawTerrainCommand;
import command.SetValueCommand;
import datahandler.DataModel;
import editor.EditorContext;
import editor.terrainchooser.TerrainSelectionModel;
import editor.terrainchooser.TerrainViewer;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import javafx.application.Platform;
import javafx.scene.Node;
import mapmodel.map.MapSizeModel;
import terrain.TerrainEntry;

public class CoverMapTerrainTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private EditorContext editorContext;
   
   private TerrainSelectionModel terrainSelectionModel;
   private TerrainViewer terrainViewer;
   
   public CoverMapTerrainTool(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      terrainSelectionModel = new TerrainSelectionModel();
      terrainViewer = new TerrainViewer(terrainSelectionModel);
      
      terrainSelectionModel.getObservableManager().addObserver(TerrainSelectionModel.SELECTION_CHANGE, this::coverTerrain);
   }
   
   private void coverTerrain(TerrainEntry terrainEntry) {
      if (terrainEntry == null) {
         return;
      }
      Byte groupValue = (byte) terrainEntry.getGroupIndex();
      Byte indexValue = (byte) terrainEntry.getIndex();
      
      CommandExecutor commandExecutor = editorContext.getCommandExecutor();
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      for (DataModel<Byte> dataModel : mapSizeModel.getTerrainGroup().getChildModels()) {
         commandExecutor.addPart(new SetValueCommand<>(dataModel, groupValue));
      }
      for (DataModel<Byte> dataModel : mapSizeModel.getTerrainData().getChildModels()) {
         commandExecutor.addPart(new SetValueCommand<>(dataModel, indexValue));
      }
      commandExecutor.addPart(new RedrawTerrainCommand());
      commandExecutor.done();
      
      Platform.runLater(() -> terrainSelectionModel.setSelectedTerrainType(null));
   }
   
   @Override
   public Node getLeftGraphics() {
      return terrainViewer.getTableView();
   }
   
   @Override
   public void destroy() {
   }
   
}
