
package utility.graphics.filteredcombobox.javafx;

import javafx.application.Platform;

/**
 * Holds the state of the {@link FilteredComboBoxPossibleItemsDisplayer} but is delayed to allow places to check later on the same update cycle to
 * check the old state of the {@link FilteredComboBoxPossibleItemsDisplayer}.
 */
class FilteredComboBoxPossibleItemsDisplayerDelayedState {
   
   private boolean showing;
   
   /**
    * Constructor storing the initial state and registering for changes.
    * @param itemsDisplayer Displays the possible items.
    */
   public FilteredComboBoxPossibleItemsDisplayerDelayedState(FilteredComboBoxPossibleItemsDisplayer<?> itemsDisplayer) {
      showing = itemsDisplayer.isShowing();
      
      itemsDisplayer.getObservableManager().addObserver(FilteredComboBoxPossibleItemsDisplayer.SHOWING_STATE, this::updateState);
   }
   
   /**
    * Updates to the new state but delays it.
    * @param newState New state to update to.
    */
   private void updateState(boolean newState) {
      Platform.runLater(() -> showing = newState);
   }
   
   public boolean isShowing() {
      return showing;
   }
   
}
