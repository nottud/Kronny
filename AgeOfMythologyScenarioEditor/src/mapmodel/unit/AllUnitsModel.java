
package mapmodel.unit;

import java.util.Arrays;
import java.util.List;

import datahandler.DataModel;
import datahandler.converter.IntegerConverter;
import datahandler.location.LocationNotFoundException;
import datahandler.location.NextSequenceLocationFinder;
import mapmodel.BranchModel;
import mapmodel.ListModel;
import mapmodel.ParentModel;

public class AllUnitsModel extends BranchModel {
   
   private NextSequenceLocationFinder unitsFinder = NextSequenceLocationFinder.afterSequence(Arrays.asList((byte) 0x80, (byte) 0xbf,
         (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0xbf, (byte) 0x00, (byte) 0x00, (byte) 0x80, (byte) 0xbf, (byte) 0x01, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x34, (byte) 0x33,
         (byte) 0x33, (byte) 0x3f, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x5a, (byte) 0x31), IntegerConverter.BYTES_IN_INT);
   
   private NextSequenceLocationFinder unitElementFinder =
         NextSequenceLocationFinder.afterSequence(Arrays.asList((byte) 0x45, (byte) 0x4e, (byte) 0x50), 0); //ENP
   
   private DataModel<Integer> unitNumber;
   private ListModel<UnitModel> unitModels;
   
   public AllUnitsModel(ParentModel parent) {
      super(parent);
      
      unitNumber = children.add("UnitNumber", new DataModel<>(this, unitsFinder, new IntegerConverter()));
      unitModels = children.add("Units", new ListModel<>(this, unitElementFinder, unitElementFinder, UnitModel::new));
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      unitNumber.readAllModels(data, offsetHint);
      unitModels.getChildModels().clear();
      int unitCount = unitNumber.getValue();
      for (int i = 0; i < unitCount; i++) {
         unitModels.getChildModels().add(unitModels.getChildFactory().apply(unitModels));
      }
      if (unitNumber.getValue() > 0) {
         unitModels.readAllModels(data, unitNumber.getDataLocationFinder().findLocation(data, offsetHint));
      }
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      unitModels.writeAllModels(data, offsetHint);
      unitNumber.writeAllModels(data, offsetHint);
   }
   
   public DataModel<Integer> getUnitNumber() {
      return unitNumber;
   }
   
   public ListModel<UnitModel> getUnitModels() {
      return unitModels;
   }
   
}
