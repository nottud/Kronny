
package datahandler.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BooleanConverter implements DataConverter<Boolean> {
   
   private static final List<Byte> FALSE_VALUE = Collections.unmodifiableList(Arrays.asList((byte) 0x00));
   private static final List<Byte> TRUE_VALUE = Collections.unmodifiableList(Arrays.asList((byte) 0xFF));
   
   @Override
   public Boolean fromBytes(List<Byte> bytes, int offset) {
      List<Byte> booleanBytes = bytes.subList(offset, offset + 1);
      if (booleanBytes.equals(TRUE_VALUE)) {
         return true;
      }
      if (booleanBytes.equals(FALSE_VALUE)) {
         return false;
      }
      throw new IllegalStateException("Conversion error.");
   }
   
   @Override
   public List<Byte> toBytes(Boolean value) {
      return value ? TRUE_VALUE : FALSE_VALUE;
   }
   
   @Override
   public int getStorageLength(List<Byte> bytes, int offset) {
      return 1;
   }
   
   @Override
   public Boolean createDefaultValue() {
      return false;
   }
   
}
