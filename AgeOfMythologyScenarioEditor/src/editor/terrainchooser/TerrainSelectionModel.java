
package editor.terrainchooser;

import gameenumeration.terrain.TerrainEntry;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class TerrainSelectionModel implements Observable {
   
   public static final ObserverType<TerrainEntry> SELECTION_CHANGE = new ObserverType<>();
   
   private ObservableManager observableManager;
   
   private TerrainEntry selectedTerrainEntry;
   
   public TerrainSelectionModel() {
      observableManager = new ObservableManagerImpl();
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
   public TerrainEntry getSelectedTerrainEntry() {
      return selectedTerrainEntry;
   }
   
   public void setSelectedTerrainType(TerrainEntry selectedTerrainEntry) {
      this.selectedTerrainEntry = selectedTerrainEntry;
      observableManager.notifyObservers(SELECTION_CHANGE, selectedTerrainEntry);
   }
   
}
