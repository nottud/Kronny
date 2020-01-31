
package utility.image;

import java.util.EnumMap;
import java.util.Map;

import javafx.scene.image.Image;

public class ImageLoaderEntry {
   
   private Map<ImageLoaderEntryType, Image> typeToImage;
   
   public ImageLoaderEntry(Image largeImage, Image mediumImage, Image smallImage, Image tinyImage) {
      typeToImage = new EnumMap<>(ImageLoaderEntryType.class);
      typeToImage.put(ImageLoaderEntryType.LARGE, largeImage);
      typeToImage.put(ImageLoaderEntryType.MEDIUM, mediumImage);
      typeToImage.put(ImageLoaderEntryType.SMALL, smallImage);
      typeToImage.put(ImageLoaderEntryType.TINY, tinyImage);
   }
   
   public Image getImage(ImageLoaderEntryType type) {
      return typeToImage.get(type);
   }
   
}
