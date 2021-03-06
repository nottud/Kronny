
package utility.graphics.filteredcombobox.javafx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import utility.graphics.filteredcombobox.FilteredComboBoxItem;
import utility.graphics.filteredcombobox.FilteredComboBoxModel;
import utility.graphics.filteredcombobox.FilteredComboBoxProperties;
import utility.graphics.filteredcombobox.FilteredComboBoxRequirements;
import utility.graphics.filteredcombobox.SearchPerformer;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

/**
 * Provides a versatile way for the user to select an object by typing and filtering. Optionally the user can request all possible items to be
 * returned in a form easy to manually search and select (e.g. in alphabetical order whereas searching would give results in order of closest match).
 * Request all is optional because in some cases the number of possible items is extremely large and it is not practical to display all without
 * filtering.
 * @param <T> The type of the items the user can search for.
 */
public class FilteredComboBox<T> implements FilteredComboBoxRequirements<T> {
   
   private SearchPerformer<T> searchPerformer;
   private FilteredComboBoxProperties properties;
   
   private ObserverType<T> selectedItemChangedObserverType;
   private ObserverType<T> actionPerformedObserverType;
   private ObservableManager observableManager;
   
   private StackPane stackPane;
   
   private FilteredComboBoxModel<T> model;
   private FilteredComboBoxInput<T> input;
   private FilteredComboBoxPossibleItemsDisplayer<T> itemsDisplayer;
   private FilteredComboBoxAllButton allButton;
   
   /**
    * Constructor building the {@link FilteredComboBox}.
    * @param searchPerformer Performs the {@link SearchPerformer}.
    * @param properties Holds configuration for the {@link FilteredComboBox}.
    */
   public FilteredComboBox(SearchPerformer<T> searchPerformer, FilteredComboBoxProperties properties) {
      this.searchPerformer = searchPerformer;
      this.properties = properties;
      
      selectedItemChangedObserverType = new ObserverType<>();
      actionPerformedObserverType = new ObserverType<>();
      observableManager = new ObservableManagerImpl();
      
      stackPane = new StackPane();
      stackPane.setAlignment(Pos.CENTER_LEFT);
      
      model = new FilteredComboBoxModel<>(searchPerformer, properties, Platform::runLater);
      model.getObservableManager().addObserver(
            model.getItemChosenObserverType(), selectedItem -> observableManager.notifyObservers(selectedItemChangedObserverType, selectedItem));
      
      itemsDisplayer = new FilteredComboBoxPossibleItemsDisplayer<>(model, searchPerformer, properties);
      itemsDisplayer.getNode().setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
      itemsDisplayer.getNode().setFocusTraversable(false);
      stackPane.getChildren().add(new BorderPane(itemsDisplayer.getNode()));
      
      input = new FilteredComboBoxInput<>(model, searchPerformer, properties);
      input.getNode().setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
      input.getNode().addEventFilter(ActionEvent.ACTION,
            actionEvent -> observableManager.notifyObservers(actionPerformedObserverType, model.getSelectedItem()));
      BorderPane topBorderPane = new BorderPane(input.getNode());
      if (properties.isAllIncluded()) {
         FilteredComboBoxPossibleItemsDisplayerDelayedState delayedState = new FilteredComboBoxPossibleItemsDisplayerDelayedState(
               itemsDisplayer);
         allButton = new FilteredComboBoxAllButton(model, delayedState);
         allButton.getNode().setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
         allButton.getNode().addEventFilter(ActionEvent.ACTION, actionEvent -> input.getNode().requestFocus());
         topBorderPane.setRight(allButton.getNode());
      }
      stackPane.getChildren().add(topBorderPane);
   }
   
   @Override
   public void setSelectedItem(T item) {
      model.itemChosen(item);
   }
   
   @Override
   public T getSelectedItem() {
      return model.getSelectedItem();
   }
   
   @Override
   public ObserverType<T> getSelectedItemChangedObserverType() {
      return selectedItemChangedObserverType;
   }
   
   @Override
   public ObserverType<T> getInputActionPerformedObserverType() {
      return actionPerformedObserverType;
   }
   
   public StackPane getNode() {
      return stackPane;
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
   /**
    * Set cell factory to render the items.
    * @param cellFactory Creates cell to show the items
    */
   public void setCellFactory(Callback<ListView<FilteredComboBoxItem<T>>, ListCell<FilteredComboBoxItem<T>>> cellFactory) {
      itemsDisplayer.setCellFactory(cellFactory);
   }
   
   @Override
   public void requestFocusOnInput() {
      input.requestFocusOnInput();
   }
   
   @Override
   public boolean isInputFocused() {
      return input.isInputFocused();
   }
   
   FilteredComboBoxPossibleItemsDisplayer<T> getItemsDisplayer() {
      return itemsDisplayer;
   }
   
   FilteredComboBoxInput<T> getInput() {
      return input;
   }
   
   FilteredComboBoxAllButton getAllButton() {
      return allButton;
   }
   
   @Override
   public SearchPerformer<T> getSearchPerformer() {
      return searchPerformer;
   }
   
   @Override
   public FilteredComboBoxProperties getProperties() {
      return properties;
   }
   
   /**
    * Sets the height of the dropdown to display the items in.
    * @param height New height to display at.
    */
   @Override
   public void setDropdownHeight(double height) {
      itemsDisplayer.setDropdownHeight(height);
   }
   
   @Override
   public double getDropdownHeight() {
      return itemsDisplayer.getDropdownHeight();
   }
}
