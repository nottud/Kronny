
package datahandler.editor;

import command.CommandExecutor;
import datahandler.DataModelHolder;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Region;
import utility.observable.Observer;

public class NamedCheckboxBooleanModelEditor extends DataModelEditor<Boolean> {
   
   private CommandExecutor commandExecutor;
   private DataModelHolder<Boolean> dataModel;
   
   private CheckBox checkBox;
   
   private Observer<Boolean> observer;
   
   public NamedCheckboxBooleanModelEditor(CommandExecutor commandExecutor, DataModelHolder<Boolean> dataModel, String displayName) {
      super(commandExecutor, dataModel);
      this.commandExecutor = commandExecutor;
      this.dataModel = dataModel;
      checkBox = new CheckBox(displayName);
      checkBox.setOnAction(event -> handleButtonClicked());
      observer = dataModel.addValueObserverAndImmediatelyNotify(value -> updateFromModel());
   }
   
   private void handleButtonClicked() {
      notifyValueChanged(checkBox.isSelected());
      commandExecutor.done();
   }
   
   @Override
   protected void updateValueInternal(Boolean newValue) {
      checkBox.setSelected(newValue);
   }
   
   @Override
   public Region getEditor() {
      return checkBox;
   }
   
   @Override
   public void destroy() {
      dataModel.getObservableManager().removeObserver(dataModel.getValueChangedObserverType(), observer);
   }
   
}
