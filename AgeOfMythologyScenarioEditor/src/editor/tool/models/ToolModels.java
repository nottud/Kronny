
package editor.tool.models;

import editor.brush.BrushModelHolder;

public class ToolModels {
   
   private BrushModelHolder brushModelHolder;
   private TerrainModels terrainModels;
   private WaterModels waterModels;
   private UnitModels unitModels;
   
   public ToolModels() {
      brushModelHolder = new BrushModelHolder();
      terrainModels = new TerrainModels();
      waterModels = new WaterModels();
      unitModels = new UnitModels();
   }
   
   public BrushModelHolder getBrushModelHolder() {
      return brushModelHolder;
   }
   
   public TerrainModels getTerrainModels() {
      return terrainModels;
   }
   
   public WaterModels getWaterModels() {
      return waterModels;
   }
   
   public UnitModels getUnitModels() {
      return unitModels;
   }
   
}
