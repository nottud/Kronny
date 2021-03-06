
package utility.graphics.filteredcombobox;

/**
 * Holds an item for a filtered combo box. This provides a means to deal with an issue where a null item selected was being treated as no
 * selection. This wrapping ensures that every item is non null regardless of the value. In addition this provides a convenient {@link String} to
 * display representing the item.
 * @param <T> The type of the item.
 */
public class FilteredComboBoxItem<T> {
   
   private T value;
   private String searchText;
   
   /**
    * Constructs with the provided value.
    * @param value Value to hold.
    * @param searchText {@link String} to display representing the item.
    */
   public FilteredComboBoxItem(T value, String searchText) {
      this.value = value;
      this.searchText = searchText;
   }
   
   public T getValue() {
      return value;
   }
   
   public String getSearchText() {
      return searchText;
   }
   
   @Override
   public String toString() {
      return searchText;
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((searchText == null) ? 0 : searchText.hashCode());
      result = prime * result + ((value == null) ? 0 : value.hashCode());
      return result;
   }
   
   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      FilteredComboBoxItem<?> other = (FilteredComboBoxItem<?>) obj;
      if (searchText == null) {
         if (other.searchText != null) {
            return false;
         }
      } else if (!searchText.equals(other.searchText)) {
         return false;
      }
      if (value == null) {
         if (other.value != null) {
            return false;
         }
      } else if (!value.equals(other.value)) {
         return false;
      }
      return true;
   }
}
