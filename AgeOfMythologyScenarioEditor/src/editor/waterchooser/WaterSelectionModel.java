package editor.waterchooser;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;
import water.WaterEntry;

public class WaterSelectionModel implements Observable {
	
	public static final ObserverType<WaterEntry> SELECTION_CHANGE = new ObserverType<>();
	
	private ObservableManager observableManager;
	
	private WaterEntry selectedWaterEntry;
	
	public WaterSelectionModel() {
		observableManager = new ObservableManagerImpl();
	}

	@Override
	public ObservableManager getObservableManager() {
		return observableManager;
	}
	
	public WaterEntry getSelectedWaterEntry() {
		return selectedWaterEntry;
	}
	
	public void setSelectedTerrainType(WaterEntry selectedWaterEntry) {
		this.selectedWaterEntry = selectedWaterEntry;
		observableManager.notifyObservers(SELECTION_CHANGE, selectedWaterEntry);
	}

}
