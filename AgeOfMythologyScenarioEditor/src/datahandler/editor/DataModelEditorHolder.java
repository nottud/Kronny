package datahandler.editor;

import java.util.ArrayList;
import java.util.Collection;

public class DataModelEditorHolder {
	
	private Collection<DataModelEditor<?>> dataModelEditors;
	
	public DataModelEditorHolder() {
		dataModelEditors = new ArrayList<>();
	}
	
	public <T> DataModelEditor<T> add(DataModelEditor<T> editor) {
		dataModelEditors.add(editor);
		return editor;
	}
	
	public void destroyAllEditors() {
		for(DataModelEditor<?> editor : dataModelEditors) {
			editor.destroy();
		}
		dataModelEditors.clear();
	}

}
