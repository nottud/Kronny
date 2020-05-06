
package editor.tool.models;

import editor.unitchooser.UnitSelectionModel;

public class UnitModels {
   
   private UnitSelectionModel unitSelectionModel;
   
   public UnitModels() {
      unitSelectionModel = new UnitSelectionModel();
   }
   
   public UnitSelectionModel getUnitSelectionModel() {
      return unitSelectionModel;
   }
   
}
