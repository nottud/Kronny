
package editor.tool.models;

import editor.terrainchooser.TerrainSelectionModel;
import editor.tool.elevation.ElevationHeightModel;
import editor.tool.elevation.ElevationRangeModel;

public class TerrainModels {
   
   private TerrainSelectionModel terrainSelectionModel;
   private ElevationRangeModel elevationRangeModel;
   private ElevationHeightModel elevationHeightModel;
   
   public TerrainModels() {
      terrainSelectionModel = new TerrainSelectionModel();
      elevationRangeModel = new ElevationRangeModel();
      elevationHeightModel = new ElevationHeightModel();
   }
   
   public TerrainSelectionModel getTerrainSelectionModel() {
      return terrainSelectionModel;
   }
   
   public ElevationRangeModel getElevationRangeModel() {
      return elevationRangeModel;
   }
   
   public ElevationHeightModel getElevationHeightModel() {
      return elevationHeightModel;
   }
}
