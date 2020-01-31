
package datahandler.path;

public class PathNameLookup implements PathElement {
   
   private String name;
   
   public PathNameLookup(String name) {
      this.name = name;
   }
   
   public String getName() {
      return name;
   }
   
}
