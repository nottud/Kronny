
package mapmodel.multiplayeroverride;

import java.util.List;

import datahandler.DataModel;
import datahandler.converter.IntegerConverter;
import datahandler.location.AfterKnownLocationFinder;
import datahandler.location.LocationNotFoundException;
import datahandler.location.RelativeLocationFinder;
import mapmodel.BranchModel;
import mapmodel.RootModel;

public class MultiplayerOverrideModel extends BranchModel {
   
   private MultiplayerOverrideDataModel multiplayerOverrideDataModel;
   
   private DataModel<Integer> playerNumber;
   private DataModel<Boolean> shouldOverride;
   
   public MultiplayerOverrideModel(RootModel parent) {
      super(parent);
      
      multiplayerOverrideDataModel = children.add("OverrideData", new MultiplayerOverrideDataModel(this, parent));
      
      playerNumber = children.add("playerNumber", new DataModel<>(this, new RelativeLocationFinder(172), new IntegerConverter()));
      shouldOverride = children.add("shouldOverride", new DataModel<>(this, new AfterKnownLocationFinder(playerNumber, 21),
            new MultiplayerOverrideDataConverter(multiplayerOverrideDataModel::getInitialLength)));
   }
   
   public MultiplayerOverrideDataModel getMultiplayerOverrideDataModel() {
      return multiplayerOverrideDataModel;
   }
   
   public DataModel<Integer> getPlayerNumber() {
      return playerNumber;
   }
   
   public DataModel<Boolean> getShouldOverride() {
      return shouldOverride;
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      playerNumber.readAllModels(data, offsetHint);
      shouldOverride.readAllModels(data, offsetHint);
      if (shouldOverride.getValue()) {
         multiplayerOverrideDataModel.readAllModels(data, shouldOverride.getDataLocationFinder().findLocation(data, offsetHint) + 1);
      }
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      shouldOverride.writeAllModels(data, offsetHint);
      playerNumber.writeAllModels(data, offsetHint);
      if (shouldOverride.getValue()) {
         multiplayerOverrideDataModel.writeAllModels(data, shouldOverride.getDataLocationFinder().findLocation(data, offsetHint) + 1);
      }
   }
   
}
