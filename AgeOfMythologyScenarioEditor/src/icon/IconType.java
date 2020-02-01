
package icon;

import javafx.scene.image.Image;
import utility.image.ImageLoader;

public enum IconType {
   
   LOAD("Load", "editor load.bmp"),
   SAVE("Save", "editor save.bmp"),
   UNDO("Undo", "editor undo.bmp"),
   REDO("Redo", "editor redo.bmp"),
   PAINT_TERRAIN("Paint Terrain", "editor paintterrain.bmp"),
   PAINT_ELEVATION("Paint Elevation", "editor raiselowerterrain.bmp"),
   PAINT_WATER("Paint Terrain", "editor river.bmp"),
   PAINT_WATER_COLOUR("Paint Terrain", "editor edit water.bmp"),
   PAINT_WATER_HEIGHT("Paint Water Elevation", "editor raiselowerwater.bmp");
   
   private String name;
   private String file;
   
   private Image image;
   
   private IconType(String name, String file) {
      this.name = name;
      this.file = file;
   }
   
   public String getName() {
      return name;
   }
   
   public String getFile() {
      return file;
   }
   
   public Image loadOrGetImage() {
      if (image == null) {
         image = ImageLoader.loadImage(IconRoot.class, file);
      }
      return image;
   }
   
}
