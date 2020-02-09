
package datahandler.editor;

import command.CommandExecutor;
import command.SetValueCommand;
import datahandler.DataModel;
import javafx.scene.layout.Region;
import utility.graphics.BlockListener;

public abstract class DataModelEditor<T> {
   
   private CommandExecutor commandExecutor;
   private DataModel<T> dataModel;
   
   private BlockListener blockListener;
   
   public DataModelEditor(CommandExecutor commandExecutor, DataModel<T> dataModel) {
      this.commandExecutor = commandExecutor;
      this.dataModel = dataModel;
      
      blockListener = new BlockListener();
   }
   
   protected void updateFromModel() {
      updateValue(dataModel.getValue());
   }
   
   public void updateValue(T newValue) {
      blockListener.attemptBlockAndDo(() -> updateValueInternal(newValue));
   }
   
   protected abstract void updateValueInternal(T newValue);
   
   protected void notifyValueChanged(T value) {
      blockListener.attemptBlockAndDo(() -> commandExecutor.addPart(new SetValueCommand<>(dataModel, value)));
   }
   
   protected void doneEditing() {
      commandExecutor.done();
   }
   
   protected void notifyValueChangedAndDone(T value) {
      notifyValueChanged(value);
      doneEditing();
   }
   
   public abstract Region getEditor();
   
   public abstract void destroy();
   
}
