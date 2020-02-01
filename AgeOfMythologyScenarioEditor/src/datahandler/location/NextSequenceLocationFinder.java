
package datahandler.location;

import java.util.List;
import java.util.OptionalInt;

public class NextSequenceLocationFinder implements DataLocationFinder {
   
   private List<Byte> byteSequence;
   private int additionalOffset;
   private boolean after;
   
   private NextSequenceLocationFinder(List<Byte> byteSequence, int additionalOffset, boolean after) {
      this.byteSequence = byteSequence;
      this.additionalOffset = additionalOffset;
      this.after = after;
   }
   
   public static NextSequenceLocationFinder atSequenceStart(List<Byte> byteSequence, int additionalOffset) {
      return new NextSequenceLocationFinder(byteSequence, additionalOffset, false);
   }
   
   public static NextSequenceLocationFinder afterSequence(List<Byte> byteSequence, int additionalOffset) {
      return new NextSequenceLocationFinder(byteSequence, additionalOffset, true);
   }
   
   @Override
   public int findLocation(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      OptionalInt matchLocation = OptionalInt.empty();
      for (int i = offsetHint; i < data.size(); i++) {
         boolean match = evaluateLocation(data, i);
         if (match) {
            matchLocation = OptionalInt.of(i);
            break;
         }
      }
      if (!matchLocation.isPresent()) {
         throw new LocationNotFoundException("Location could not be determined.");
      }
      if (after) {
         return matchLocation.getAsInt() + byteSequence.size() + additionalOffset;
      } else {
         return matchLocation.getAsInt() + additionalOffset;
      }
   }
   
   private boolean evaluateLocation(List<Byte> data, int offset) {
      for (int i = 0; i < byteSequence.size(); i++) {
         if (i + offset >= data.size()) {
            return false;
         }
         Byte sequenceByte = byteSequence.get(i);
         Byte dataByte = data.get(i + offset);
         if (!sequenceByte.equals(dataByte)) {
            return false;
         }
      }
      return true;
   }
   
}
