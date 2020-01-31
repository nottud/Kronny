package command;

import datahandler.path.Path;
import editor.EditorContext;
import mapmodel.ChildModel;
import mapmodel.ListModel;

public class RemoveListElementCommand<T extends ChildModel> implements Command {
	
	private Path<ListModel<T>> path;
	private int index;
	
	private T removedItem;

	public RemoveListElementCommand(ListModel<T> listModel, int index) {
		this.index = index;
		path = new Path<>(listModel);
	}

	@Override
	public void run(EditorContext editorContext) {
		ListModel<T> listModel = path.resolve(editorContext.getRootModel());
		removedItem = listModel.getChildModels().get(index);
		listModel.getChildModels().remove(index);
		listModel.markDirty();
	}

	@Override
	public void undo(EditorContext editorContext) {
		ListModel<T> listModel = path.resolve(editorContext.getRootModel());
		listModel.getChildModels().add(index, removedItem);
		listModel.markDirty();
	}

}
