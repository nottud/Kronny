
package mapmodel;

import datahandler.path.PathElement;

public interface ParentModel extends ModelRequirements {
   
   public PathElement createChildPathElement(ChildModel childModel);
   
}
