
package editor.unit.selection;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class SelectedUnitsModel implements Observable {
   
   public static final ObserverType<Set<Integer>> SELECTION_CHANGED = new ObserverType<>();
   
   private ObservableManager observableManager;
   
   private Set<Integer> selectedUnitsModifiable;
   private Set<Integer> selectedUnits;
   
   public SelectedUnitsModel() {
      observableManager = new ObservableManagerImpl();
      
      selectedUnitsModifiable = new LinkedHashSet<>();
      selectedUnits = Collections.unmodifiableSet(selectedUnitsModifiable);
   }
   
   public Set<Integer> getSelectedUnits() {
      return selectedUnits;
   }
   
   public void setSelectedUnit(Integer index) {
      selectedUnitsModifiable.clear();
      if (index != null) {
         selectedUnitsModifiable.add(index);
      }
      observableManager.notifyObservers(SELECTION_CHANGED, selectedUnits);
   }
   
   public void setSelectedUnits(Collection<Integer> selection) {
      selectedUnitsModifiable.clear();
      selectedUnitsModifiable.addAll(selection);
      observableManager.notifyObservers(SELECTION_CHANGED, selectedUnits);
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
