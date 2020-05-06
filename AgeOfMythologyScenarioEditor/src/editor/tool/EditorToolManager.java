
package editor.tool;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import editor.EditorContext;
import editor.tool.covermap.CoverMapTerrainTool;
import editor.tool.elevation.TerrainElevationTool;
import editor.tool.elevation.WaterElevationTool;
import editor.tool.paintterrain.TerrainTool;
import editor.tool.paintwater.PaintWaterColourTool;
import editor.tool.paintwater.PaintWaterTool;
import editor.tool.player.PlayerTool;
import editor.tool.unit.UnitTool;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class EditorToolManager implements Observable {
   
   public static final ObserverType<EditorTool> TOOL_STARTED = new ObserverType<>();
   
   private EditorContext editorContext;
   
   private ObservableManager observableManager;
   
   private Map<EditorToolType, Function<EditorContext, EditorTool>> editorToolTypeToFactory;
   
   private EditorTool activeTool;
   
   public EditorToolManager(EditorContext editorContext) {
      this.editorContext = editorContext;
      observableManager = new ObservableManagerImpl();
      activeTool = new NullEditorTool();
      
      editorToolTypeToFactory = new LinkedHashMap<>();
      editorToolTypeToFactory.put(NullEditorTool.TOOL_TYPE, foundContext -> new NullEditorTool());
      editorToolTypeToFactory.put(PlayerTool.TOOL_TYPE, PlayerTool::new);
      editorToolTypeToFactory.put(CoverMapTerrainTool.TOOL_TYPE, CoverMapTerrainTool::new);
      editorToolTypeToFactory.put(TerrainTool.TOOL_TYPE, TerrainTool::new);
      editorToolTypeToFactory.put(PaintWaterColourTool.TOOL_TYPE, PaintWaterColourTool::new);
      editorToolTypeToFactory.put(PaintWaterTool.TOOL_TYPE, PaintWaterTool::new);
      editorToolTypeToFactory.put(TerrainElevationTool.TOOL_TYPE, TerrainElevationTool::new);
      editorToolTypeToFactory.put(WaterElevationTool.TOOL_TYPE, WaterElevationTool::new);
      editorToolTypeToFactory.put(UnitTool.TOOL_TYPE, UnitTool::new);
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
   public void startTool(EditorToolType editorToolType) {
      activeTool.destroy();
      
      activeTool = editorToolTypeToFactory.get(editorToolType).apply(editorContext);
      observableManager.notifyObservers(TOOL_STARTED, activeTool);
   }
   
   public void closeActiveTool() {
      startTool(NullEditorTool.TOOL_TYPE);
   }
   
   public EditorTool getActiveTool() {
      return activeTool;
   }
   
}
