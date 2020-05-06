
package datahandler.editor;

import command.CommandExecutor;
import datahandler.DataModelHolder;
import javafx.scene.layout.Region;
import utility.graphics.validationinput.ValidationInputParserString;
import utility.graphics.validationinput.ValidationInputTextField;
import utility.observable.Observer;

public class StringModelEditor extends DataModelEditor<String> {
   
   private DataModelHolder<String> dataModel;
   
   private ValidationInputTextField<String> editor;
   
   private Observer<String> observer;
   
   public StringModelEditor(CommandExecutor commandExecutor, DataModelHolder<String> dataModel) {
      super(commandExecutor, dataModel);
      this.dataModel = dataModel;
      editor = new ValidationInputTextField<>("", new ValidationInputParserString());
      editor.getObservableManager().addObserver(editor.getValueChangedObserverType(), this::notifyValueChanged);
      editor.getNode().focusedProperty().addListener((source, oldValue, newValue) -> doneEditing());
      observer = dataModel.addValueObserverAndImmediatelyNotify(value -> updateFromModel());
   }
   
   @Override
   protected void updateValueInternal(String newValue) {
      editor.setValue(newValue);
   }
   
   @Override
   public Region getEditor() {
      return editor.getNode();
   }
   
   @Override
   public void destroy() {
      dataModel.getObservableManager().removeObserver(dataModel.getValueChangedObserverType(), observer);
   }
   
}
