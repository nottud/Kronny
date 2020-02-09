
package datahandler.editor;

import command.CommandExecutor;
import datahandler.DataModel;
import javafx.scene.layout.Region;
import utility.graphics.validationinput.ValidationInputParserFloat;
import utility.graphics.validationinput.ValidationInputTextField;
import utility.observable.Observer;

public class FloatModelEditor extends DataModelEditor<Float> {
   
   private DataModel<Float> dataModel;
   
   private ValidationInputTextField<Float> editor;
   
   private Observer<Float> observer;
   
   public FloatModelEditor(CommandExecutor commandExecutor, DataModel<Float> dataModel) {
      super(commandExecutor, dataModel);
      this.dataModel = dataModel;
      editor = new ValidationInputTextField<>(0F, new ValidationInputParserFloat());
      editor.getObservableManager().addObserver(editor.getValueChangedObserverType(), this::notifyValueChanged);
      editor.getNode().focusedProperty().addListener((source, oldValue, newValue) -> doneEditing());
      observer = dataModel.addValueObserverAndImmediatelyNotify(value -> updateFromModel());
   }
   
   @Override
   protected void updateValueInternal(Float newValue) {
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
