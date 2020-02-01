
package datahandler.location;

import java.util.List;

public interface DataLocationFinder {
   
   public int findLocation(List<Byte> bytes, int offsetHint) throws LocationNotFoundException;
   
}
