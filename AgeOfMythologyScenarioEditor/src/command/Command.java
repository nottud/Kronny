package command;

import editor.EditorContext;

public interface Command {
	
	public void run(EditorContext editorContext);
	
	public void undo(EditorContext editorContext);

}
