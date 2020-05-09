
package mapmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import datahandler.location.DataLocationFinder;
import datahandler.location.LocationNotFoundException;
import datahandler.path.PathElement;
import datahandler.path.PathIndexLookup;

public class ListModel<T extends ChildModel> implements ParentModel, ChildModel {
   
   private ParentModel parentModel;
   private DataLocationFinder baseLocationFinder;
   private DataLocationFinder nextItemLocationFinder;
   private Function<ParentModel, T> childFactory;
   
   private List<T> childModels;
   private Map<T, Integer> valueToIndex;
   
   private boolean dirty;
   
   public ListModel(ParentModel parentModel, DataLocationFinder baseLocationFinder,
         DataLocationFinder nextItemLocationFinder, Function<ParentModel, T> childFactory) {
      this.parentModel = parentModel;
      this.baseLocationFinder = baseLocationFinder;
      this.nextItemLocationFinder = nextItemLocationFinder;
      this.childFactory = childFactory;
      
      childModels = new ArrayList<>();
      valueToIndex = new LinkedHashMap<>();
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      int offset = baseLocationFinder.findLocation(data, offsetHint);
      Iterator<T> iterator = childModels.iterator();
      while (iterator.hasNext()) {
         iterator.next().readAllModels(data, offset);
         if (iterator.hasNext()) {
            offset = nextItemLocationFinder.findLocation(data, offset);
         }
      }
      markDirty();
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      if (childModels.isEmpty()) {
         return;
      }
      int offset = baseLocationFinder.findLocation(data, offsetHint);
      Iterator<T> iterator = childModels.iterator();
      while (iterator.hasNext()) {
         iterator.next().writeAllModels(data, offset);
         if (iterator.hasNext()) {
            offset = nextItemLocationFinder.findLocation(data, offset);
         }
      }
   }
   
   public DataLocationFinder getBaseLocationFinder() {
      return baseLocationFinder;
   }
   
   public DataLocationFinder getNextItemLocationFinder() {
      return nextItemLocationFinder;
   }
   
   @Override
   public ParentModel getParentModel() {
      return parentModel;
   }
   
   public ChildModel findChildModel(PathIndexLookup pathIndexLookup) {
      return childModels.get(pathIndexLookup.getIndex());
   }
   
   @Override
   public PathElement createChildPathElement(ChildModel childModel) {
      return new PathIndexLookup(indexOf(childModel));
   }
   
   public List<T> getChildModels() {
      return childModels;
   }
   
   public Function<ParentModel, T> getChildFactory() {
      return childFactory;
   }
   
   public void markDirty() {
      dirty = true;
      valueToIndex.clear();
   }
   
   private int indexOf(ChildModel value) {
      if (dirty) {
         for (int i = 0; i < childModels.size(); i++) {
            valueToIndex.put(childModels.get(i), i);
         }
         dirty = false;
      }
      return valueToIndex.get(value);
   }
   
}
