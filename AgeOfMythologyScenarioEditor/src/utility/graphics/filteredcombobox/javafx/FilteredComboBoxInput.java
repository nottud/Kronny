
package utility.graphics.filteredcombobox.javafx;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import utility.graphics.BlockListener;
import utility.graphics.filteredcombobox.FilteredComboBoxModel;
import utility.graphics.filteredcombobox.FilteredComboBoxProperties;
import utility.graphics.filteredcombobox.SearchPerformer;

/**
 * Displays the currently selected value and allows the user to enter search text to locate an item. Both are displayed in the same place.
 * @param <T> Type of items the user can search for.
 */
class FilteredComboBoxInput<T> {
   
   private FilteredComboBoxModel<T> model;
   private SearchPerformer<T> searchPerformer;
   private FilteredComboBoxProperties properties;
   private BlockListener blockListener;
   private TextField textField;
   
   /**
    * Constructor building the input.
    * @param model {@link FilteredComboBoxModel} to connect to.
    * @param searchPerformer Used to retrieve the search text from an item.
    * @param properties Provides additional configuration. This is used to determine if when the user loses focus on the text input that the item
    *           chosen should set to null.
    */
   public FilteredComboBoxInput(FilteredComboBoxModel<T> model, SearchPerformer<T> searchPerformer, FilteredComboBoxProperties properties) {
      this.model = model;
      this.searchPerformer = searchPerformer;
      this.properties = properties;
      blockListener = new BlockListener();
      textField = new TextField();
      
      textField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
      
      textField.textProperty().addListener((source, oldValue, newValue) -> searchTextChanged(textField.getText()));
      
      textField.focusedProperty().addListener((source, oldValue, newValue) -> focusChanged(newValue));
      
      model.getObservableManager().addObserver(model.getItemChosenObserverTypeInternal(), this::itemChosen);
      itemChosen(model.getSelectedItem());
   }
   
   /**
    * The search text has been changed so perform a search if the text changed from a user interaction.
    * @param text The new text in the box.
    */
   private void searchTextChanged(String text) {
      if (!blockListener.isBlocked()) {
         blockListener.setBlocked(true);
         model.search(text);
         blockListener.setBlocked(false);
      }
   }
   
   /**
    * Item was chosen by the user so update the text to display it providing itself did't cause the update.
    * @param item The item that was chosen.
    */
   private void itemChosen(T item) {
      if (!blockListener.isBlocked()) {
         blockListener.setBlocked(true);
         textField.setText(searchPerformer.getSearchTextFromItem(item));
         textField.positionCaret(getLengthAcceptingNull(textField.getText()));
         blockListener.setBlocked(false);
      }
   }
   
   /**
    * Returns the length of a {@link String} but copes with null returning 0.
    * @param string {@link String} to check length of.
    * @return Length of {@link String} or 0 is null.
    */
   private int getLengthAcceptingNull(String string) {
      if (string == null) {
         return 0;
      } else {
         return string.length();
      }
   }
   
   /**
    * The focus to the input has changed. On focus the text is selected to allow searching quicker. If focus is lost then the text reverts to showing
    * the last valid selected item.
    * @param focused The new focus state.
    */
   private void focusChanged(boolean focused) {
      if (focused) {
         runLater(textField::selectAll);
      } else {
         if (properties.isSelectNullIfBlank() && textField.getText().isEmpty()) {
            if (model.getSelectedItem() != null) {
               model.itemChosen(null);
            }
         } else {
            itemChosen(model.getSelectedItem());
         }
      }
   }
   
   /**
    * Runs the runnable later on the JavaFx thread. This is intended to allow for delayed behaviour which would not work if applied immediately.
    * @param runnable {@link Runnable} to run.
    */
   void runLater(Runnable runnable) {
      Platform.runLater(runnable);
   }
   
   /**
    * Handles key presses while the dropdown is not open.
    * @param keyEvent {@link KeyEvent} to handle.
    */
   private void handleKeyPress(KeyEvent keyEvent) {
      if (KeyCode.DOWN.equals(keyEvent.getCode())) {
         model.search(textField.getText());
      }
   }
   
   public TextField getNode() {
      return textField;
   }
   
   /**
    * Request focus on the text input.
    */
   public void requestFocusOnInput() {
      textField.requestFocus();
   }
   
   /**
    * Returns true if the text input box is focused.
    * @return True if focused.
    */
   public boolean isInputFocused() {
      return textField.isFocused();
   }
   
   /**
    * Set preferred width.
    * @param width width to set
    */
   public void setPrefWidth(double width) {
      textField.setPrefWidth(width);
   }
   
}
