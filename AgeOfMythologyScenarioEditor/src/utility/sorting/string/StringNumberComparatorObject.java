
package utility.sorting.string;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Makes use of the {@link StringNumberComparator} by converting the value to a string and handling null cases.
 */
public class StringNumberComparatorObject implements Comparator<Object>, Serializable {
   
   /**
    * Provides a public accessible instance of this singleton for external use.
    */
   public static final StringNumberComparatorObject INSTANCE = new StringNumberComparatorObject();
   
   private static final long serialVersionUID = -1309801401065994264L;
   
   /**
    * Singleton pattern - no more than one instance needed.
    */
   private StringNumberComparatorObject() {
   }
   
   @Override
   public int compare(Object o1, Object o2) {
      if (o1 == null) {
         return -1;
      } else if (o2 == null) {
         return 1;
      } else {
         return StringNumberComparator.INSTANCE.compare(o1.toString(), o2.toString());
      }
   }
   
}
