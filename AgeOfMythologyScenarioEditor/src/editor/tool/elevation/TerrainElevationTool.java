
package editor.tool.elevation;

import editor.EditorContext;
import editor.tool.EditorToolType;

public class TerrainElevationTool extends BaseElevationTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   public TerrainElevationTool(EditorContext editorContext) {
      super(editorContext, editorContext.getRootModel().getMapSizeModel().getTerrainHeight().getChildModels());
   }
   
}
