
package gameenumeration.terrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TerrainType {
   
   private static final TerrainType INSTANCE = new TerrainType();
   
   private int groupIndex = 0;
   private int index = 0;
   
   private String currentGroupName;
   private List<TerrainEntry> currentTerrainEntries;
   
   private List<TerrainGroup> terrainGroups;
   private List<TerrainEntry> terrains;
   
   private TerrainType() {
      terrainGroups = new ArrayList<>();
      terrains = new ArrayList<>();
      
      startGroup("Normal");
      addEntry("GrassA", "grassa.bmp");
      addEntry("GrassB", "grassb.bmp");
      addEntry("GrassDirt25", "grassdirt25.bmp");
      addEntry("GrassDirt50", "grassdirt50.bmp");
      addEntry("GrassDirt75", "grassdirt75.bmp");
      addEntry("CliffGreekB", "cliffgreekb.bmp");
      addEntry("GreekRoadBurnt", "greekroad burnt.bmp");
      addEntry("GreekRoadBurntB", "greekroad burntb.bmp");
      addEntry("GaiaCreepA", "gaiacreepa.bmp");
      addEntry("GaiaCreepASnow", "gaiacreepasnow.bmp");
      addEntry("GaiaCreepASand", "gaiacreepasand.bmp");
      addEntry("GaiaCreepB", "gaiacreepb.bmp");
      addEntry("GaiaCreepBorder", "gaiacreepborder.bmp");
      addEntry("GaiaCreepBorderSnow", "gaiacreepbordersnow.bmp");
      addEntry("GaiaCreepBorderSand", "gaiacreepbordersand.bmp");
      addEntry("SavannahA", "savannaha.bmp");
      addEntry("SavannahB", "savannahb.bmp");
      addEntry("SavannahC", "savannahc.bmp");
      addEntry("SavannahD", "savannahd.bmp");
      addEntry("JungleA", "junglea.bmp");
      addEntry("JungleB", "jungleb.bmp");
      addEntry("JungleDirt25", "jungledirt25.bmp");
      addEntry("JungleDirt50", "jungledirt50.bmp");
      addEntry("JungleDirt75", "jungledirt75.bmp");
      addEntry("CliffJungleB", "cliffjungleb.bmp");
      addEntry("PlainA", "plaina.bmp");
      addEntry("PlainB", "plainb.bmp");
      addEntry("PlainDirt25", "plaindirt25.bmp");
      addEntry("PlainDirt50", "plaindirt50.bmp");
      addEntry("PlainDirt75", "plaindirt75.bmp");
      addEntry("CliffPlainB", "cliffplainb.bmp");
      addEntry("DirtA", "dirta.bmp");
      addEntry("DirtB", "dirtb.bmp");
      addEntry("DirtC", "dirtc.bmp");
      addEntry("SandA", "sanda.bmp");
      addEntry("SandB", "sandb.bmp");
      addEntry("SandC", "sandc.bmp");
      addEntry("SandD", "sandd.bmp");
      addEntry("CliffEgyptianB", "cliffegyptianb.bmp");
      addEntry("SandDirt50", "sanddirt50.bmp");
      addEntry("SandDirt50b", "sanddirt50b.bmp");
      addEntry("SnowA", "snowa.bmp");
      addEntry("SnowB", "snowb.bmp");
      addEntry("SnowGrass25", "snowgrass25.bmp");
      addEntry("SnowGrass50", "snowgrass50.bmp");
      addEntry("SnowGrass75", "snowgrass75.bmp");
      addEntry("SnowSand25", "snowsand25.bmp");
      addEntry("SnowSand50", "snowsand50.bmp");
      addEntry("SnowSand75", "snowsand75.bmp");
      addEntry("CliffNorseB", "cliffnorseb.bmp");
      addEntry("OlympusA", "olympusa.bmp");
      addEntry("OlympusB", "olympusb.bmp");
      addEntry("OlympusC", "olympusc.bmp");
      addEntry("OlympusTile", "olympustile.bmp");
      addEntry("TundraGrassA", "tundragrassa.bmp");
      addEntry("TundraGrassB", "tundragrassb.bmp");
      addEntry("TundraRockA", "tundrarocka.bmp");
      addEntry("TundraRockB", "tundrarockb.bmp");
      addEntry("MarshA", "marsha.bmp");
      addEntry("MarshB", "marshb.bmp");
      addEntry("MarshC", "marshc.bmp");
      addEntry("MarshD", "marshd.bmp");
      addEntry("MarshE", "marshe.bmp");
      addEntry("MarshF", "marshf.bmp");
      addEntry("EgyptianRoadA", "egyptianroada.bmp");
      addEntry("GreekRoadA", "greekroada.bmp");
      addEntry("NorseRoadA", "norseroada.bmp");
      addEntry("JungleRoadA", "jungleroada.bmp");
      addEntry("PlainRoadA", "plainroada.bmp");
      addEntry("TundraRoadA", "tundraroada.bmp");
      addEntry("CityTileA", "citytilea.bmp");
      addEntry("CityTileAtlantis", "citytileatlantis.bmp");
      addEntry("CityTileAtlantiscoral", "citytileatlantiscoral.bmp");
      addEntry("CityTileWaterPool", "citytilewaterpool.bmp");
      addEntry("CityTileWaterEdgeA", "citytilewateredgea.bmp");
      addEntry("CityTileWaterEdgeB", "citytilewateredgeb.bmp");
      addEntry("CityTileWaterEdgeEndA", "citytilewateredgeenda.bmp");
      addEntry("CityTileWaterEdgeEndB", "citytilewateredgeendb.bmp");
      addEntry("CityTileWaterEdgeEndC", "citytilewateredgeendc.bmp");
      addEntry("CityTileWaterEdgeEndD", "citytilewateredgeendd.bmp");
      addEntry("CityTileWaterCornerA", "citytilewatercornera.bmp");
      addEntry("CityTileWaterCornerB", "citytilewatercornerb.bmp");
      addEntry("CityTileWaterCornerC", "citytilewatercornerc.bmp");
      addEntry("CityTileWaterCornerD", "citytilewatercornerd.bmp");
      addEntry("Hadesbuildable1", "hadesbuildable1.bmp");
      addEntry("Hadesbuildable2", "hadesbuildable2.bmp");
      addEntry("ForestfloorPalm", "forestfloorpalm.bmp");
      addEntry("ForestfloorPine", "forestfloorpine.bmp");
      addEntry("ForestfloorPineSnow", "forestfloorpinesnow.bmp");
      addEntry("ForestfloorOak", "forestflooroak.bmp");
      addEntry("ForestfloorGaia", "forestfloorgaia.bmp");
      addEntry("ForestfloorSavannah", "forestfloorsavannah.bmp");
      addEntry("ForestfloorDeadPine", "forestfloordeadpine.bmp");
      addEntry("ForestfloorTundra", "forestfloortundra.bmp");
      addEntry("ForestfloorMarsh", "forestfloormarsh.bmp");
      addEntry("ForestfloorJungle", "forestfloorjungle.bmp");
      endGroup();
      
      startGroup("Water");
      addEntry("Water", "testwaterbh.bmp");
      endGroup();
      
      startGroup("Impassable");
      addEntry("CliffA", "cliffa.bmp");
      addEntry("CliffGreekA", "cliffgreeka.bmp");
      addEntry("CliffEgyptianA", "cliffegyptiana.bmp");
      addEntry("CliffNorseA", "cliffnorsea.bmp");
      addEntry("CliffJungleA", "cliffjunglea.bmp");
      addEntry("CliffPlainA", "cliffplaina.bmp");
      addEntry("Dam", "dam.bmp");
      addEntry("Hades3", "hades3.bmp");
      addEntry("Hades5", "hades5.bmp");
      addEntry("Hades6", "hades6.bmp");
      addEntry("Hades7", "hades7.bmp");
      addEntry("HadesCliff", "hadescliff.bmp");
      addEntry("Hades4", "hades4.bmp");
      addEntry("BlackRock", "blackrock.bmp");
      endGroup();
      
      startGroup("Underwater");
      addEntry("UnderwaterRockA", "underwaterrocka.bmp");
      addEntry("UnderwaterRockB", "underwaterrockb.bmp");
      addEntry("UnderwaterRockC", "underwaterrockc.bmp");
      addEntry("UnderwaterRockD", "underwaterrockd.bmp");
      addEntry("UnderwaterRockE", "underwaterrocke.bmp");
      addEntry("UnderwaterRockF", "underwaterrockf.bmp");
      addEntry("UnderwaterIceA", "underwatericea.bmp");
      addEntry("UnderwaterIceB", "underwatericeb.bmp");
      addEntry("UnderwaterIceC", "underwatericec.bmp");
      addEntry("coralA", "corala.bmp");
      addEntry("coralB", "coralb.bmp");
      addEntry("coralC", "coralc.bmp");
      addEntry("coralC2", "coralc2.bmp");
      addEntry("coralD", "corald.bmp");
      addEntry("coralE", "corale.bmp");
      addEntry("coralF", "coralf.bmp");
      endGroup();
      
      startGroup("Shoreline");
      addEntry("ShorelineSandA", "shorelinesanda.bmp");
      addEntry("ShorelineAegeanA", "shorelineaegeana.bmp");
      addEntry("ShorelineAegeanB", "shorelineaegeanb.bmp");
      addEntry("ShorelineAegeanC", "shorelineaegeanc.bmp");
      addEntry("ShorelineRedSeaA", "shorelineredseaa.bmp");
      addEntry("ShorelineRedSeaB", "shorelineredseab.bmp");
      addEntry("ShorelineRedSeaC", "shorelineredseac.bmp");
      addEntry("ShorelineNorwegianA", "shorelinenorwegiana.bmp");
      addEntry("ShorelineNorwegianB", "shorelinenorwegianb.bmp");
      addEntry("ShorelineNorwegianC", "shorelinenorwegianc.bmp");
      addEntry("ShorelineMediterraneanA", "shorelinemediterraneana.bmp");
      addEntry("ShorelineMediterraneanB", "shorelinemediterraneanb.bmp");
      addEntry("ShorelineMediterraneanC", "shorelinemediterraneanc.bmp");
      addEntry("ShorelineMediterraneanD", "shorelinemediterraneand.bmp");
      addEntry("ShorelineAtlanticA", "shorelineatlantica.bmp");
      addEntry("ShorelineAtlanticB", "shorelineatlanticb.bmp");
      addEntry("ShorelineAtlanticC", "shorelineatlanticc.bmp");
      addEntry("ShorelineTundraA", "shorelinetundraa.bmp");
      addEntry("ShorelineTundraB", "shorelinetundrab.bmp");
      addEntry("ShorelineTundraC", "shorelinetundrac.bmp");
      addEntry("ShorelineTundraD", "shorelinetundrad.bmp");
      addEntry("ShorelineJungleA", "shorelinejunglea.bmp");
      addEntry("ShorelineJungleB", "shorelinejungleb.bmp");
      addEntry("ShorelineJungleC", "shorelinejunglec.bmp");
      addEntry("ShorelinePlainA", "shorelineplaina.bmp");
      addEntry("ShorelinePlainB", "shorelineplainb.bmp");
      addEntry("ShorelinePlainC", "shorelineplainc.bmp");
      addEntry("ShorelinePlainD", "shorelineplaind.bmp");
      addEntry("RiverSandyA", "riversandya.bmp");
      addEntry("RiverSandyB", "riversandyb.bmp");
      addEntry("RiverSandyC", "riversandyc.bmp");
      addEntry("RiverSandyShallowA", "riversandyshallowa.bmp");
      addEntry("RiverGrassyA", "rivergrassya.bmp");
      addEntry("RiverGrassyB", "rivergrassyb.bmp");
      addEntry("RiverGrassyC", "rivergrassyc.bmp");
      addEntry("RiverIcyA", "rivericya.bmp");
      addEntry("RiverIcyB", "rivericyb.bmp");
      addEntry("RiverIcyC", "rivericyc.bmp");
      addEntry("RiverMarshA", "rivermarsha.bmp");
      addEntry("RiverMarshB", "rivermarshb.bmp");
      addEntry("RiverMarshC", "rivermarshc.bmp");
      endGroup();
      
      startGroup("Unbuildable");
      addEntry("IceA", "icea.bmp");
      addEntry("IceB", "iceb.bmp");
      addEntry("IceC", "icec.bmp");
      addEntry("MiningGround", "miningground.bmp");
      addEntry("black", "black.bmp");
      addEntry("Hades1", "hades1.bmp");
      addEntry("Hades2", "hades2.bmp");
      addEntry("Hades4Passable", "hades4.bmp");
      addEntry("Hades8", "hades8.bmp");
      addEntry("Hades9", "hades9.bmp");
      endGroup();
      
      terrainGroups = Collections.unmodifiableList(terrainGroups);
      terrains = Collections.unmodifiableList(terrains);
   }
   
   public static TerrainType getInstance() {
      return INSTANCE;
   }
   
   private void startGroup(String groupName) {
      currentGroupName = groupName;
      currentTerrainEntries = new ArrayList<>();
      index = 0;
   }
   
   private void endGroup() {
      terrainGroups.add(new TerrainGroup(groupIndex, currentGroupName, Collections.unmodifiableList(currentTerrainEntries)));
      groupIndex++;
   }
   
   private void addEntry(String name, String file) {
      TerrainEntry terrainEntry = new TerrainEntry(groupIndex, index, name, file);
      currentTerrainEntries.add(terrainEntry);
      terrains.add(terrainEntry);
      index++;
   }
   
   public List<TerrainGroup> getTerrainGroups() {
      return terrainGroups;
   }
   
   public List<TerrainEntry> getTerrains() {
      return terrains;
   }
}
