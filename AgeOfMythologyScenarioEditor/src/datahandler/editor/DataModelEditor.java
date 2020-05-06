
package datahandler.editor;

import command.CommandExecutor;
import command.SetValueCommand;
import datahandler.DataModel;
import datahandler.DataModelHolder;
import javafx.scene.layout.Region;
import utility.graphics.BlockListener;

public abstract class DataModelEditor<T> {
   
   private CommandExecutor commandExecutor;
   private DataModelHolder<T> dataModelHolder;
   
   private BlockListener blockListener;
   
   public DataModelEditor(CommandExecutor commandExecutor, DataModelHolder<T> dataModelHolder) {
      this.commandExecutor = commandExecutor;
      this.dataModelHolder = dataModelHolder;
      
      blockListener = new BlockListener();
   }
   
   protected void updateFromModel() {
      updateValue(dataModelHolder.getValue());
   }
   
   public void updateValue(T newValue) {
      blockListener.attemptBlockAndDo(() -> updateValueInternal(newValue));
   }
   
   protected abstract void updateValueInternal(T newValue);
   
   protected void notifyValueChanged(T value) {
      blockListener.attemptBlockAndDo(() -> {
         for (DataModel<T> dataModel : dataModelHolder.getDataModels()) {
            commandExecutor.addPart(new SetValueCommand<>(dataModel, value));
         }
      });
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
