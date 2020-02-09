
package mapmodel.player;

import java.util.List;

import datahandler.DataModel;
import datahandler.converter.FloatConverter;
import datahandler.converter.FullStringConverter;
import datahandler.converter.IntegerConverter;
import datahandler.lengthdependency.LengthDependency;
import datahandler.location.AfterKnownLocationFinder;
import datahandler.location.LocationNotFoundException;
import datahandler.location.RelativeLocationFinder;
import mapmodel.BranchModel;
import mapmodel.ParentModel;

public class PlayerModel extends BranchModel {
   
   private DataModel<Integer> lengthFlag;
   private DataModel<Integer> unknown2;
   private DataModel<Integer> playerId;
   private DataModel<String> playerName;
   private DataModel<Integer> playerGod;
   private DataModel<Integer> playerColour;
   private DataModel<Float> playerGold;
   private DataModel<Float> playerWood;
   private DataModel<Float> playerFood;
   private DataModel<Float> playerPop;
   
   private LengthDependency<String> nameLengthDependency;
   
   public PlayerModel(ParentModel parentModel) {
      super(parentModel);
      lengthFlag = children.add("DataLength", new DataModel<>(this, new RelativeLocationFinder(0), new IntegerConverter()));
      unknown2 = children.add("unknown2", new DataModel<>(this, new AfterKnownLocationFinder(lengthFlag, 0), new IntegerConverter()));
      playerId = children.add("Id", new DataModel<>(this, new AfterKnownLocationFinder(unknown2, 0), new IntegerConverter()));
      playerName = children.add("Name", new DataModel<>(this, new AfterKnownLocationFinder(playerId, 0), new FullStringConverter()));
      playerGod = children.add("God", new DataModel<>(this, new AfterKnownLocationFinder(playerName, 18), new IntegerConverter()));
      playerColour = children.add("PlayerColour", new DataModel<>(this, new AfterKnownLocationFinder(playerGod, 8), new IntegerConverter()));
      playerGold = children.add("PlayerGold", new DataModel<>(this, new AfterKnownLocationFinder(playerColour, 92), new FloatConverter()));
      playerWood = children.add("PlayerWood", new DataModel<>(this, new AfterKnownLocationFinder(playerGold, 0), new FloatConverter()));
      playerFood = children.add("PlayerFood", new DataModel<>(this, new AfterKnownLocationFinder(playerWood, 0), new FloatConverter()));
      playerPop = children.add("PlayerPop", new DataModel<>(this, new AfterKnownLocationFinder(playerFood, 0), new FloatConverter()));
      
      nameLengthDependency = new LengthDependency<>(lengthFlag, playerName);
   }
   
   public DataModel<Integer> getLengthFlag() {
      return lengthFlag;
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
   
   public DataModel<Integer> getPlayerGod() {
      return playerGod;
   }
   
   public DataModel<Integer> getPlayerColour() {
      return playerColour;
   }
   
   public DataModel<Float> getPlayerGold() {
      return playerGold;
   }
   
   public DataModel<Float> getPlayerWood() {
      return playerWood;
   }
   
   public DataModel<Float> getPlayerFood() {
      return playerFood;
   }
   
   public DataModel<Float> getPlayerPop() {
      return playerPop;
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      nameLengthDependency.setActive(false);
      
      lengthFlag.readAllModels(data, offsetHint);
      unknown2.readAllModels(data, offsetHint);
      playerId.readAllModels(data, offsetHint);
      playerName.readAllModels(data, offsetHint);
      playerGod.readAllModels(data, offsetHint);
      playerColour.readAllModels(data, offsetHint);
      playerGold.readAllModels(data, offsetHint);
      playerWood.readAllModels(data, offsetHint);
      playerFood.readAllModels(data, offsetHint);
      playerPop.readAllModels(data, offsetHint);
      
      nameLengthDependency.setActive(true);
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      playerPop.writeAllModels(data, offsetHint);
      playerFood.writeAllModels(data, offsetHint);
      playerWood.writeAllModels(data, offsetHint);
      playerGold.writeAllModels(data, offsetHint);
      playerColour.writeAllModels(data, offsetHint);
      playerGod.writeAllModels(data, offsetHint);
      playerName.writeAllModels(data, offsetHint);
      playerId.writeAllModels(data, offsetHint);
      unknown2.writeAllModels(data, offsetHint);
      lengthFlag.writeAllModels(data, offsetHint);
   }
   
}
