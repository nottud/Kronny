
package raycastviewer.terrain.cache;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import utility.graphics.colour.ColourToArgb;

public class TerrainRayCastingCache {
   
   private int[][] colourCache;
   private int width;
   private int height;
   
   public TerrainRayCastingCache(Image image) {
      PixelReader pixelReader = image.getPixelReader();
      width = (int) image.getWidth();
      height = (int) image.getHeight();
      colourCache = new int[width][height];
      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            colourCache[x][y] = ColourToArgb.convert(pixelReader.getColor(x, y));
         }
      }
   }
   
   public int getWidth() {
      return width;
   }
   
   public int getHeight() {
      return height;
   }
   
   public int getColour(int x, int y) {
      return colourCache[x][y];
   }
   
}
