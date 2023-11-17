
package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import editor.EditorContext;
import mapmodel.unit.UnitModel;

public class TriggerWriter {
   
   public void writeUnits(EditorContext editor, String filename) throws IOException {
      List<String> output = new ArrayList<>();
      for (UnitModel unitModel : editor.getRootModel().getAllUnitsModel().getUnitModels().getChildModels()) {
         //unitModel.getUnitPlayer().getValue()
         output.add("trArmyDispatch(\"" + unitModel.getUnitPlayer().getValue() + ",0\", \"Dwarf\", 1, 1, 0, 1, 0, true);");
         output.add("trArmySelect(\"" + unitModel.getUnitPlayer().getValue() + ",0\");");
         output.add("trUnitTeleport(" + unitModel.getPosX().getValue() + "," + unitModel.getPosY().getValue() + "," + unitModel.getPosZ().getValue()
               + ");");
         output.add("trMutateSelected(" + unitModel.getUnitType().getValue() + ");");
         output.add("trSetUnitOrientation(vector(" + unitModel.getmXX().getValue() + "," + unitModel.getmXY().getValue() + ","
               + unitModel.getmXZ().getValue() + ", vector(" + unitModel.getmYX().getValue() + "," + unitModel.getmYY().getValue() + ","
               + unitModel.getmYZ().getValue() + "), true));");
         output.add("trSetSelectedScale(" + unitModel.getScaleX().getValue() + "," + unitModel.getScaleY().getValue() + ","
               + unitModel.getScaleZ().getValue() + ");");
      }
      File triggersFile = new File(filename);
      triggersFile.createNewFile();
      FileWriter nickonhawk = new FileWriter(filename);
      for (String out : output) {
         nickonhawk.write(out);
      }
      nickonhawk.close();
   }
   
}
