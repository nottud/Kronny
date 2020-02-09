
package utility.graphics.filteredcombobox;

import java.util.List;
import java.util.function.Consumer;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

/**
 * The underlying system that the graphical elements of the filtered combo box work off allowing for each of those elements to be switched out
 * as non of them directly depend on each other.
 * @param <T> The type of value the filtered combo box holds.
 */
public class FilteredComboBoxModel<T> implements Observable {
   
   private SearchPerformer<T> searchPerformer;
   private FilteredComboBoxProperties properties;
   private Consumer<Runnable> threadRedirectConsumer;
   
   private ObservableManager observableManager;
   
   private ObserverType<List<T>> searchCompletedObserverType;
   private ObserverType<T> itemChosenObserverTypeInternal;
   private ObserverType<T> itemChosenObserverType;
   
   private T selectedItem;
   
   /**
    * Constructs the model building the internals ready for use.
    * @param searchPerformer The search does the actual searching and returns the results.
    * @param properties Used to determine if the model should switch the selection back to null after an item has been chosen.
    * @param threadRedirectConsumer Provides redirection of the provided runnable onto the appropriate graphics thread.
    */
   public FilteredComboBoxModel(SearchPerformer<T> searchPerformer, FilteredComboBoxProperties properties,
         Consumer<Runnable> threadRedirectConsumer) {
      this.searchPerformer = searchPerformer;
      this.properties = properties;
      this.threadRedirectConsumer = threadRedirectConsumer;
      observableManager = new ObservableManagerImpl();
      
      searchCompletedObserverType = new ObserverType<>();
      itemChosenObserverTypeInternal = new ObserverType<>();
      itemChosenObserverType = new ObserverType<>();
      
      searchPerformer.addSearchResultProcessor(this::searchCompletedAnyThread);
   }
   
   /**
    * Handles search completing on any thread which calls searchCompleted but redirected onto the JavaFx {@link Thread}.
    * @param results Results the search came back with.
    */
   private void searchCompletedAnyThread(List<T> results) {
      threadRedirectConsumer.accept(() -> searchCompleted(results));
   }
   
   /**
    * Search has completed with the provided results and observers should be notified.
    * @param results The results of the search.
    */
   private void searchCompleted(List<T> results) {
      observableManager.notifyObservers(searchCompletedObserverType, results);
   }
   
   /**
    * Begins a search with the provided {@link String} text.
    * @param text Search text.
    */
   public void search(String text) {
      searchPerformer.search(text);
   }
   
   /**
    * Requests all possible items.
    */
   public void requestAll() {
      searchPerformer.requestAll();
   }
   
   /**
    * The user has considered to have chosen an item - store and notify.
    * @param item The item that was chosen.
    */
   public void itemChosen(T item) {
      selectedItem = item;
      observableManager.notifyObservers(itemChosenObserverTypeInternal, item);
      observableManager.notifyObservers(itemChosenObserverType, item);
      if (properties.isRevertToNullAfterSelection()) {
         selectedItem = null;
         observableManager.notifyObservers(itemChosenObserverTypeInternal, null);
      }
   }
   
   /**
    * Returns the currently selected item.
    * @return The currently selected item.
    */
   public T getSelectedItem() {
      return selectedItem;
   }
   
   public ObserverType<List<T>> getSearchCompletedObserverType() {
      return searchCompletedObserverType;
   }
   
   public ObserverType<T> getItemChosenObserverTypeInternal() {
      return itemChosenObserverTypeInternal;
   }
   
   public ObserverType<T> getItemChosenObserverType() {
      return itemChosenObserverType;
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
