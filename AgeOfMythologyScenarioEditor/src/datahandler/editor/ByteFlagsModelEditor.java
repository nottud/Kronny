
package datahandler.editor;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import command.CommandExecutor;
import datahandler.DataModelHolder;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utility.observable.Observer;

public class ByteFlagsModelEditor extends DataModelEditor<Byte> {
   
   private CommandExecutor commandExecutor;
   private DataModelHolder<Byte> dataModel;
   
   private VBox holder;
   private List<CheckBox> checkBoxes;
   
   private Observer<Byte> observer;
   
   public ByteFlagsModelEditor(CommandExecutor commandExecutor, DataModelHolder<Byte> dataModel, List<String> fields) {
      super(commandExecutor, dataModel);
      this.commandExecutor = commandExecutor;
      this.dataModel = dataModel;
      checkBoxes = new ArrayList<>(fields.size());
      for (String field : fields) {
         CheckBox checkBox = new CheckBox(field);
         checkBox.setOnAction(event -> handleButtonClicked());
         checkBoxes.add(checkBox);
      }
      holder = new VBox();
      holder.getChildren().setAll(checkBoxes);
      observer = dataModel.addValueObserverAndImmediatelyNotify(value -> updateFromModel());
   }
   
   private void handleButtonClicked() {
      BitSet bitSet = new BitSet(checkBoxes.size());
      for (int i = 0; i < checkBoxes.size(); i++) {
         bitSet.set(i, checkBoxes.get(i).isSelected());
      }
      byte newValue = bitSet.toByteArray()[0];
      notifyValueChanged(newValue);
      commandExecutor.done();
   }
   
   @Override
   protected void updateValueInternal(Byte newValue) {
      BitSet bitSet = BitSet.valueOf(new byte[] {newValue});
      for (int i = 0; i < checkBoxes.size(); i++) {
         checkBoxes.get(i).setSelected(bitSet.get(i));
      }
   }
   
   @Override
   public Region getEditor() {
      return holder;
   }
   
   @Override
   public void destroy() {
      dataModel.getObservableManager().removeObserver(dataModel.getValueChangedObserverType(), observer);
   }
   
}
