
package utility.graphics.filteredcombobox;

import java.util.List;

/**
 * Allows for call back when a search completes to process the results.
 * @param <T> Type of the results.
 */
public interface SearchResultProcessor<T> {
   
   /**
    * Callback when a search completes with the results.
    * @param results Results to process.
    */
   public void processResult(List<T> results);
   
}
