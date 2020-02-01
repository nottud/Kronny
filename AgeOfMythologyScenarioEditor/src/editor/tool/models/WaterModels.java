
package editor.tool.models;

import editor.colourchooser.ColourSelectionModel;
import editor.tool.elevation.ElevationHeightModel;
import editor.tool.elevation.ElevationRangeModel;
import editor.waterchooser.WaterSelectionModel;

public class WaterModels {
   
   private ElevationRangeModel elevationRangeModel;
   private ElevationHeightModel elevationHeightModel;
   private WaterSelectionModel waterSelectionModel;
   private ColourSelectionModel colourSelectionModel;
   
   public WaterModels() {
      elevationRangeModel = new ElevationRangeModel();
      elevationHeightModel = new ElevationHeightModel();
      waterSelectionModel = new WaterSelectionModel();
      colourSelectionModel = new ColourSelectionModel();
   }
   
   public ElevationRangeModel getElevationRangeModel() {
      return elevationRangeModel;
   }
   
   public ElevationHeightModel getElevationHeightModel() {
      return elevationHeightModel;
   }
   
   public WaterSelectionModel getWaterSelectionModel() {
      return waterSelectionModel;
   }
   
   public ColourSelectionModel getColourSelectionModel() {
      return colourSelectionModel;
   }
   
}
