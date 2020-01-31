
package datahandler.path;

import java.util.Iterator;
import java.util.LinkedList;

import mapmodel.BranchModel;
import mapmodel.ChildModel;
import mapmodel.ListModel;
import mapmodel.ParentModel;
import mapmodel.RootModel;

public class Path<T extends ChildModel> {
   
   private LinkedList<PathElement> pathElements;
   
   public Path(T item) {
      pathElements = new LinkedList<>();
      ChildModel childModel = item;
      ParentModel parentModel = item.getParentModel();
      while (true) {
         pathElements.add(parentModel.createChildPathElement(childModel));
         if (parentModel instanceof ChildModel) {
            childModel = (ChildModel) parentModel;
            parentModel = childModel.getParentModel();
         } else {
            break;
         }
      }
   }
   
   public T resolve(RootModel rootModel) {
      Iterator<PathElement> iterator = pathElements.descendingIterator();
      ChildModel childModel = rootModel.getChildModel((PathNameLookup) iterator.next());
      while (childModel instanceof ParentModel) {
         if (childModel instanceof BranchModel) {
            childModel = ((BranchModel) childModel).getChildModel((PathNameLookup) iterator.next());
         } else {
            childModel = ((ListModel<?>) childModel).findChildModel(((PathIndexLookup) iterator.next()));
         }
      }
      @SuppressWarnings("unchecked")
      T foundValue = (T) childModel;
      return foundValue;
   }
   
}
