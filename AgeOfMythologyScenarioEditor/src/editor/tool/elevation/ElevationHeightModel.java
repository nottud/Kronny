package editor.tool.elevation;

import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class ElevationHeightModel implements Observable {
	
	public static final ObserverType<Float> HEIGHT_CHANGED = new ObserverType<>();
	
	private ObservableManager observableManager;
	
	private float height;
	
	public ElevationHeightModel() {
		observableManager = new ObservableManagerImpl();
		
		height = 0.0F;
	}
	
	public float getHeight() {
		return height;
	}
	
	public void setHeight(float height) {
		this.height = height;
		observableManager.notifyObservers(HEIGHT_CHANGED, height);
	}

	@Override
	public ObservableManager getObservableManager() {
		return observableManager;
	}

}
