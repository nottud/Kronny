
package utility.graphics.filteredcombobox.javafx;

import javafx.scene.control.Button;
import utility.graphics.filteredcombobox.FilteredComboBoxModel;

/**
 * Provides a {@link Button} to list all items alphabetically.
 */
class FilteredComboBoxAllButton {
   
   private FilteredComboBoxModel<?> filteredComboBoxModel;
   private FilteredComboBoxPossibleItemsDisplayerDelayedState delayedState;
   private Button button;
   
   private boolean disableAction;
   
   /**
    * Creates button to list all alphabetically.
    * @param filteredComboBoxModel {@link FilteredComboBoxModel} to call through to.
    * @param delayedState {@link FilteredComboBoxPossibleItemsDisplayerDelayedState} to track the state of the items displayer at a delayed level to
    *           prevent the button action interfering with clicking on it to close.
    */
   public FilteredComboBoxAllButton(FilteredComboBoxModel<?> filteredComboBoxModel, FilteredComboBoxPossibleItemsDisplayerDelayedState delayedState) {
      this.filteredComboBoxModel = filteredComboBoxModel;
      this.delayedState = delayedState;
      
      button = new Button();
      button.setText("\u25BC");
      button.setFocusTraversable(false);
      
      button.setOnMousePressed(mouseEvent -> disableActionIfShowing());
      button.setOnMouseReleased(mouseEvent -> disableAction = false);
      button.setOnAction(actionEvent -> requestAllIfDelayedStateSaysNotAlreadyOpen());
   }
   
   /**
    * Prevents the button from working if the delayed state is showing.
    */
   private void disableActionIfShowing() {
      if (delayedState.isShowing()) {
         disableAction = true;
      }
   }
   
   /**
    * Does a request all to the model providing it is not disabled and re-enables the button.
    */
   private void requestAllIfDelayedStateSaysNotAlreadyOpen() {
      if (!disableAction) {
         filteredComboBoxModel.requestAll();
      }
      disableAction = false;
   }
   
   public Button getNode() {
      return button;
   }
   
   public double getPrefWidth() {
      return button.getPrefWidth();
   }
   
}
