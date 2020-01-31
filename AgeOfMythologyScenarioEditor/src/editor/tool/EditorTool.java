package editor.tool;

import javafx.scene.Node;

public interface EditorTool {
	
	public default Node getLeftGraphics() {
		return null;
	}
	
	public default Node getBottomGraphics() {
		return null;
	}

	public void destroy();
	
}
