
package mapmodel.player;

import java.util.Arrays;
import java.util.List;

import datahandler.DataModel;
import datahandler.converter.IntegerConverter;
import datahandler.location.NextSequenceLocationFinder;
import mapmodel.BranchModel;
import mapmodel.ListModel;
import mapmodel.ParentModel;

public class AllPlayersModel extends BranchModel {
   
   private NextSequenceLocationFinder playerElementFinder;
   
   private DataModel<Integer> playerNumber;
   private ListModel<PlayerModel> playerModels;
   
   public AllPlayersModel(ParentModel parentModel) {
      super(parentModel);
      playerElementFinder = NextSequenceLocationFinder.afterSequence(Arrays.asList((byte) 0x42, (byte) 0x50), 0); //BP
      
      playerNumber = children.add("PlayerNumber", new DataModel<>(this, NextSequenceLocationFinder.atSequenceStart(Arrays.asList(
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x42, (byte) 0x50/*
                                                                                         * , (byte) 0x61, (byte) 0x04, (byte) 0x00, (byte) 0x00,
                                                                                         * (byte) 0x4e
                                                                                         */), -1), new IntegerConverter()));
      playerModels = children.add("Players", new ListModel<>(this, playerElementFinder, playerElementFinder, PlayerModel::new));
   }
   
   public DataModel<Integer> getPlayerNumber() {
      return playerNumber;
   }
   
   public ListModel<PlayerModel> getPlayerModels() {
      return playerModels;
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) {
      playerNumber.readAllModels(data, offsetHint);
      playerModels.getChildModels().clear();
      int playerCount = playerNumber.getValue();
      for (int i = 0; i < playerCount; i++) {
         playerModels.getChildModels().add(playerModels.getChildFactory().apply(playerModels));
      }
      playerModels.readAllModels(data, playerNumber.getDataLocationFinder().findLocation(data, offsetHint));
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) {
      playerModels.writeAllModels(data, playerNumber.getDataLocationFinder().findLocation(data, offsetHint));
      playerNumber.writeAllModels(data, offsetHint);
   }
   
}
