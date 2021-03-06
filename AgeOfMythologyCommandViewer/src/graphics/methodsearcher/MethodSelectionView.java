
package graphics.methodsearcher;

import graphics.SelectedMethodModel;
import javafx.scene.layout.StackPane;
import model.command.AomMethod;
import model.command.CommandModel;
import utility.graphics.filteredcombobox.FilteredComboBoxProperties;
import utility.graphics.filteredcombobox.SimpleRegexTextSearchPerformer;
import utility.graphics.filteredcombobox.javafx.FilteredComboBox;

public class MethodSelectionView {
   
   private SimpleRegexTextSearchPerformer<AomMethod> simpleRegexTextSearchPerformer;
   private FilteredComboBox<AomMethod> filteredComboBox;
   
   public MethodSelectionView(CommandModel commandModel, SelectedMethodModel selectedMethodModel) {
      simpleRegexTextSearchPerformer = new SimpleRegexTextSearchPerformer<>(commandModel::getMethods, this::getSearchAndDisplayText);
      simpleRegexTextSearchPerformer.enableAsynchronousJavaFx();
      filteredComboBox = new FilteredComboBox<>(simpleRegexTextSearchPerformer, new FilteredComboBoxProperties());
      filteredComboBox.getObservableManager().addObserver(filteredComboBox.getSelectedItemChangedObserverType(),
            selectedMethodModel::setSelectedMethod);
   }
   
   private String getSearchAndDisplayText(AomMethod method) {
      if (method == null) {
         return "";
      } else {
         return method.getName();
      }
   }
   
   public StackPane getNode() {
      return filteredComboBox.getNode();
   }
   
}
