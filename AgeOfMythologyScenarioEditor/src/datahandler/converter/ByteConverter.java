
package datahandler.converter;

import java.util.Arrays;
import java.util.List;

public class ByteConverter implements DataConverter<Byte> {
   
   @Override
   public Byte fromBytes(List<Byte> bytes, int offset) {
      return bytes.get(offset);
   }
   
   @Override
   public List<Byte> toBytes(Byte value) {
      return Arrays.asList(value);
   }
   
   @Override
   public int getStorageLength(List<Byte> bytes, int offset) {
      return 1;
   }
   
   @Override
   public Byte createDefaultValue() {
      return (byte) 0x00;
   }
   
}
