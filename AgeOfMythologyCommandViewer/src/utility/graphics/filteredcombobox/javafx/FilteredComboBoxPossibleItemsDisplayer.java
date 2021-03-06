
package utility.graphics.filteredcombobox.javafx;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import utility.graphics.filteredcombobox.FilteredComboBoxItem;
import utility.graphics.filteredcombobox.FilteredComboBoxModel;
import utility.graphics.filteredcombobox.FilteredComboBoxProperties;
import utility.graphics.filteredcombobox.SearchPerformer;
import utility.math.Integers;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

/**
 * Provides a display of possible items that could be chosen based on the search. An item can be chosen by the user using the mouse or the keyboard.
 * It also responds to external requests to choose the currently selected item - e.g. user hits the enter key in the search box.
 * @param <T> The type of the items being displayed.
 */
class FilteredComboBoxPossibleItemsDisplayer<T> implements Observable {
   
   public static final ObserverType<Boolean> SHOWING_STATE = new ObserverType<>();
   
   /**
    * Minimal internal styling used to make the the popup appear correctly without the user needing to set up and configure stylesheets externally.
    */
   protected static final String CONTEXT_MENU_STYLE = "-fx-padding: 0; -fx-border-insets: 0; -fx-effect: null;";
   protected static final String CONTEXT_MENU_ITEM_STYLE = "-fx-padding: 0; -fx-border-insets: 0;";
   
   private static final double HEIGHT = 200;
   
   private FilteredComboBoxModel<T> model;
   private SearchPerformer<T> searchPerformer;
   private FilteredComboBoxProperties properties;
   
   private ObservableManager observableManager;
   
   private BorderPane borderPane;
   private ObservableList<FilteredComboBoxItem<T>> listViewModel;
   private ListView<FilteredComboBoxItem<T>> listView;
   private CustomMenuItem customMenuItem;
   private ContextMenu contextMenu;
   private double dropdownHeight;
   private boolean firstShow;
   
   /**
    * Constructor building the displayed.
    * @param model The {@link FilteredComboBoxModel} to connect to.
    * @param searchPerformer {@link SearchPerformer} used to display the items by getting a text representation.
    * @param properties {@link FilteredComboBoxProperties} for configuration.
    */
   public FilteredComboBoxPossibleItemsDisplayer(
         FilteredComboBoxModel<T> model, SearchPerformer<T> searchPerformer, FilteredComboBoxProperties properties) {
      this.model = model;
      this.searchPerformer = searchPerformer;
      this.properties = properties;
      firstShow = true;
      
      observableManager = new ObservableManagerImpl();
      
      borderPane = new BorderPane();
      listViewModel = FXCollections.observableArrayList();
      listView = new ListView<>(listViewModel);
      borderPane.widthProperty().addListener((source, oldValue, newValue) -> listView.setPrefWidth(newValue.doubleValue()));
      listView.setPrefHeight(HEIGHT);
      listView.setOnMouseReleased(mouseEvent -> handleItemMouseReleased());
      
      model.getObservableManager().addObserver(model.getSearchCompletedObserverType(), this::searchCompleted);
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
   public BorderPane getNode() {
      return borderPane;
   }
   
   public boolean isShowing() {
      return contextMenu != null && contextMenu.isShowing();
   }
   
   /**
    * Set cell factory to render the items.
    * @param cellFactory Creates cell to show the items
    */
   public void setCellFactory(Callback<ListView<FilteredComboBoxItem<T>>, ListCell<FilteredComboBoxItem<T>>> cellFactory) {
      listView.setCellFactory(cellFactory);
   }
   
   /**
    * Search has completed and has produced the following items.
    * @param items Items to display.
    */
   private void searchCompleted(List<T> items) {
      listViewModel.clear();
      for (T item : items) {
         listViewModel.add(new FilteredComboBoxItem<>(item, searchPerformer.getSearchTextFromItem(item)));
      }
      if (contextMenu == null) {
         customMenuItem = new CustomMenuItem(listView, false);
         contextMenu = new ContextMenu(customMenuItem);
         contextMenu.setStyle(CONTEXT_MENU_STYLE);
         contextMenu.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
         show();
      } else if (!contextMenu.isShowing()) {
         show();
      }
      if (!listViewModel.isEmpty()) {
         listView.scrollTo(0);
         int maxIndex = listViewModel.size() - 1;
         listView.getSelectionModel().select(Math.min(properties.getDefaultSelectionIndex(), maxIndex));
      }
   }
   
   /**
    * Shows the drop down menu with the possible results to select from.
    */
   @SuppressWarnings("deprecation") //Using non API object here - currently required to work
   private void show() {
      Bounds bounds = borderPane.localToScreen(borderPane.getBoundsInLocal());
      if (firstShow) {
         /*
          * Hack to apply style class to the menu item to remove the border. This will likely need changing in future Java releases as uses a non API
          * call to work. Possibly related to: https://bugs.openjdk.java.net/browse/JDK-8149623
          */
         firstShow = false;
         contextMenu.show(borderPane, Side.TOP, bounds.getMinX(), bounds.getMaxY());
         customMenuItem.impl_styleableGetNode().setStyle(CONTEXT_MENU_ITEM_STYLE);
         contextMenu.hide();
         //Add listener after showing now so initial show and hide doesn't trigger
         contextMenu.showingProperty().addListener((source, oldValue, newValue) -> observableManager.notifyObservers(SHOWING_STATE, newValue));
      }
      contextMenu.show(borderPane, Side.TOP, bounds.getMinX(), bounds.getMaxY());
      if (contextMenu.getAnchorY() != bounds.getMaxY()) {
         contextMenu.show(borderPane, Side.BOTTOM, bounds.getMinX(), bounds.getMinY() - contextMenu.getHeight());
      }
      //Fix x position randomly being changed.
      contextMenu.setX(bounds.getMinX() - (listView.localToScreen(listView.getBoundsInLocal()).getMinX() - contextMenu.getX()));
      fixContextMenuY();
   }
   
   /**
    * Fix the Y coordinate of the context menu. This is required for when the filtered combo box is displayed in a context menu itself.
    */
   private void fixContextMenuY() {
      Bounds bounds = borderPane.localToScreen(borderPane.getBoundsInLocal());
      //Fix y position randomly being changed.
      contextMenu.setY(bounds.getMaxY() - (listView.localToScreen(listView.getBoundsInLocal()).getMinY() - contextMenu.getY()));
      
      if (contextMenu.getY() < bounds.getMaxY()) {
         //Fix y position randomly being changed.
         contextMenu.setY(bounds.getMinY() - (listView.localToScreen(listView.getBoundsInLocal()).getHeight()));
      }
   }
   
   /**
    * Handles a keyboard key being pressed.
    * @param keyEvent {@link KeyEvent} to handle.
    */
   private void handleKeyPressed(KeyEvent keyEvent) {
      switch (keyEvent.getCode()) {
         case ENTER:
            selectSelectedItem();
            break;
         case DOWN:
            moveSelection(1);
            break;
         case UP:
            moveSelection(-1);
            break;
         case TAB:
            contextMenu.hide();
            break;
         default:
            break;
      }
   }
   
   /**
    * Handles the mouse button being released.
    */
   private void handleItemMouseReleased() {
      selectSelectedItem();
   }
   
   /**
    * Selects the currently selected item.
    */
   private void selectSelectedItem() {
      FilteredComboBoxItem<T> item = listView.getSelectionModel().getSelectedItem();
      if (item != null) {
         model.itemChosen(item.getValue());
      }
      contextMenu.hide();
   }
   
   /**
    * Moves the selection index by the provided delta.
    * @param delta Amount to move by.
    */
   private void moveSelection(int delta) {
      if (!listViewModel.isEmpty()) {
         int index = listView.getSelectionModel().getSelectedIndex();
         int newIndex;
         if (delta < 0 && index < 0) {
            newIndex = Math.max(0, listViewModel.size() + delta);
         } else {
            newIndex = Integers.clamp(index + delta, 0, listViewModel.size() - 1);
         }
         listView.getSelectionModel().select(newIndex);
         listView.scrollTo(newIndex);
      }
   }
   
   /**
    * Sets the height of the dropdown to display the items in.
    * @param height New height to display at.
    */
   void setDropdownHeight(double height) {
      dropdownHeight = height;
      listView.setPrefHeight(dropdownHeight);
   }
   
   public double getDropdownHeight() {
      return dropdownHeight;
   }
   
   ObservableList<FilteredComboBoxItem<T>> getListViewModel() {
      return listViewModel;
   }
   
   ListView<FilteredComboBoxItem<T>> getListView() {
      return listView;
   }
   
   ContextMenu getContextMenu() {
      return contextMenu;
   }
   
   CustomMenuItem getCustomMenuItem() {
      return customMenuItem;
   }
}
