
package mapmodel.multiplayeroverride;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import datahandler.DataModel;
import datahandler.converter.ByteConverter;
import datahandler.converter.FixedDataChunkConverter;
import datahandler.converter.FloatConverter;
import datahandler.converter.FullStringConverter;
import datahandler.converter.IntegerConverter;
import datahandler.location.AfterKnownLocationFinder;
import datahandler.location.LocationNotFoundException;
import datahandler.location.RelativeLocationFinder;
import mapmodel.BranchModel;
import mapmodel.ParentModel;
import mapmodel.RootModel;

public class MultiplayerOverrideDataModel extends BranchModel {
   
   private static final int LENGTH_WITH_EMPTY_STRINGS = 339;
   
   private static final List<Byte> INITIAL_FIXED_DATA = Arrays.asList((byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x00);
   
   private static final int STORED_PLAYERS = 17;
   public static final int SUPPORTED_PLAYERS = 13;
   
   private static final List<Byte> TERNARY_FIXED_DATA = Arrays.asList((byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00);
   
   private static final List<Byte> QUATERNARY_FIXED_DATA = Arrays.asList((byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0f, (byte) 0x00, (byte) 0x00, (byte) 0x00);
   
   private static final int STORED_NON_GAIA_PLAYERS = STORED_PLAYERS - 1;
   public static final int SUPPORTED_NON_GAIA_PLAYERS = SUPPORTED_PLAYERS - 1;
   
   private static final List<Byte> NEAR_FINAL_FIXED_DATA = Arrays.asList((byte) 0x00, (byte) 0x00, (byte) 0x00);
   
   private static final List<Byte> FINAL_FIXED_DATA = Arrays.asList((byte) 0x01, (byte) 0x00, (byte) 0x00);
   
   private RootModel rootModel;
   
   private DataModel<List<Byte>> initialFixedData;
   
   private DataModel<Integer> playerNumber;
   
   private List<DataModel<Byte>> playerTypes;
   private List<DataModel<Byte>> unknowns1;
   private List<DataModel<Byte>> unknowns2;
   private List<DataModel<Byte>> playerGods;
   
   private DataModel<List<Byte>> ternaryFixedData;
   private DataModel<Byte> gameType;
   private DataModel<List<Byte>> quaternaryFixedData;
   
   private List<DataModel<String>> playerNames;
   private List<DataModel<Float>> playerRatings;
   private List<DataModel<Float>> playerHandicaps;
   private List<DataModel<Byte>> playerIds;
   private List<DataModel<Byte>> playerTeams;
   
   private DataModel<String> lobbyName;
   private DataModel<Byte> unknownGlobal0;
   private DataModel<Byte> flags;
   
   private DataModel<List<Byte>> nearFinalFixedData;
   private DataModel<Byte> pauseLimit;
   private DataModel<Byte> unknownGlobal1;
   private DataModel<Byte> mapVisibility;
   private DataModel<List<Byte>> finalFixedData;
   
   public MultiplayerOverrideDataModel(ParentModel parent, RootModel rootModel) {
      super(parent);
      this.rootModel = rootModel;
      
      initialFixedData = new DataModel<>(this, new RelativeLocationFinder(0), new FixedDataChunkConverter(INITIAL_FIXED_DATA));
      DataModel<?> lastDataModel = initialFixedData;
      
      playerTypes = new ArrayList<>(STORED_PLAYERS);
      unknowns1 = new ArrayList<>(STORED_PLAYERS);
      unknowns2 = new ArrayList<>(STORED_PLAYERS);
      playerGods = new ArrayList<>(STORED_PLAYERS);
      for (int i = 0; i < STORED_PLAYERS; i++) {
         DataModel<Byte> playerType =
               children.add("playerType" + i, new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new ByteConverter()));
         playerTypes.add(playerType);
         lastDataModel = playerType;
         
         DataModel<Byte> unknown1 =
               children.add("unknown1Player" + i, new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new ByteConverter()));
         unknowns1.add(unknown1);
         lastDataModel = unknown1;
         
         DataModel<Byte> unknown2 =
               children.add("unknown2Player" + i, new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new ByteConverter()));
         unknowns2.add(unknown2);
         lastDataModel = unknown2;
         
         DataModel<Byte> playerGod =
               children.add("god" + i, new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new ByteConverter()));
         playerGods.add(playerGod);
         lastDataModel = playerGod;
      }
      
      playerNumber = new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new IntegerConverter());
      ternaryFixedData = new DataModel<>(this, new AfterKnownLocationFinder(playerNumber, 0), new FixedDataChunkConverter(TERNARY_FIXED_DATA));
      gameType = children.add("gameType", new DataModel<>(this, new AfterKnownLocationFinder(ternaryFixedData, 0), new ByteConverter()));
      quaternaryFixedData = new DataModel<>(this, new AfterKnownLocationFinder(gameType, 0), new FixedDataChunkConverter(QUATERNARY_FIXED_DATA));
      
      playerNames = new ArrayList<>(STORED_NON_GAIA_PLAYERS);
      playerRatings = new ArrayList<>(STORED_NON_GAIA_PLAYERS);
      playerHandicaps = new ArrayList<>(STORED_NON_GAIA_PLAYERS);
      playerIds = new ArrayList<>(STORED_NON_GAIA_PLAYERS);
      playerTeams = new ArrayList<>(STORED_NON_GAIA_PLAYERS);
      
      lastDataModel = quaternaryFixedData;
      for (int i = 0; i < STORED_NON_GAIA_PLAYERS; i++) {
         DataModel<String> playerName =
               children.add("playerName" + (i + 1), new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new FullStringConverter()));
         playerNames.add(playerName);
         lastDataModel = playerName;
         
         DataModel<Float> playerRating =
               children.add("playerRating" + (i + 1), new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new FloatConverter()));
         playerRatings.add(playerRating);
         lastDataModel = playerRating;
         
         DataModel<Float> playerHandicap =
               children.add("playerHandicap" + (i + 1), new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new FloatConverter()));
         playerHandicaps.add(playerHandicap);
         lastDataModel = playerHandicap;
         
         DataModel<Byte> playerId =
               children.add("playerId" + (i + 1), new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new ByteConverter()));
         playerIds.add(playerId);
         lastDataModel = playerId;
         
         DataModel<Byte> playerTeam =
               children.add("playerTeam" + (i + 1), new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new ByteConverter()));
         playerTeams.add(playerTeam);
         lastDataModel = playerTeam;
      }
      
      lobbyName = children.add("lobbyName", new DataModel<>(this, new AfterKnownLocationFinder(lastDataModel, 0), new FullStringConverter()));
      unknownGlobal0 = children.add("unknownGlobal0", new DataModel<>(this, new AfterKnownLocationFinder(lobbyName, 0), new ByteConverter()));
      //1 Shared resources, 2 Shared population, 4 Locked teams, 8 Enable cheats
      flags = children.add("flags", new DataModel<>(this, new AfterKnownLocationFinder(unknownGlobal0, 0), new ByteConverter()));
      
      nearFinalFixedData = new DataModel<>(this, new AfterKnownLocationFinder(flags, 0), new FixedDataChunkConverter(NEAR_FINAL_FIXED_DATA));
      pauseLimit = children.add("pauseLimit", new DataModel<>(this, new AfterKnownLocationFinder(nearFinalFixedData, 0), new ByteConverter()));
      unknownGlobal1 = children.add("unknownGlobal1", new DataModel<>(this, new AfterKnownLocationFinder(pauseLimit, 0), new ByteConverter()));
      mapVisibility = children.add("mapVisibility", new DataModel<>(this, new AfterKnownLocationFinder(unknownGlobal1, 0), new ByteConverter()));
      finalFixedData = new DataModel<>(this, new AfterKnownLocationFinder(mapVisibility, 0), new FixedDataChunkConverter(FINAL_FIXED_DATA));
   }
   
   public List<DataModel<Byte>> getPlayerTypes() {
      return playerTypes;
   }
   
   public List<DataModel<Byte>> getUnknowns1() {
      return unknowns1;
   }
   
   public List<DataModel<Byte>> getUnknowns2() {
      return unknowns2;
   }
   
   public List<DataModel<Byte>> getPlayerGods() {
      return playerGods;
   }
   
   public DataModel<Byte> getGameType() {
      return gameType;
   }
   
   public List<DataModel<String>> getPlayerNames() {
      return playerNames;
   }
   
   public List<DataModel<Float>> getPlayerRatings() {
      return playerRatings;
   }
   
   public List<DataModel<Float>> getPlayerHandicaps() {
      return playerHandicaps;
   }
   
   public List<DataModel<Byte>> getPlayerIds() {
      return playerIds;
   }
   
   public List<DataModel<Byte>> getPlayerTeams() {
      return playerTeams;
   }
   
   public DataModel<String> getLobbyName() {
      return lobbyName;
   }
   
   public DataModel<Byte> getUnknown() {
      return unknownGlobal0;
   }
   
   public DataModel<Byte> getFlags() {
      return flags;
   }
   
   public DataModel<Byte> getPauseLimit() {
      return pauseLimit;
   }
   
   public DataModel<Byte> getMapVisibility() {
      return mapVisibility;
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      for (int i = 0; i < STORED_PLAYERS; i++) {
         playerTypes.get(i).readAllModels(data, offsetHint);
         unknowns1.get(i).readAllModels(data, offsetHint);
         unknowns2.get(i).readAllModels(data, offsetHint);
         playerGods.get(i).readAllModels(data, offsetHint);
      }
      
      gameType.readAllModels(data, offsetHint);
      
      for (int i = 0; i < STORED_NON_GAIA_PLAYERS; i++) {
         playerNames.get(i).readAllModels(data, offsetHint);
         playerRatings.get(i).readAllModels(data, offsetHint);
         playerHandicaps.get(i).readAllModels(data, offsetHint);
         playerIds.get(i).readAllModels(data, offsetHint);
         playerTeams.get(i).readAllModels(data, offsetHint);
      }
      
      lobbyName.readAllModels(data, offsetHint);
      unknownGlobal0.readAllModels(data, offsetHint);
      flags.readAllModels(data, offsetHint);
      
      pauseLimit.readAllModels(data, offsetHint);
      unknownGlobal1.readAllModels(data, offsetHint);
      mapVisibility.readAllModels(data, offsetHint);
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      initialFixedData.writeAllModels(data, offsetHint);
      
      for (int i = 0; i < STORED_PLAYERS; i++) {
         playerTypes.get(i).writeAllModels(data, offsetHint);
         unknowns1.get(i).writeAllModels(data, offsetHint);
         unknowns2.get(i).writeAllModels(data, offsetHint);
         playerGods.get(i).writeAllModels(data, offsetHint);
      }
      
      playerNumber.setValue(rootModel.getAllPlayersModel().getPlayerNumber().getValue() - 1);
      playerNumber.writeAllModels(data, offsetHint);
      
      ternaryFixedData.writeAllModels(data, offsetHint);
      gameType.writeAllModels(data, offsetHint);
      quaternaryFixedData.writeAllModels(data, offsetHint);
      
      for (int i = 0; i < STORED_NON_GAIA_PLAYERS; i++) {
         playerNames.get(i).writeAllModels(data, offsetHint);
         playerRatings.get(i).writeAllModels(data, offsetHint);
         playerHandicaps.get(i).writeAllModels(data, offsetHint);
         playerIds.get(i).writeAllModels(data, offsetHint);
         playerTeams.get(i).writeAllModels(data, offsetHint);
      }
      
      lobbyName.writeAllModels(data, offsetHint);
      unknownGlobal0.writeAllModels(data, offsetHint);
      flags.writeAllModels(data, offsetHint);
      
      nearFinalFixedData.writeAllModels(data, offsetHint);
      pauseLimit.writeAllModels(data, offsetHint);
      unknownGlobal1.writeAllModels(data, offsetHint);
      mapVisibility.writeAllModels(data, offsetHint);
      finalFixedData.writeAllModels(data, offsetHint);
   }
   
   public int getInitialLength() {
      return LENGTH_WITH_EMPTY_STRINGS;
   }
   
}
