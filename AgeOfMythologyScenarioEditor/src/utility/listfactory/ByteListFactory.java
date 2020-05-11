
package utility.listfactory;

import java.util.ArrayList;
import java.util.List;

public class ByteListFactory {
   
   public static List<Byte> generate(int count) {
      List<Byte> list = new ArrayList<>(count);
      for (int i = 0; i < count; i++) {
         list.add((byte) i);
      }
      return list;
   }
   
}
