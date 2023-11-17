
package io;

import editor.EditorContext;
import mapmodel.unit.UnitModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class TriggerWriter {
   
   public void writeUnits(EditorContext editor, String filename) throws IOException {
      List<String> output = new List<String>();
      for (UnitModel unitModel : editorContext.getRootModel().getAllUnitsModel().getUnitModels().getChildModels()) {
         //unitModel.getUnitPlayer().getValue()
         output.append("trArmyDispatch(\"" + unitModel.getUnitPlayer().getValue() + ",0\", \"Dwarf\", 1, 1, 0, 1, 0, true);");
         output.append("trArmySelect(\""+ unitModel.getUnitPlayer().getValue() +",0\");");
         output.append("trUnitTeleport(" + unitModel.getPosX().getValue() + "," + unitModel.getPosY().getValue() + "," + unitModel.getPosZ().getValue() +");");
         output.append("trMutateSelected("+ unitModel.getUnitType().getValue() +");");
         output.append("trSetUnitOrientation(vector(" + unitModel.getmXX().getValue() + "," + unitModel.getmXY().getValue() + "," + unitModel.getmXZ().getValue() + ", vector("+ unitModel.getmYX().getValue() +","+ unitModel.getmYY().getValue() +","+ unitModel.getmYZ().getValue() +"), true));");
         output.append("trSetSelectedScale("+unitModel.getScaleX().getValue()+","+unitModel.getScaleY().getValue()+","+unitModel.getScaleZ().getValue()+");");
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
