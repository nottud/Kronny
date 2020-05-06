
package gameenumeration.unit;

import javafx.scene.image.Image;
import unit.UnitRoot;
import utility.image.ImageLoader;

public class UnitEntry {
   
   private int index;
   private String editorName;
   private String ingameName;
   private String icon;
   
   private Image image;
   
   public UnitEntry(int index, String editorName, String ingameName, String icon) {
      this.index = index;
      this.editorName = editorName;
      this.ingameName = ingameName;
      this.icon = icon;
   }
   
   public int getIndex() {
      return index;
   }
   
   public String getEditorName() {
      return editorName;
   }
   
   public String getIngameName() {
      return ingameName;
   }
   
   public String getIcon() {
      return icon;
   }
   
   public Image loadOrGetImage() {
      if (image == null) {
         image = ImageLoader.loadImage(UnitRoot.class, icon);
      }
      return image;
   }
   
}
