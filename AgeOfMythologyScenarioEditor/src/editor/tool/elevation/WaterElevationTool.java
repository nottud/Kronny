package editor.tool.elevation;

import editor.EditorContext;
import editor.tool.EditorToolType;

public class WaterElevationTool extends BaseElevationTool {
	
	public static final EditorToolType TOOL_TYPE = new EditorToolType();
	
	public WaterElevationTool(EditorContext editorContext) {
		super(editorContext, editorContext.getRootModel().getMapSizeModel().getWaterHeight().getChildModels());
	}

}
