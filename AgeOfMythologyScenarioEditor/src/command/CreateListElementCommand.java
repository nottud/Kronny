package command;

import datahandler.path.Path;
import editor.EditorContext;
import mapmodel.ChildModel;
import mapmodel.ListModel;

public class CreateListElementCommand<T extends ChildModel> implements Command {
	
	private Path<ListModel<T>> path;
	private int index;

	public CreateListElementCommand(ListModel<T> listModel, int index) {
		this.index = index;
		path = new Path<>(listModel);
	}

	@Override
	public void run(EditorContext editorContext) {
		ListModel<T> listModel = path.resolve(editorContext.getRootModel());
		listModel.getChildModels().add(index, listModel.getChildFactory().apply(listModel));
		listModel.markDirty();
	}

	@Override
	public void undo(EditorContext editorContext) {
		ListModel<T> listModel = path.resolve(editorContext.getRootModel());
		listModel.getChildModels().remove(index);
		listModel.markDirty();
	}

}
