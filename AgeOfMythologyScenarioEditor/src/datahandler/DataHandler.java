
package datahandler;

import java.util.List;

import datahandler.converter.DataConverter;
import datahandler.location.DataLocationFinder;
import datahandler.location.LocationNotFoundException;

public class DataHandler<T> {
   
   private DataLocationFinder dataLocationFinder;
   private DataConverter<T> dataConverter;
   
   public DataHandler(DataLocationFinder dataLocationFinder, DataConverter<T> dataConverter) {
      this.dataLocationFinder = dataLocationFinder;
      this.dataConverter = dataConverter;
   }
   
   public T readValue(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      return dataConverter.fromBytes(data, dataLocationFinder.findLocation(data, offsetHint));
   }
   
   public void writeValue(List<Byte> data, int offsetHint, T value) throws LocationNotFoundException {
      int location = dataLocationFinder.findLocation(data, offsetHint);
      
      int bytesToRemoveLength = dataConverter.getStorageLength(data, location);
      List<Byte> newBytes = dataConverter.toBytes(value);
      
      if (bytesToRemoveLength == newBytes.size()) {
         for (int i = 0; i < bytesToRemoveLength; i++) {
            data.set(i + location, newBytes.get(i));
         }
      } else {
         data.subList(location, location + bytesToRemoveLength).clear();
         data.addAll(location, dataConverter.toBytes(value));
      }
   }
   
   public DataLocationFinder getDataLocationFinder() {
      return dataLocationFinder;
   }
   
}
