
package editor.tool.paintterrain;

import java.util.LinkedHashMap;
import java.util.Map;

import gameenumeration.terrain.TerrainEntry;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import utility.image.ImageLoaderEntryType;

public class TerrainPreviewColourCache {
   
   private Map<TerrainEntry, Color> terrainEntryToColour;
   
   public TerrainPreviewColourCache() {
      terrainEntryToColour = new LinkedHashMap<>();
   }
   
   public Color getColour(TerrainEntry terrainEntry) {
      return terrainEntryToColour.computeIfAbsent(terrainEntry, this::calculatePreviewColour);
   }
   
   private Color calculatePreviewColour(TerrainEntry terrainEntry) {
      Image image = terrainEntry.loadOrGetImage().getImage(ImageLoaderEntryType.TINY);
      double red = 0;
      double green = 0;
      double blue = 0;
      
      int imageWidth = (int) Math.round(image.getWidth());
      int imageHeight = (int) Math.round(image.getHeight());
      int imageSize = imageWidth * imageHeight;
      
      PixelReader pixelReader = image.getPixelReader();
      for (int y = 0; y < imageHeight; y++) {
         for (int x = 0; x < imageWidth; x++) {
            Color colour = pixelReader.getColor(x, y);
            red = red + colour.getRed();
            green = green + colour.getGreen();
            blue = blue + colour.getBlue();
         }
      }
      
      return new Color(red / imageSize, green / imageSize, blue / imageSize, 1.0);
   }
   
}
