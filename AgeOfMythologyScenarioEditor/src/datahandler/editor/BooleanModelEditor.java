
package datahandler.editor;

import command.CommandExecutor;
import datahandler.DataModelHolder;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import utility.observable.Observer;

public class BooleanModelEditor extends DataModelEditor<Boolean> {
   
   private CommandExecutor commandExecutor;
   private DataModelHolder<Boolean> dataModel;
   
   private ToggleButton toggleButton;
   
   private Observer<Boolean> observer;
   
   public BooleanModelEditor(CommandExecutor commandExecutor, DataModelHolder<Boolean> dataModel) {
      super(commandExecutor, dataModel);
      this.commandExecutor = commandExecutor;
      this.dataModel = dataModel;
      toggleButton = new ToggleButton();
      toggleButton.setOnAction(event -> handleButtonClicked());
      observer = dataModel.addValueObserverAndImmediatelyNotify(value -> updateFromModel());
   }
   
   private void handleButtonClicked() {
      notifyValueChanged(toggleButton.isSelected());
      commandExecutor.done();
   }
   
   @Override
   protected void updateValueInternal(Boolean newValue) {
      toggleButton.setSelected(newValue);
      if (newValue) {
         toggleButton.setText("True");
      } else {
         toggleButton.setText("False");
      }
   }
   
   @Override
   public Region getEditor() {
      return toggleButton;
   }
   
   @Override
   public void destroy() {
      dataModel.getObservableManager().removeObserver(dataModel.getValueChangedObserverType(), observer);
   }
   
}
