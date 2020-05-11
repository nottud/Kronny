
package mapmodel.multiplayeroverride;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import datahandler.converter.DataConverter;
import datahandler.location.LocationNotFoundException;
import datahandler.location.NextSequenceLocationFinder;

public class MultiplayerOverrideDataConverter implements DataConverter<Boolean> {
   
   private static final NextSequenceLocationFinder END_OF_DATA_FINDER =
         NextSequenceLocationFinder.atSequenceStart(Arrays.asList((byte) 0x56, (byte) 0x56, (byte) 0x04), 0);
   
   public static final int EXISTENCE_LENGTH = 1;
   
   private static final byte NO_OVERRIDE = (byte) 0;
   private static final byte OVERRIDE = (byte) 1;
   
   private Supplier<Integer> lengthSupplier;
   
   public MultiplayerOverrideDataConverter(Supplier<Integer> lengthSupplier) {
      this.lengthSupplier = lengthSupplier;
   }
   
   @Override
   public Boolean fromBytes(List<Byte> bytes, int offset) {
      return bytes.get(offset) == OVERRIDE;
   }
   
   @Override
   public List<Byte> toBytes(Boolean value) {
      if (value) {
         int length = lengthSupplier.get();
         List<Byte> bytes = new ArrayList<>(length + 1);
         bytes.add(0, OVERRIDE);
         for (int i = 0; i < length; i++) {
            bytes.add((byte) 0);
         }
         return bytes;
      } else {
         return Arrays.asList(NO_OVERRIDE);
      }
   }
   
   @Override
   public int getStorageLength(List<Byte> bytes, int offset) {
      if (bytes.get(offset) == OVERRIDE) {
         try {
            return END_OF_DATA_FINDER.findLocation(bytes, offset) - offset;
         } catch (LocationNotFoundException e) {
            return bytes.size() - offset;
         }
      } else {
         return 1;
      }
   }
   
   @Override
   public Boolean createDefaultValue() {
      return false;
   }
   
}
