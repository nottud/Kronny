
package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import editor.EditorContext;
import mapmodel.map.MapSizeModel;
import mapmodel.unit.UnitModel;

public class TriggerWriter {
   
   public void writeUnits(EditorContext editor, String filename) throws IOException {
      List<String> output = new ArrayList<>();
      DecimalFormat df = new DecimalFormat("#.###");
      
      // Units
      for (UnitModel unitModel : editor.getRootModel().getAllUnitsModel().getUnitModels().getChildModels()) {
         //unitModel.getUnitPlayer().getValue()
         output.add("trArmyDispatch(\"" + unitModel.getUnitPlayer().getValue() + ",0\", \"Militia\", 1, 1, 0, 1, 0, true);");
         output.add("trArmySelect(\"" + unitModel.getUnitPlayer().getValue() + ",0\");");
         output.add("trUnitTeleport(" + df.format(unitModel.getPosX().getValue()) + "," + df.format(unitModel.getPosY().getValue()) + ","
               + df.format(unitModel.getPosZ().getValue())
               + ");");
         output.add("trMutateSelected(" + unitModel.getUnitType().getValue() + ");");
         output.add("trSetUnitOrientation(vector(" + df.format(unitModel.getmXX().getValue()) + "," + df.format(unitModel.getmXY().getValue()) + ","
               + df.format(unitModel.getmXZ().getValue()) + "), vector(" + df.format(unitModel.getmYX().getValue()) + ","
               + df.format(unitModel.getmYY().getValue()) + ","
               + df.format(unitModel.getmYZ().getValue()) + "), true);");
         output.add("trSetSelectedScale(" + df.format(unitModel.getScaleX().getValue()) + "," + df.format(unitModel.getScaleY().getValue()) + ","
               + df.format(unitModel.getScaleZ().getValue()) + ");");
         output.add("trUnitOverrideAnimation(-1,0,false,true, -1);");
      }
      
      // Terrain paint
      MapSizeModel mapSizeModel = editor.getRootModel().getMapSizeModel();
      for (int x = 0; x < mapSizeModel.getMapSizeX().getValue(); x++) {
         for (int z = 0; z < mapSizeModel.getMapSizeZ().getValue(); z++) {
            int index = x * mapSizeModel.getMapSizeZ().getValue() + z;
            Byte groupType = mapSizeModel.getTerrainGroup().getChildModels().get(index).getValue();
            Byte terrainType = mapSizeModel.getTerrainData().getChildModels().get(index).getValue();
            output.add(
                  "trPaintTerrain(" + x + "," + z + "," + x + "," + z + "," + groupType.intValue() + ", " + terrainType.intValue() + ", false);");
         }
      }
      
      for (int x = 0; x <= mapSizeModel.getMapSizeX().getValue(); x++) {
         for (int z = 0; z <= mapSizeModel.getMapSizeZ().getValue(); z++) {
            int index = x * (mapSizeModel.getMapSizeZ().getValue() + 1) + z;
            float terrainHeight = mapSizeModel.getTerrainHeight().getChildModels().get(index).getValue();
            output.add("trChangeTerrainHeight(" + x + "," + z + "," + x + "," + z + "," + df.format(terrainHeight) + ", false);");
         }
      }
      // Write file
      File triggersFile = new File(filename);
      triggersFile.createNewFile();
      FileWriter nickonhawk = new FileWriter(filename);
      for (String out : output) {
         nickonhawk.write(out + "\n");
      }
      nickonhawk.close();
   }
   
}
