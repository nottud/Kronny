
package datahandler.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FixedDataChunkConverter implements DataConverter<List<Byte>> {
   
   private List<Byte> fixedBytes;
   
   public FixedDataChunkConverter(List<Byte> fixedBytes) {
      this.fixedBytes = Collections.unmodifiableList(new ArrayList<>(fixedBytes));
   }
   
   @Override
   public List<Byte> fromBytes(List<Byte> bytes, int offset) {
      return fixedBytes;
   }
   
   @Override
   public List<Byte> toBytes(List<Byte> value) {
      return fixedBytes;
   }
   
   @Override
   public int getStorageLength(List<Byte> bytes, int offset) {
      return fixedBytes.size();
   }
   
   @Override
   public List<Byte> createDefaultValue() {
      //Should never be edited
      return fixedBytes;
   }
   
}
