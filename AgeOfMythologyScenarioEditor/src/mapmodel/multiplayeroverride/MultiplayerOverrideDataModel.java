
package mapmodel.multiplayeroverride;

import java.util.Arrays;
import java.util.List;

import datahandler.DataModel;
import datahandler.converter.FixedDataChunkConverter;
import datahandler.converter.FullStringConverter;
import datahandler.converter.IntegerConverter;
import datahandler.location.AfterKnownLocationFinder;
import datahandler.location.LocationNotFoundException;
import datahandler.location.RelativeLocationFinder;
import mapmodel.BranchModel;
import mapmodel.ParentModel;
import mapmodel.RootModel;

public class MultiplayerOverrideDataModel extends BranchModel {
   
   private static final List<Byte> INITIAL_FIXED_DATA = Arrays.asList((byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x10,
         (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00,
         (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x10,
         (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00,
         (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x10,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10);
   
   private static final List<Byte> SECONDARY_FIXED_DATA = Arrays.asList((byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00);
   
   private static final List<Byte> FINAL_FIXED_DATA = Arrays.asList((byte) 0x00, (byte) 0x00, (byte) 0xc8, (byte) 0x44,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x02, (byte) 0x02, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x03, (byte) 0x01, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x04, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x05,
         (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x06, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x07, (byte) 0x07, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x08, (byte) 0x01, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x09, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0a, (byte) 0x0a,
         (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0b, (byte) 0x0b, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x0c, (byte) 0x0c, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0d, (byte) 0x0d, (byte) 0x01, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x0e, (byte) 0x0e, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0x0f,
         (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x0e, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x6e, (byte) 0x00, (byte) 0x6f, (byte) 0x00, (byte) 0x74, (byte) 0x00, (byte) 0x74, (byte) 0x00, (byte) 0x75, (byte) 0x00,
         (byte) 0x64, (byte) 0x00, (byte) 0x27, (byte) 0x00, (byte) 0x73, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x47, (byte) 0x00,
         (byte) 0x61, (byte) 0x00, (byte) 0x6d, (byte) 0x00, (byte) 0x65, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00);
   
   private RootModel rootModel;
   
   private DataModel<List<Byte>> initialFixedData;
   private DataModel<Integer> playerNumber;
   private DataModel<List<Byte>> secondaryFixedData;
   private DataModel<String> player1Name;
   private DataModel<List<Byte>> finalFixedData;
   
   public MultiplayerOverrideDataModel(ParentModel parent, RootModel rootModel) {
      super(parent);
      this.rootModel = rootModel;
      
      initialFixedData = new DataModel<>(this, new RelativeLocationFinder(0), new FixedDataChunkConverter(INITIAL_FIXED_DATA));
      playerNumber = new DataModel<>(this, new AfterKnownLocationFinder(initialFixedData, 0), new IntegerConverter());
      secondaryFixedData = new DataModel<>(this, new AfterKnownLocationFinder(playerNumber, 0), new FixedDataChunkConverter(SECONDARY_FIXED_DATA));
      player1Name = new DataModel<>(this, new AfterKnownLocationFinder(secondaryFixedData, 0), new FullStringConverter());
      finalFixedData = new DataModel<>(this, new AfterKnownLocationFinder(player1Name, 0), new FixedDataChunkConverter(FINAL_FIXED_DATA));
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      //Nothing to do
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      initialFixedData.writeAllModels(data, offsetHint);
      
      playerNumber.setValue(rootModel.getAllPlayersModel().getPlayerNumber().getValue() - 1);
      playerNumber.writeAllModels(data, offsetHint);
      
      secondaryFixedData.writeAllModels(data, offsetHint);
      
      player1Name.setValue(rootModel.getAllPlayersModel().getPlayerModels().getChildModels().get(1).getPlayerName().getValue() + "\0");
      player1Name.writeAllModels(data, offsetHint);
      
      finalFixedData.writeAllModels(data, offsetHint);
   }
   
   public int getLength() {
      return initialFixedData.getDataConverter().toBytes(initialFixedData.getValue()).size()
            + playerNumber.getDataConverter().toBytes(playerNumber.getValue()).size()
            + secondaryFixedData.getDataConverter().toBytes(secondaryFixedData.getValue()).size()
            + player1Name.getDataConverter().toBytes("").size()
            + finalFixedData.getDataConverter().toBytes(finalFixedData.getValue()).size();
   }
   
}
