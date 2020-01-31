package mapmodel;

import java.util.List;

import datahandler.DataModel;
import datahandler.converter.IntegerConverter;
import datahandler.location.RelativeLocationFinder;
import datahandler.path.PathElement;
import datahandler.path.PathNameLookup;
import mapmodel.map.MapSizeModel;
import mapmodel.player.AllPlayersModel;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class RootModel implements ParentModel, Observable {

	public static final ObserverType<Void> MODEL_READ = new ObserverType<>();
	public static final ObserverType<Void> MODEL_WRITTEN = new ObserverType<>();

	private ObservableManager observableManager;
	private NamedChildStorage children;

	private DataModel<Integer> lengthModel;

	private MapSizeModel mapSizeModel;
	private AllPlayersModel allPlayersModel;

	public RootModel() {
		observableManager = new ObservableManagerImpl();
		children = new NamedChildStorage();

		lengthModel = children.add("Length",
				new DataModel<>(this, new RelativeLocationFinder(2), new IntegerConverter()));

		mapSizeModel = children.add("MapSize", new MapSizeModel(this));
		allPlayersModel = children.add("Players", new AllPlayersModel(this));
	}

	public MapSizeModel getMapSizeModel() {
		return mapSizeModel;
	}

	public AllPlayersModel getAllPlayersModel() {
		return allPlayersModel;
	}

	@Override
	public void readAllModels(List<Byte> data, int offsetHint) {
		lengthModel.readAllModels(data, offsetHint);
		mapSizeModel.readAllModels(data, offsetHint);
		allPlayersModel.readAllModels(data, offsetHint);
		observableManager.notifyObservers(MODEL_READ, null);
	}

	public void writeAllModels(List<Byte> data, int offsetHint) {
		mapSizeModel.writeAllModels(data, offsetHint);
		allPlayersModel.writeAllModels(data, offsetHint);
		lengthModel.writeAllModels(data, offsetHint); //Length change not allowed TODO

		observableManager.notifyObservers(MODEL_WRITTEN, null);
	}

	public ChildModel getChildModel(PathNameLookup pathNameLookup) {
		return children.findChild(pathNameLookup);
	}

	@Override
	public PathElement createChildPathElement(ChildModel childModel) {
		return new PathNameLookup(children.getName(childModel));
	}

	@Override
	public ObservableManager getObservableManager() {
		return observableManager;
	}

}
