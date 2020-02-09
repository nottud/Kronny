
package gameenumeration.water;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import utility.image.ImageLoader;
import water.WaterRoot;

public class WaterEntry {
   
   private int index;
   private String name;
   private String file;
   private Color colour;
   private double depth;
   
   private Image image;
   
   public WaterEntry(int index, String name, String file, Color colour, double depth) {
      this.index = index;
      this.name = name;
      this.file = file;
      this.colour = colour;
      this.depth = depth;
   }
   
   public int getIndex() {
      return index;
   }
   
   public String getName() {
      return name;
   }
   
   public String getFile() {
      return file;
   }
   
   public Color getColour() {
      return colour;
   }
   
   public double getDepth() {
      return depth;
   }
   
   public Image loadOrGetImage() {
      if (image == null) {
         image = ImageLoader.loadImage(WaterRoot.class, file);
      }
      return image;
   }
   
}
