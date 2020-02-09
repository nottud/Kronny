
package utility.listfactory;

import java.util.ArrayList;
import java.util.List;

public class IntegerListFactory {
   
   public static List<Integer> generate(int count) {
      List<Integer> list = new ArrayList<>(count);
      for (int i = 0; i < count; i++) {
         list.add(i);
      }
      return list;
   }
   
}
