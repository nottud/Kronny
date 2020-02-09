
package utility.graphics.filteredcombobox;

import utility.observable.Observable;
import utility.observable.ObserverType;

/**
 * Requirements for an implementation of a filtered combo box.
 * @param <T> Type of item it holds.
 */
public interface FilteredComboBoxRequirements<T> extends Observable {
   
   /**
    * Sets the item that is currently considered selected. This item item can be anything including items that the {@link SearchPerformer} can never
    * return.
    * @param item Item considered selected.
    */
   public void setSelectedItem(T item);
   
   /**
    * Returns the currently selected item. This is the current item being shown or if the user has typed in it will be the last item chosen.
    * @return The currently found item.
    */
   public T getSelectedItem();
   
   /**
    * Returns the {@link ObserverType} allowing listening to when the select item has been changed.
    * @return {@link ObserverType} to register with.
    */
   public ObserverType<T> getSelectedItemChangedObserverType();
   
   /**
    * Returns the {@link ObserverType} allowing listening to when the user has already selected and item and hits enter to perform an action based
    * upon the currently selected item.
    * @return {@link ObserverType} to register with.
    */
   public ObserverType<T> getInputActionPerformedObserverType();
   
   /**
    * Focuses the input box.
    */
   public void requestFocusOnInput();
   
   /**
    * Gets if the input box currently has focus.
    * @return True if the input box currently has focus.
    */
   public boolean isInputFocused();
   
   /**
    * Returns the {@link SearchPerformer} used when searching.
    * @return The found {@link SearchPerformer}.
    */
   public SearchPerformer<T> getSearchPerformer();
   
   /**
    * Returns the {@link FilteredComboBoxProperties} that determining additional behaviour.
    * @return The {@link FilteredComboBoxProperties} in use.
    */
   public FilteredComboBoxProperties getProperties();
   
   /**
    * Sets the height of the dropdown box.
    * @param height of dropdown.
    */
   public void setDropdownHeight(double height);
   
   /**
    * Gets the height of the dropdown box.
    * @return the dropdown height.
    */
   public double getDropdownHeight();
}
