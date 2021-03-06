
package utility.graphics.filteredcombobox;

/**
 * Properties to customise the filtered combo box.
 */
public class FilteredComboBoxProperties {
   
   private boolean allIncluded;
   private int defaultSelectionIndex;
   private boolean revertToNullAfterSelection;
   private boolean selectNullIfBlank;
   
   /**
    * Constructs with the default properties. The default properties are as follows:<ul>
    * <li>All button is included</li>
    * <li>The default selection index is 0</li>
    * <li>It will not revert to null are an item has been selected</li>
    * <li>If the user clears the input field it will not automatically default to null</li></ul>
    */
   public FilteredComboBoxProperties() {
      this.allIncluded = true;
      this.defaultSelectionIndex = 0;
      this.revertToNullAfterSelection = false;
      this.selectNullIfBlank = false;
   }
   
   public void setAllIncluded(boolean allIncluded) {
      this.allIncluded = allIncluded;
   }
   
   public boolean isAllIncluded() {
      return allIncluded;
   }
   
   public void setDefaultSelectionIndex(int defaultSelectionIndex) {
      this.defaultSelectionIndex = defaultSelectionIndex;
   }
   
   public int getDefaultSelectionIndex() {
      return defaultSelectionIndex;
   }
   
   public void setRevertToNullAfterSelection(boolean revertToNullAfterSelection) {
      this.revertToNullAfterSelection = revertToNullAfterSelection;
   }
   
   public boolean isRevertToNullAfterSelection() {
      return revertToNullAfterSelection;
   }
   
   public void setSelectNullIfBlank(boolean selectNullIfBlank) {
      this.selectNullIfBlank = selectNullIfBlank;
   }
   
   public boolean isSelectNullIfBlank() {
      return selectNullIfBlank;
   }
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (allIncluded ? 1231 : 1237);
      result = prime * result + defaultSelectionIndex;
      result = prime * result + (revertToNullAfterSelection ? 1231 : 1237);
      result = prime * result + (selectNullIfBlank ? 1231 : 1237);
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
      FilteredComboBoxProperties other = (FilteredComboBoxProperties) obj;
      if (allIncluded != other.allIncluded) {
         return false;
      }
      if (defaultSelectionIndex != other.defaultSelectionIndex) {
         return false;
      }
      if (revertToNullAfterSelection != other.revertToNullAfterSelection) {
         return false;
      }
      if (selectNullIfBlank != other.selectNullIfBlank) {
         return false;
      }
      return true;
   }
   
}
