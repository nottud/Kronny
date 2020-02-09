
package utility.graphics.filteredcombobox;

import java.util.function.Function;
import java.util.function.Supplier;

import utility.search.regex.SearchUtilities;

/**
 * Extension of {@link AbstractRegexTextSearchPerformer} providing common code to reduce duplication definable using functions. The regex itself uses
 * the common defined simple version where * means any string and ? means any character. The results have an implicit * at the end and case
 * sensitivity as off to make searching easier.
 * @param <T> Type of value to search.
 */
public class SimpleRegexTextSearchPerformer<T> extends AbstractRegexTextSearchPerformer<T> {
   
   private Supplier<? extends Iterable<T>> possibleItemsSupplier;
   private Function<T, String> itemToSearchText;
   
   /**
    * Constructor.
    * @param possibleItemsSupplier Provides the possible items to search from as a {@link Supplier}. This allows mutations in the items available over
    *           time.
    * @param itemToSearchText Provides mapping of item to the text expected to search for it.
    */
   public SimpleRegexTextSearchPerformer(Supplier<? extends Iterable<T>> possibleItemsSupplier, Function<T, String> itemToSearchText) {
      this.possibleItemsSupplier = possibleItemsSupplier;
      this.itemToSearchText = itemToSearchText;
   }
   
   @Override
   public String getSearchTextFromItem(T item) {
      return itemToSearchText.apply(item);
   }
   
   @Override
   public Iterable<T> getData() {
      return possibleItemsSupplier.get();
   }
   
   @Override
   public void search(String text) {
      super.search(SearchUtilities.convertSearchTextForRegexMatch(text, true, false));
   }
   
}
