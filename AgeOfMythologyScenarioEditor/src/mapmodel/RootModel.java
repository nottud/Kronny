
package mapmodel;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import datahandler.DataModel;
import datahandler.converter.IntegerConverter;
import datahandler.location.LocationNotFoundException;
import datahandler.location.NextSequenceLocationFinder;
import datahandler.location.RelativeLocationFinder;
import datahandler.path.PathElement;
import datahandler.path.PathNameLookup;
import mapmodel.map.MapSizeModel;
import mapmodel.multiplayeroverride.MultiplayerOverrideModel;
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
   
   private static final List<Byte> YET_ANOTHER_LENGTH = Arrays.asList((byte) 0x84, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x36,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4b, (byte) 0x37,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4b, (byte) 0x39,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x34,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x34,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x33,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x47, (byte) 0x41,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x46, (byte) 0x52,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x46, (byte) 0x50,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x46, (byte) 0x4e,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x46, (byte) 0x4c,
         (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00);
   
   private ObservableManager observableManager;
   private Map<DataModel<Integer>, Integer> pointerToEndDistance;
   private NamedChildStorage children;
   
   private DataModel<Integer> mainLengthModel;
   private DataModel<Integer> otherLengthModel;
   private DataModel<Integer> yetAnotherLength;
   
   private MultiplayerOverrideModel multiplayerOverrideModel;
   private MapSizeModel mapSizeModel;
   private AllPlayersModel allPlayersModel;
   private AllUnitsModel allUnitsModel;
   
   public RootModel() {
      observableManager = new ObservableManagerImpl();
      pointerToEndDistance = new LinkedHashMap<>();
      children = new NamedChildStorage();
      
      mainLengthModel = children.add("Length",
            new DataModel<>(this, new RelativeLocationFinder(2), new IntegerConverter()));
      otherLengthModel = children.add("OtherLength",
            new DataModel<>(this, NextSequenceLocationFinder.atSequenceStart(OTHER_LENGTH_SEQUENCE, -8), new IntegerConverter()));
      yetAnotherLength = children.add("YetAnotherLength", new DataModel<>(this,
            NextSequenceLocationFinder.atSequenceStart(YET_ANOTHER_LENGTH, -IntegerConverter.BYTES_IN_INT), new IntegerConverter()));
      
      multiplayerOverrideModel = children.add("MultiplayerOverride", new MultiplayerOverrideModel(this));
      mapSizeModel = children.add("MapSize", new MapSizeModel(this));
      allPlayersModel = children.add("Players", new AllPlayersModel(this));
      allUnitsModel = children.add("Units", new AllUnitsModel(this));
   }
   
   public MultiplayerOverrideModel getMultiplayerOverrideModel() {
      return multiplayerOverrideModel;
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
      int fileLength = data.size();
      readPointer(data, offsetHint, fileLength, mainLengthModel);
      readPointer(data, offsetHint, fileLength, otherLengthModel);
      readPointer(data, offsetHint, fileLength, yetAnotherLength);
      
      multiplayerOverrideModel.readAllModels(data, offsetHint);
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
      multiplayerOverrideModel.writeAllModels(data, offsetHint);
      
      int fileLength = data.size();
      writePointer(data, offsetHint, fileLength, yetAnotherLength);
      writePointer(data, offsetHint, fileLength, otherLengthModel);
      writePointer(data, offsetHint, fileLength, mainLengthModel);
      
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
   
   private void readPointer(List<Byte> data, int offsetHint, int fileLength, DataModel<Integer> pointer) throws LocationNotFoundException {
      int location = pointer.getDataLocationFinder().findLocation(data, offsetHint);
      pointer.readAllModels(data, offsetHint);
      pointerToEndDistance.put(pointer, fileLength - location);
   }
   
   private void writePointer(List<Byte> data, int offsetHint, int fileLength, DataModel<Integer> pointer) throws LocationNotFoundException {
      int location = pointer.getDataLocationFinder().findLocation(data, offsetHint);
      int newDiff = fileLength - location;
      int value = pointer.getValue() + newDiff - pointerToEndDistance.get(pointer);
      
      pointer.setValue(value);
      pointer.writeAllModels(data, offsetHint);
      pointerToEndDistance.put(pointer, newDiff);
   }
   
}
