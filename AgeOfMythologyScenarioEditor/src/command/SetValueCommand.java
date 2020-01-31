package command;

import datahandler.DataModel;
import datahandler.path.Path;
import editor.EditorContext;

public class SetValueCommand<T> implements Command {
	
	private T newValue;
	private Path<DataModel<T>> path;
	
	private T oldValue;
	
	public SetValueCommand(DataModel<T> dataModel, T value) {
		this.newValue = value;
		path = new Path<>(dataModel);
	}

	@Override
	public void run(EditorContext editorContext) {
		DataModel<T> dataModel = path.resolve(editorContext.getRootModel());
		oldValue = dataModel.getValue();
		dataModel.setValue(newValue);
	}

	@Override
	public void undo(EditorContext editorContext) {
		DataModel<T> dataModel = path.resolve(editorContext.getRootModel());
		dataModel.setValue(oldValue);
	}
}
