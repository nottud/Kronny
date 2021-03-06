
package utility.graphics.filteredcombobox;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides useful common code between search performer workers handling the notification list adding a utility method to notify the search completed
 * to processors.
 * @param <T> Type of item the search is for.
 */
public abstract class AbstractSearchPerformer<T> implements SearchPerformer<T> {
   
   private Set<SearchResultProcessor<T>> searchResultProcessors = new LinkedHashSet<>();
   
   @Override
   public void addSearchResultProcessor(SearchResultProcessor<T> searchResultProcessor) {
      searchResultProcessors.add(searchResultProcessor);
   }
   
   @Override
   public void removeSearchResultProcessor(SearchResultProcessor<T> searchResultProcessor) {
      searchResultProcessors.remove(searchResultProcessor);
   }
   
   /**
    * Notifies all processors that the search has completed providing the results.
    * @param results Results to process.
    */
   protected void notifySearchResultProcessors(List<T> results) {
      for (SearchResultProcessor<T> searchResultProcessor : searchResultProcessors) {
         searchResultProcessor.processResult(results);
      }
   }
   
}
