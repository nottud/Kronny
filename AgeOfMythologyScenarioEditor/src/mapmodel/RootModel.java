
package mapmodel;

import java.util.Arrays;
import java.util.List;

import datahandler.DataModel;
import datahandler.converter.IntegerConverter;
import datahandler.location.LocationNotFoundException;
import datahandler.location.NextSequenceLocationFinder;
import datahandler.location.RelativeLocationFinder;
import datahandler.path.PathElement;
import datahandler.path.PathNameLookup;
import mapmodel.map.MapSizeModel;
import mapmodel.player.AllPlayersModel;
import mapmodel.unit.AllUnitsModel;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class RootModel implements ParentModel, Observable {
   
   public static final ObserverType<Void> MODEL_READ = new ObserverType<>();
   public static final ObserverType<Void> MODEL_WRITTEN = new ObserverType<>();
   
   private static final List<Byte> OTHER_LENGTH_SEQUENCE = Arrays.asList((byte) 0x48, (byte) 0x36, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4b, (byte) 0x37, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4b, (byte) 0x39, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x34, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x34, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x33, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00,
         (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00);
   
   private ObservableManager observableManager;
   private NamedChildStorage children;
   
   private DataModel<Integer> mainLengthModel;
   private DataModel<Integer> otherLengthModel;
   
   private MapSizeModel mapSizeModel;
   private AllPlayersModel allPlayersModel;
   private AllUnitsModel allUnitsModel;
   
   private int oldLength;
   
   public RootModel() {
      observableManager = new ObservableManagerImpl();
      children = new NamedChildStorage();
      
      mainLengthModel = children.add("Length",
            new DataModel<>(this, new RelativeLocationFinder(2), new IntegerConverter()));
      otherLengthModel = children.add("OtherLength",
            new DataModel<>(this, NextSequenceLocationFinder.atSequenceStart(OTHER_LENGTH_SEQUENCE, -8), new IntegerConverter()));
      
      mapSizeModel = children.add("MapSize", new MapSizeModel(this));
      allPlayersModel = children.add("Players", new AllPlayersModel(this));
      allUnitsModel = children.add("Units", new AllUnitsModel(this));
   }
   
   public MapSizeModel getMapSizeModel() {
      return mapSizeModel;
   }
   
   public AllPlayersModel getAllPlayersModel() {
      return allPlayersModel;
   }
   
   public AllUnitsModel getAllUnitsModel() {
      return allUnitsModel;
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      oldLength = data.size();
      mainLengthModel.readAllModels(data, offsetHint);
      otherLengthModel.readAllModels(data, offsetHint);
      mapSizeModel.readAllModels(data, offsetHint);
      allPlayersModel.readAllModels(data, offsetHint);
      allUnitsModel.readAllModels(data, offsetHint);
      observableManager.notifyObservers(MODEL_READ, null);
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      allUnitsModel.writeAllModels(data, offsetHint);
      allPlayersModel.writeAllModels(data, offsetHint);
      mapSizeModel.writeAllModels(data, offsetHint);
      
      //Write out length with diff between original current and set back after
      int newLength = data.size();
      int oldOtherLength = otherLengthModel.setValue(otherLengthModel.getValue() + newLength - oldLength);
      otherLengthModel.writeAllModels(data, offsetHint);
      otherLengthModel.setValue(oldOtherLength);
      int oldMainLength = mainLengthModel.setValue(mainLengthModel.getValue() + newLength - oldLength);
      mainLengthModel.writeAllModels(data, offsetHint);
      mainLengthModel.setValue(oldMainLength);
      
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
