
package mapmodel;

import java.util.LinkedHashMap;
import java.util.Map;

import datahandler.path.PathNameLookup;

public class NamedChildStorage {
   
   private Map<String, ChildModel> nameToChild;
   private Map<ChildModel, String> childToName;
   
   public NamedChildStorage() {
      nameToChild = new LinkedHashMap<>();
      childToName = new LinkedHashMap<>();
   }
   
   public <T extends ChildModel> T add(String name, T childModel) {
      nameToChild.put(name, childModel);
      childToName.put(childModel, name);
      return childModel;
   }
   
   public ChildModel findChild(PathNameLookup pathNameLookup) {
      return nameToChild.get(pathNameLookup.getName());
   }
   
   public String getName(ChildModel childModel) {
      return childToName.get(childModel);
   }
   
}
