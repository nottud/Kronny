
package datahandler.editor;

import command.CommandExecutor;
import datahandler.DataModelHolder;
import javafx.scene.layout.Region;
import utility.graphics.validationinput.ValidationInputParserInteger;
import utility.graphics.validationinput.ValidationInputTextField;
import utility.observable.Observer;

public class IntegerModelEditor extends DataModelEditor<Integer> {
   
   private DataModelHolder<Integer> dataModel;
   
   private ValidationInputTextField<Integer> editor;
   
   private Observer<Integer> observer;
   
   public IntegerModelEditor(CommandExecutor commandExecutor, DataModelHolder<Integer> dataModel) {
      super(commandExecutor, dataModel);
      this.dataModel = dataModel;
      editor = new ValidationInputTextField<>(0, new ValidationInputParserInteger());
      editor.getObservableManager().addObserver(editor.getValueChangedObserverType(), this::notifyValueChanged);
      editor.getNode().focusedProperty().addListener((source, oldValue, newValue) -> doneEditing());
      observer = dataModel.addValueObserverAndImmediatelyNotify(value -> updateFromModel());
   }
   
   @Override
   protected void updateValueInternal(Integer newValue) {
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
