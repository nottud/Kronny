
package editor.unitchooser;

import gameenumeration.unit.UnitEntry;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class UnitSelectionModel implements Observable {
   
   public static final ObserverType<UnitEntry> SELECTION_CHANGE = new ObserverType<>();
   
   private ObservableManager observableManager;
   
   private UnitEntry selectedUnitEntry;
   
   public UnitSelectionModel() {
      observableManager = new ObservableManagerImpl();
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
   public UnitEntry getSelectedUnitEntry() {
      return selectedUnitEntry;
   }
   
   public void setSelectedUnitType(UnitEntry selectedWaterEntry) {
      this.selectedUnitEntry = selectedWaterEntry;
      observableManager.notifyObservers(SELECTION_CHANGE, selectedWaterEntry);
   }
   
}
