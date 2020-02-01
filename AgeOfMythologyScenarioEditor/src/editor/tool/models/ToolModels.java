
package editor.tool.models;

import editor.brush.BrushModelHolder;

public class ToolModels {
   
   private BrushModelHolder brushModelHolder;
   private TerrainModels terrainModels;
   private WaterModels waterModels;
   
   public ToolModels() {
      brushModelHolder = new BrushModelHolder();
      terrainModels = new TerrainModels();
      waterModels = new WaterModels();
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
   
}
