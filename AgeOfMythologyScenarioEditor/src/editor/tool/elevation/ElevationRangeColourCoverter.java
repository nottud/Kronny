
package editor.tool.elevation;

import javafx.scene.paint.Color;

public class ElevationRangeColourCoverter {
   
   private static final Color LOWEST_COLOUR_OUTSIDE = Color.BLUE;
   private static final Color LOWEST_COLOUR = Color.BLACK;
   private static final Color HIGHEST_COLOUR = Color.WHITE;
   private static final Color HIGHEST_COLOUR_OUTSIDE = Color.YELLOW;
   
   private ElevationRangeModel model;
   
   public ElevationRangeColourCoverter(ElevationRangeModel model) {
      this.model = model;
   }
   
   public Color getColour(float value) {
      if (value < model.getBottomRange()) {
         return LOWEST_COLOUR_OUTSIDE;
      }
      if (value > model.getTopRange()) {
         return HIGHEST_COLOUR_OUTSIDE;
      }
      double proportion = (value - model.getBottomRange()) / (model.getTopRange() - model.getBottomRange());
      double red = HIGHEST_COLOUR.getRed() * proportion + LOWEST_COLOUR.getRed() * (1.0 - proportion);
      double green = HIGHEST_COLOUR.getGreen() * proportion + LOWEST_COLOUR.getGreen() * (1.0 - proportion);
      double blue = HIGHEST_COLOUR.getBlue() * proportion + LOWEST_COLOUR.getBlue() * (1.0 - proportion);
      return new Color(red, green, blue, 1.0);
   }
   
}
