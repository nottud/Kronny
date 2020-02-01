
package datahandler.location;

import java.util.Iterator;
import java.util.List;

import datahandler.DataModel;
import mapmodel.ListModel;

public class AfterSimpleListLocationFinder implements DataLocationFinder {
   
   private ListModel<? extends DataModel<?>> listModel;
   private int offset;
   
   public AfterSimpleListLocationFinder(ListModel<? extends DataModel<?>> dataModel, int offset) {
      this.listModel = dataModel;
      this.offset = offset;
   }
   
   @Override
   public int findLocation(List<Byte> bytes, int offsetHint) throws LocationNotFoundException {
      int currentOffset = listModel.getBaseLocationFinder().findLocation(bytes, offsetHint);
      Iterator<? extends DataModel<?>> iterator = listModel.getChildModels().iterator();
      while (iterator.hasNext()) {
         DataModel<?> childModel = iterator.next();
         currentOffset = currentOffset + childModel.getDataConverter().getStorageLength(bytes, currentOffset);
      }
      
      return currentOffset + offset;
   }
   
}
