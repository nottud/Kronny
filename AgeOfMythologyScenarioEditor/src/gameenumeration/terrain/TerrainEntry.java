
package gameenumeration.terrain;

import terrain.TerrainRoot;
import utility.image.ImageLoader;
import utility.image.ImageLoaderEntry;

public class TerrainEntry {
   
   private int groupIndex;
   private int index;
   private String name;
   private String file;
   private ImageLoaderEntry imageLoaderEntry;
   
   public TerrainEntry(int groupIndex, int index, String name, String file) {
      this.groupIndex = groupIndex;
      this.index = index;
      this.name = name;
      this.file = file;
   }
   
   public int getGroupIndex() {
      return groupIndex;
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
   
   public ImageLoaderEntry loadOrGetImage() {
      if (imageLoaderEntry == null) {
         imageLoaderEntry = ImageLoader.loadImageGroup(TerrainRoot.class, file);
         
      }
      return imageLoaderEntry;
   }
   
}
