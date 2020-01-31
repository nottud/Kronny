
package datahandler.converter;

import java.util.ArrayList;
import java.util.List;

public class ListConverter<T> implements DataConverter<List<T>> {
   
   private DataConverter<T> subConverter;
   
   private IntegerConverter integerConverter;
   
   public ListConverter(DataConverter<T> subConverter) {
      this.subConverter = subConverter;
      
      integerConverter = new IntegerConverter();
   }
   
   @Override
   public List<T> fromBytes(List<Byte> bytes, int offset) {
      int length = integerConverter.fromBytes(bytes, offset);
      
      List<T> elements = new ArrayList<>(length);
      int totalOffset = offset + integerConverter.getStorageLength(bytes, 0);
      for (int i = 0; i < length; i++) {
         elements.add(subConverter.fromBytes(bytes, totalOffset));
         totalOffset = totalOffset + subConverter.getStorageLength(bytes, totalOffset);
      }
      return elements;
   }
   
   @Override
   public List<Byte> toBytes(List<T> value) {
      List<Byte> bytes = new ArrayList<>();
      int length = value.size();
      bytes.addAll(integerConverter.toBytes(length));
      
      for (T valueElement : value) {
         bytes.addAll(subConverter.toBytes(valueElement));
      }
      return bytes;
   }
   
   @Override
   public int getStorageLength(List<Byte> bytes, int offset) {
      int length = integerConverter.fromBytes(bytes, offset);
      
      int totalOffset = offset + integerConverter.getStorageLength(bytes, 0);
      for (int i = 0; i < length; i++) {
         totalOffset = totalOffset + subConverter.getStorageLength(bytes, totalOffset);
      }
      return totalOffset - offset;
   }
   
   @Override
   public List<T> createDefaultValue() {
      return new ArrayList<>();
   }
   
}
