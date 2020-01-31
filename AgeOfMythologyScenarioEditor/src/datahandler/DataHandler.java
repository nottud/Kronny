
package datahandler;

import java.util.List;
import java.util.function.Predicate;

import datahandler.converter.DataConverter;
import datahandler.location.DataLocationFinder;

public class DataHandler<T> {
   
   private DataLocationFinder dataLocationFinder;
   private DataConverter<T> dataConverter;
   
   public DataHandler(DataLocationFinder dataLocationFinder, DataConverter<T> dataConverter) {
      this.dataLocationFinder = dataLocationFinder;
      this.dataConverter = dataConverter;
   }
   
   public T readValue(List<Byte> data, int offsetHint) {
      return dataConverter.fromBytes(data, dataLocationFinder.findLocation(data, offsetHint));
   }
   
   public void writeValue(List<Byte> data, int offsetHint, T value) {
      int location = dataLocationFinder.findLocation(data, offsetHint);
      
      int bytesToRemoveLength = dataConverter.getStorageLength(data, location);
      List<Byte> newBytes = dataConverter.toBytes(value);
      
      if (bytesToRemoveLength == newBytes.size()) {
         for (int i = 0; i < bytesToRemoveLength; i++) {
            data.set(i + location, newBytes.get(i));
         }
      } else {
         
         for (int i = 0; i < bytesToRemoveLength; i++) {
            data.remove(location);
         }
         //		removeItemsAtIndex(data, location, bytesToRemoveLength);
         data.addAll(location, dataConverter.toBytes(value));
      }
   }
   
   private void removeItemsAtIndex(List<Byte> items, int index, int count) {
      items.removeIf(new Predicate<Byte>() {
         
         private int currentIndex = 0;
         private int stopIndex = index + count;
         
         @Override
         public boolean test(Byte byteToTest) {
            boolean result = currentIndex >= index && currentIndex < stopIndex;
            currentIndex++;
            return result;
         }
      });
   }
   
   public DataLocationFinder getDataLocationFinder() {
      return dataLocationFinder;
   }
   
}
