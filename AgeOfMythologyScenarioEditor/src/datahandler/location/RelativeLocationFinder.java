
package datahandler.location;

import java.util.List;

public class RelativeLocationFinder implements DataLocationFinder {
   
   private int offset;
   
   public RelativeLocationFinder(int offset) {
      this.offset = offset;
   }
   
   @Override
   public int findLocation(List<Byte> bytes, int offsetHint) {
      return offset + offsetHint;
   }
   
}
