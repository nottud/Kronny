
package utility.graphics.colour;

import javafx.scene.paint.Color;

public class ColourToArgb {
   
   public static int convert(Color colour) {
      int a = (int) Math.round(colour.getOpacity() * 255.0);
      int r = (int) Math.round(colour.getRed() * 255.0);
      int g = (int) Math.round(colour.getGreen() * 255.0);
      int b = (int) Math.round(colour.getBlue() * 255.0);
      return (a << 24) | (r << 16) | (g << 8) | b;
   }
   
   public static Color convert(int value) {
      int a = value >>> 24;
      int r = (value >> 16) & 0xff;
      int g = (value >> 8) & 0xff;
      int b = (value) & 0xff;
      return Color.rgb(r, g, b, a / 255.0);
   }
   
}
