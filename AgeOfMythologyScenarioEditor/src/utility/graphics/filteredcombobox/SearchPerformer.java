
package utility.graphics.filteredcombobox;

/**
 * Allows the filtered combo box to request the possible choices given a search text. After performing the search all the registered search result
 * processors should be notified of the results. This implementation allows for a asynchronous searches used for when it is expensive to do.
 * @param <T> Type of items that can be searched.
 */
public interface SearchPerformer<T> {
   
   /**
    * Request to start a search with the provided search text.
    * @param text Text to search.
    */
   public void search(String text);
   
   /**
    * Requests all the items.
    */
   public void requestAll();
   
   /**
    * Returns the text representing the item that should be displayed.
    * @param item Item to get the text of.
    * @return The text to display.
    */
   public String getSearchTextFromItem(T item);
   
   /**
    * Adds a search result processor to be notified of search completion.
    * @param searchResultProcessor Listener to add.
    */
   public void addSearchResultProcessor(SearchResultProcessor<T> searchResultProcessor);
   
   /**
    * Removes the specified search result processor.
    * @param searchResultProcessor Listener to remove.
    */
   public void removeSearchResultProcessor(SearchResultProcessor<T> searchResultProcessor);
   
}
