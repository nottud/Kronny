
package utility.graphics.filteredcombobox;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

/**
 * Provides user keyboard based events to manipulate the items in the items displayer.
 */
public class FilteredComboBoxKeyEvents implements Observable {
   
   public static final ObserverType<Void> ACCEPT = new ObserverType<>();
   public static final ObserverType<Void> MOVE_SELECTION_UP = new ObserverType<>();
   public static final ObserverType<Void> MOVE_SELECTION_DOWN = new ObserverType<>();
   public static final ObserverType<Void> CANCEL = new ObserverType<>();
   
   private ObservableManager observableManager;
   
   /**
    * Constructor.
    */
   public FilteredComboBoxKeyEvents() {
      observableManager = new ObservableManagerImpl();
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
