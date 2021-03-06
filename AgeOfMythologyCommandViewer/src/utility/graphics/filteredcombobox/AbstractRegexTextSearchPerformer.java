
package utility.graphics.filteredcombobox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import javafx.application.Platform;
import utility.sorting.string.StringNumberComparator;

/**
 * Provides the ability for a regex search on a set of items using the {@link SearchPerformer#getSearchTextFromItem(Object)} method to get the search
 * text.
 * 
 * <p>Regards the order of the results. When a "requestAll" is made then it is treated as the user wanting to make a selection quickly without
 * filtering and therefore the results will be displayed in alphabetical order to make finding an item as quickly as possible. If the user performs a
 * search then the results are sorted by length followed by dictionary order. This ensures that the closest and simplest matches appear first before
 * the longer and complex ones.
 * @param <T> Type of item being searched.
 */
public abstract class AbstractRegexTextSearchPerformer<T> extends AbstractSearchPerformer<T> {
   
   private final Comparator<T> smallerDictionaryOrderComparator = new SmallestDictionaryOrderComparator();
   private final Comparator<T> dictionaryOrderComparator = new DictionaryOrderComparator();
   
   /**
    * Sorts the results by size and then dictionary order to give the closest match.
    */
   private class SmallestDictionaryOrderComparator implements Comparator<T> {
      
      @Override
      public int compare(T o1, T o2) {
         if (o1 == null) {
            if (o2 == null) {
               return 0;
            } else {
               return -1;
            }
         }
         if (o2 == null) {
            return 1;
         }
         String item1Text = getSearchTextFromItem(o1);
         String item2Text = getSearchTextFromItem(o2);
         if (item1Text.length() <= item2Text.length()) {
            if (item1Text.length() == item2Text.length()) {
               return StringNumberComparator.INSTANCE.compare(item1Text, item2Text);
            }
            return -1;
         } else {
            return 1;
         }
      }
   }
   
   /**
    * Sorts the results by dictionary order.
    */
   private class DictionaryOrderComparator implements Comparator<T> {
      
      @Override
      public int compare(T o1, T o2) {
         if (o1 == null) {
            if (o2 == null) {
               return 0;
            } else {
               return -1;
            }
         }
         if (o2 == null) {
            return 1;
         }
         String item1Text = getSearchTextFromItem(o1);
         String item2Text = getSearchTextFromItem(o2);
         return StringNumberComparator.INSTANCE.compare(item1Text, item2Text);
      }
   }
   
   /**
    * Performs the search using a swing worker so that it can be done in the background and not block the UI.
    */
   private class SearchPerformerWorker implements Runnable {
      
      private Iterable<T> possibleItems;
      private String search;
      private boolean requestAll;
      
      /**
       * Constructs a new search swing worker
       * @param search Search text to use.
       * @param possibleItems Possible items that may be found.
       * @param requestAll If all items are requested instead.
       */
      public SearchPerformerWorker(String search, Iterable<T> possibleItems, boolean requestAll) {
         this.search = search;
         this.possibleItems = possibleItems;
         this.requestAll = requestAll;
      }
      
      @Override
      public void run() {
         List<T> searchResults = new ArrayList<>();
         if (appendNull) {
            searchResults.add(null);
         }
         for (T item : possibleItems) {
            if (Thread.currentThread().isInterrupted()) {
               break;
            }
            if (requestAll) {
               searchResults.add(item);
            } else {
               performSearchOnItem(item, search, searchResults);
            }
         }
         sortResults(searchResults, !requestAll);
         resultRedirect.accept(() -> notifySearchResultProcessors(searchResults));
      }
   }
   
   private Consumer<Runnable> resultRedirect = null;
   private boolean appendNull = false;
   
   private Future<?> future;
   
   /**
    * Enables the asynchronous option to create the combo box options on the FX thread.
    */
   public void enableAsynchronousJavaFx() {
      resultRedirect = Platform::runLater;
   }
   
   public boolean isAsynchronous() {
      return resultRedirect != null;
   }
   
   public void setAppendNull(boolean appendNull) {
      this.appendNull = appendNull;
   }
   
   public boolean isAppendNull() {
      return appendNull;
   }
   
   @Override
   public void search(String text) {
      beginSearch(text, false);
   }
   
   @Override
   public void requestAll() {
      beginSearch(null, true);
   }
   
   /**
    * Common method to be called to start the search.
    * @param text Text search to perform.
    * @param requestAll Instead retrieve all results.
    */
   private void beginSearch(String text, boolean requestAll) {
      if (!isAsynchronous()) {
         List<T> searchResults = new ArrayList<>();
         if (appendNull) {
            searchResults.add(null);
         }
         for (T item : getData()) {
            if (requestAll) {
               searchResults.add(item);
            } else {
               performSearchOnItem(item, text, searchResults);
            }
         }
         sortResults(searchResults, !requestAll);
         notifySearchResultProcessors(searchResults);
      } else {
         if (future != null && !future.isDone()) {
            future.cancel(true);
         }
         future = Executors.newSingleThreadExecutor(this::createDaemonThread).submit(new SearchPerformerWorker(text, getData(), requestAll));
      }
   }
   
   /**
    * Thread factor which uses daemon {@link Thread}s to prevent searches from preventing the application from terminating.
    * @param runnable The {@link Runnable} to run with the thread.
    * @return The created {@link Thread}.
    */
   private Thread createDaemonThread(Runnable runnable) {
      Thread thread = Executors.defaultThreadFactory().newThread(runnable);
      thread.setDaemon(true);
      return thread;
   }
   
   /**
    * Gets the possible items to search.
    * @return Possible items.
    */
   public abstract Iterable<T> getData();
   
   /**
    * Performs a search with the provided item with a regex matching string and an edit distance sorting string.
    * @param item Item to match.
    * @param search The regex search string.
    *           case.
    * @param searchResults The results list to populate.
    */
   private void performSearchOnItem(
         T item,
         String search,
         List<T> searchResults) {
      String itemSearchText = getSearchTextFromItem(item);
      if (itemSearchText.matches(search)) {
         searchResults.add(item);
      }
   }
   
   /**
    * Sorts the results according to the request type.
    * @param results Results to sort.
    * @param useSmallestDictionaryOrder Whether to prioritise length over dictionary.
    */
   private void sortResults(List<T> results, boolean useSmallestDictionaryOrder) {
      if (useSmallestDictionaryOrder) {
         Collections.sort(results, smallerDictionaryOrderComparator);
      } else {
         Collections.sort(results, dictionaryOrderComparator);
      }
   }
   
}
