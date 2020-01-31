package command;

import editor.EditorContext;

public class RedrawTerrainCommand implements Command {

	@Override
	public void run(EditorContext editorContext) {
		editorContext.getRedrawTerrainHander().requestRedraw();
	}

	@Override
	public void undo(EditorContext editorContext) {
		editorContext.getRedrawTerrainHander().requestRedraw();
	}

}
