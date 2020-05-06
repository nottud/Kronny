
package datahandler.editor;

import java.util.function.Function;
import java.util.function.Supplier;

import command.CommandExecutor;
import datahandler.DataModelHolder;
import javafx.scene.layout.Region;
import utility.graphics.filteredcombobox.FilteredComboBoxProperties;
import utility.graphics.filteredcombobox.SearchPerformer;
import utility.graphics.filteredcombobox.SimpleRegexTextSearchPerformer;
import utility.graphics.filteredcombobox.javafx.FilteredComboBox;
import utility.observable.Observer;

public class FilteredComboBoxModelEditor<T> extends DataModelEditor<T> {
   
   private DataModelHolder<T> dataModel;
   
   private FilteredComboBox<T> editor;
   
   private Observer<T> observer;
   
   public FilteredComboBoxModelEditor(CommandExecutor commandExecutor, DataModelHolder<T> dataModel,
         Supplier<? extends Iterable<T>> possibleItemsSupplier,
         Function<T, String> itemToSearchText) {
      super(commandExecutor, dataModel);
      this.dataModel = dataModel;
      FilteredComboBoxProperties properties = new FilteredComboBoxProperties();
      SearchPerformer<T> searchPerformer =
            new SimpleRegexTextSearchPerformer<>(possibleItemsSupplier, value -> value == null ? "" : itemToSearchText.apply(value));
      editor = new FilteredComboBox<>(searchPerformer, properties);
      editor.getObservableManager().addObserver(editor.getSelectedItemChangedObserverType(), this::notifyValueChangedAndDone);
      observer = dataModel.addValueObserverAndImmediatelyNotify(value -> updateFromModel());
   }
   
   @Override
   protected void updateValueInternal(T newValue) {
      editor.setSelectedItem(newValue);
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
