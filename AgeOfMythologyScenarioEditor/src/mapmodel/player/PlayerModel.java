
package mapmodel.player;

import java.util.List;

import datahandler.DataModel;
import datahandler.converter.FullStringConverter;
import datahandler.converter.IntegerConverter;
import datahandler.location.AfterKnownLocationFinder;
import datahandler.location.RelativeLocationFinder;
import mapmodel.BranchModel;
import mapmodel.ParentModel;

public class PlayerModel extends BranchModel {
   
   private DataModel<Integer> unknown1;
   private DataModel<Integer> unknown2;
   private DataModel<Integer> playerId;
   private DataModel<String> playerName;
   private DataModel<Integer> unknown3;
   private DataModel<Integer> unknown4;
   private DataModel<Integer> unknown5;
   private DataModel<Integer> unknown6;
   
   public PlayerModel(ParentModel parentModel) {
      super(parentModel);
      unknown1 = children.add("unknown1", new DataModel<>(this, new RelativeLocationFinder(0), new IntegerConverter()));
      unknown2 = children.add("unknown2", new DataModel<>(this, new AfterKnownLocationFinder(unknown1, 0), new IntegerConverter()));
      playerId = children.add("Id", new DataModel<>(this, new AfterKnownLocationFinder(unknown2, 0), new IntegerConverter()));
      playerName = children.add("Name", new DataModel<>(this, new AfterKnownLocationFinder(playerId, 0), new FullStringConverter()));
      unknown3 = children.add("unknown3", new DataModel<>(this, new AfterKnownLocationFinder(playerName, 0), new IntegerConverter()));
      unknown4 = children.add("unknown4", new DataModel<>(this, new AfterKnownLocationFinder(unknown3, 0), new IntegerConverter()));
      unknown5 = children.add("unknown5", new DataModel<>(this, new AfterKnownLocationFinder(unknown4, 0), new IntegerConverter()));
      unknown6 = children.add("unknown6", new DataModel<>(this, new AfterKnownLocationFinder(unknown5, 0), new IntegerConverter()));
   }
   
   public DataModel<Integer> getUnknown1() {
      return unknown1;
   }
   
   public DataModel<Integer> getUnknown2() {
      return unknown2;
   }
   
   public DataModel<Integer> getPlayerId() {
      return playerId;
   }
   
   public DataModel<String> getPlayerName() {
      return playerName;
   }
   
   public DataModel<Integer> getUnknown3() {
      return unknown3;
   }
   
   public DataModel<Integer> getUnknown4() {
      return unknown4;
   }
   
   public DataModel<Integer> getUnknown5() {
      return unknown5;
   }
   
   public DataModel<Integer> getUnknown6() {
      return unknown6;
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) {
      unknown1.readAllModels(data, offsetHint);
      unknown2.readAllModels(data, offsetHint);
      playerId.readAllModels(data, offsetHint);
      playerName.readAllModels(data, offsetHint);
      unknown3.readAllModels(data, offsetHint);
      unknown4.readAllModels(data, offsetHint);
      unknown5.readAllModels(data, offsetHint);
      unknown6.readAllModels(data, offsetHint);
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) {
      unknown6.writeAllModels(data, offsetHint);
      unknown5.writeAllModels(data, offsetHint);
      unknown4.writeAllModels(data, offsetHint);
      unknown3.writeAllModels(data, offsetHint);
      playerName.writeAllModels(data, offsetHint);
      playerId.writeAllModels(data, offsetHint);
      unknown2.writeAllModels(data, offsetHint);
      unknown1.writeAllModels(data, offsetHint);
   }
   
}
