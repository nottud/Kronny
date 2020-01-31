
package datahandler.location;

import java.util.List;

import datahandler.DataModel;

public class AfterKnownLocationFinder implements DataLocationFinder {
   
   private DataModel<?> dataModel;
   private int offset;
   
   public AfterKnownLocationFinder(DataModel<?> dataModel, int offset) {
      this.dataModel = dataModel;
      this.offset = offset;
   }
   
   @Override
   public int findLocation(List<Byte> bytes, int offsetHint) {
      int location = dataModel.getDataLocationFinder().findLocation(bytes, offsetHint);
      return offset + location + dataModel.getDataConverter().getStorageLength(bytes, location);
   }
   
}
