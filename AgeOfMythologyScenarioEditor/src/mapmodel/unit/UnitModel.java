
package mapmodel.unit;

import java.util.List;

import datahandler.DataModel;
import datahandler.converter.FloatConverter;
import datahandler.converter.HalfStringConverter;
import datahandler.converter.IntegerConverter;
import datahandler.location.AfterKnownLocationFinder;
import datahandler.location.LocationNotFoundException;
import datahandler.location.RelativeLocationFinder;
import mapmodel.BranchModel;
import mapmodel.ParentModel;

public class UnitModel extends BranchModel {
   
   private DataModel<Integer> unitId;
   private DataModel<Integer> unitPlayer;
   private DataModel<Float> posX;
   private DataModel<Float> posY;
   private DataModel<Float> posZ;
   private DataModel<Float> mXX;
   private DataModel<Float> mXY;
   private DataModel<Float> mXZ;
   private DataModel<Float> mYX;
   private DataModel<Float> mYY;
   private DataModel<Float> mYZ;
   private DataModel<Float> mZX;
   private DataModel<Float> mZY;
   private DataModel<Float> mZZ;
   private DataModel<String> unitName;
   private DataModel<Integer> unitType;
   private DataModel<Float> scaleX;
   private DataModel<Float> scaleY;
   private DataModel<Float> scaleZ;
   
   public UnitModel(ParentModel parent) {
      super(parent);
      unitId = children.add("Id", new DataModel<>(this, new RelativeLocationFinder(-13), new IntegerConverter()));
      unitPlayer = children.add("Player", new DataModel<>(this, new RelativeLocationFinder(11), new IntegerConverter()));
      posX = children.add("PosX", new DataModel<>(this, new AfterKnownLocationFinder(unitPlayer, 20), new FloatConverter()));
      posY = children.add("PosY", new DataModel<>(this, new AfterKnownLocationFinder(posX, 0), new FloatConverter()));
      posZ = children.add("PosZ", new DataModel<>(this, new AfterKnownLocationFinder(posY, 0), new FloatConverter()));
      mXX = children.add("Mxx", new DataModel<>(this, new AfterKnownLocationFinder(posZ, 0), new FloatConverter()));
      mXY = children.add("Mxy", new DataModel<>(this, new AfterKnownLocationFinder(mXX, 0), new FloatConverter()));
      mXZ = children.add("Mxz", new DataModel<>(this, new AfterKnownLocationFinder(mXY, 0), new FloatConverter()));
      mYX = children.add("Myx", new DataModel<>(this, new AfterKnownLocationFinder(mXZ, 0), new FloatConverter()));
      mYY = children.add("Myy", new DataModel<>(this, new AfterKnownLocationFinder(mYX, 0), new FloatConverter()));
      mYZ = children.add("Myz", new DataModel<>(this, new AfterKnownLocationFinder(mYY, 0), new FloatConverter()));
      mZX = children.add("Mzx", new DataModel<>(this, new AfterKnownLocationFinder(mYZ, 0), new FloatConverter()));
      mZY = children.add("Mzy", new DataModel<>(this, new AfterKnownLocationFinder(mZX, 0), new FloatConverter()));
      mZZ = children.add("Mzz", new DataModel<>(this, new AfterKnownLocationFinder(mZY, 0), new FloatConverter()));
      unitName = children.add("Name", new DataModel<>(this, new RelativeLocationFinder(89), new HalfStringConverter()));
      unitType = children.add("Type", new DataModel<>(this, new AfterKnownLocationFinder(unitName, 0), new IntegerConverter()));
      scaleX = children.add("ScaleX", new DataModel<>(this, new AfterKnownLocationFinder(unitType, 0), new FloatConverter()));
      scaleY = children.add("ScaleY", new DataModel<>(this, new AfterKnownLocationFinder(scaleX, 0), new FloatConverter()));
      scaleZ = children.add("ScaleZ", new DataModel<>(this, new AfterKnownLocationFinder(scaleY, 0), new FloatConverter()));
   }
   
   @Override
   public void readAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      unitId.readAllModels(data, offsetHint);
      unitPlayer.readAllModels(data, offsetHint);
      posX.readAllModels(data, offsetHint);
      posY.readAllModels(data, offsetHint);
      posZ.readAllModels(data, offsetHint);
      mXX.readAllModels(data, offsetHint);
      mXY.readAllModels(data, offsetHint);
      mXZ.readAllModels(data, offsetHint);
      mYX.readAllModels(data, offsetHint);
      mYY.readAllModels(data, offsetHint);
      mYZ.readAllModels(data, offsetHint);
      mZX.readAllModels(data, offsetHint);
      mZY.readAllModels(data, offsetHint);
      mZZ.readAllModels(data, offsetHint);
      unitName.readAllModels(data, offsetHint);
      unitType.readAllModels(data, offsetHint);
      scaleX.readAllModels(data, offsetHint);
      scaleY.readAllModels(data, offsetHint);
      scaleZ.readAllModels(data, offsetHint);
   }
   
   @Override
   public void writeAllModels(List<Byte> data, int offsetHint) throws LocationNotFoundException {
      scaleZ.writeAllModels(data, offsetHint);
      scaleY.writeAllModels(data, offsetHint);
      scaleX.writeAllModels(data, offsetHint);
      unitType.writeAllModels(data, offsetHint);
      unitName.writeAllModels(data, offsetHint);
      mZZ.writeAllModels(data, offsetHint);
      mZY.writeAllModels(data, offsetHint);
      mZX.writeAllModels(data, offsetHint);
      mYZ.writeAllModels(data, offsetHint);
      mYY.writeAllModels(data, offsetHint);
      mYX.writeAllModels(data, offsetHint);
      mXZ.writeAllModels(data, offsetHint);
      mXY.writeAllModels(data, offsetHint);
      mXX.writeAllModels(data, offsetHint);
      posZ.writeAllModels(data, offsetHint);
      posY.writeAllModels(data, offsetHint);
      posX.writeAllModels(data, offsetHint);
      unitPlayer.writeAllModels(data, offsetHint);
      unitId.writeAllModels(data, offsetHint);
   }
   
   public DataModel<Integer> getUnitId() {
      return unitId;
   }
   
   public DataModel<Integer> getUnitPlayer() {
      return unitPlayer;
   }
   
   public DataModel<Float> getPosX() {
      return posX;
   }
   
   public DataModel<Float> getPosY() {
      return posY;
   }
   
   public DataModel<Float> getPosZ() {
      return posZ;
   }
   
   public DataModel<Float> getmXX() {
      return mXX;
   }
   
   public DataModel<Float> getmXY() {
      return mXY;
   }
   
   public DataModel<Float> getmXZ() {
      return mXZ;
   }
   
   public DataModel<Float> getmYX() {
      return mYX;
   }
   
   public DataModel<Float> getmYY() {
      return mYY;
   }
   
   public DataModel<Float> getmYZ() {
      return mYZ;
   }
   
   public DataModel<Float> getmZX() {
      return mZX;
   }
   
   public DataModel<Float> getmZY() {
      return mZY;
   }
   
   public DataModel<Float> getmZZ() {
      return mZZ;
   }
   
   public DataModel<String> getUnitName() {
      return unitName;
   }
   
   public DataModel<Integer> getUnitType() {
      return unitType;
   }
   
   public DataModel<Float> getScaleX() {
      return scaleX;
   }
   
   public DataModel<Float> getScaleY() {
      return scaleY;
   }
   
   public DataModel<Float> getScaleZ() {
      return scaleZ;
   }
   
}
