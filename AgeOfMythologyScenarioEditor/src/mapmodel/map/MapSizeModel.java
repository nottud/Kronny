package mapmodel.map;

import java.util.List;

import datahandler.DataModel;
import datahandler.converter.ByteConverter;
import datahandler.converter.ColourConverter;
import datahandler.converter.FloatConverter;
import datahandler.converter.HalfStringConverter;
import datahandler.converter.IntegerConverter;
import datahandler.location.AfterKnownLocationFinder;
import datahandler.location.AfterSimpleListLocationFinder;
import datahandler.location.NextSequenceLocationFinder;
import datahandler.location.RelativeLocationFinder;
import javafx.scene.paint.Color;
import mapmodel.BranchModel;
import mapmodel.ListModel;
import mapmodel.ParentModel;

public class MapSizeModel extends BranchModel {
	
	private DataModel<Integer> mapSizeX;
	private DataModel<Integer> mapSizeZ;
	
	private ListModel<DataModel<Byte>> terrainGroup;
	private ListModel<DataModel<Byte>> terrainData;
	private ListModel<DataModel<Byte>> unknown;
	private ListModel<DataModel<Color>> waterColour;
	private ListModel<DataModel<Byte>> waterType;
	private ListModel<DataModel<Float>> terrainHeight;
	private ListModel<DataModel<Float>> waterHeight;

	public MapSizeModel(ParentModel parentModel) {
		super(parentModel);
		List<Byte> mapSequence = new HalfStringConverter().toBytes("terrain\\Hades9");
		mapSizeX = children.add("SizeX", new DataModel<>(this, NextSequenceLocationFinder.afterSequence(mapSequence, 0), new IntegerConverter()));
		mapSizeZ = children.add("SizeZ", new DataModel<>(this, new AfterKnownLocationFinder(mapSizeX, 0), new IntegerConverter()));
		terrainGroup = children.add("TerrainGroup", new ListModel<DataModel<Byte>>(this, new AfterKnownLocationFinder(mapSizeZ, IntegerConverter.BYTES_IN_INT * 4 + 2), new RelativeLocationFinder(1), foundParent -> new DataModel<Byte>(foundParent, new RelativeLocationFinder(0), new ByteConverter())));
		terrainData = children.add("TerrainData", new ListModel<DataModel<Byte>>(this, new AfterSimpleListLocationFinder(terrainGroup, IntegerConverter.BYTES_IN_INT * 2 + 2), new RelativeLocationFinder(1), foundParent -> new DataModel<Byte>(foundParent, new RelativeLocationFinder(0), new ByteConverter())));
		unknown = children.add("Unknown", new ListModel<DataModel<Byte>>(this, new AfterSimpleListLocationFinder(terrainData, IntegerConverter.BYTES_IN_INT * 2 + 2), new RelativeLocationFinder(1), foundParent -> new DataModel<Byte>(foundParent, new RelativeLocationFinder(0), new ByteConverter())));
		waterColour = children.add("WaterColour", new ListModel<DataModel<Color>>(this, new AfterSimpleListLocationFinder(unknown, IntegerConverter.BYTES_IN_INT * 2 + 2), new RelativeLocationFinder(ColourConverter.BYTES_IN_COLOUR), foundParent -> new DataModel<Color>(foundParent, new RelativeLocationFinder(0), new ColourConverter())));
		List<Byte> mapSequence2 = new HalfStringConverter().toBytes("South Sea");
		waterType = children.add("WaterType", new ListModel<DataModel<Byte>>(this, NextSequenceLocationFinder.afterSequence(mapSequence2, 10), new RelativeLocationFinder(1), foundParent -> new DataModel<Byte>(foundParent, new RelativeLocationFinder(0), new ByteConverter())));
		terrainHeight = children.add("TerrainHeight", new ListModel<DataModel<Float>>(this, new AfterSimpleListLocationFinder(waterType, 0), new RelativeLocationFinder(FloatConverter.BYTES_IN_FLOAT), foundParent -> new DataModel<Float>(foundParent, new RelativeLocationFinder(0), new FloatConverter())));
		waterHeight = children.add("WaterHeight", new ListModel<DataModel<Float>>(this, new AfterSimpleListLocationFinder(terrainHeight, 0), new RelativeLocationFinder(FloatConverter.BYTES_IN_FLOAT), foundParent -> new DataModel<Float>(foundParent, new RelativeLocationFinder(0), new FloatConverter())));
	}

	public DataModel<Integer> getMapSizeX() {
		return mapSizeX;
	}

	public DataModel<Integer> getMapSizeZ() {
		return mapSizeZ;
	}
	
	public ListModel<DataModel<Byte>> getTerrainGroup() {
		return terrainGroup;
	}
	
	public ListModel<DataModel<Byte>> getTerrainData() {
		return terrainData;
	}
	
	public ListModel<DataModel<Byte>> getUnknown() {
		return unknown;
	}
	
	public ListModel<DataModel<Color>> getWaterColour() {
		return waterColour;
	}
	
	public ListModel<DataModel<Byte>> getWaterType() {
		return waterType;
	}
	
	public ListModel<DataModel<Float>> getTerrainHeight() {
		return terrainHeight;
	}
	
	public ListModel<DataModel<Float>> getWaterHeight() {
		return waterHeight;
	}

	@Override
	public void readAllModels(List<Byte> data, int offsetHint) {
		mapSizeX.readAllModels(data, offsetHint);
		mapSizeZ.readAllModels(data, offsetHint);
		
		int tiles = mapSizeX.getValue() * mapSizeZ.getValue();
		
		terrainGroup.getChildModels().clear();
		for(int i = 0; i < tiles; i++) {
			terrainGroup.getChildModels().add(terrainGroup.getChildFactory().apply(terrainGroup));
		}
		terrainGroup.readAllModels(data, offsetHint);
		
		terrainData.getChildModels().clear();
		for(int i = 0; i < tiles; i++) {
			terrainData.getChildModels().add(terrainData.getChildFactory().apply(terrainData));
		}
		terrainData.readAllModels(data, offsetHint);
		
		unknown.getChildModels().clear();
		for(int i = 0; i < tiles; i++) {
			unknown.getChildModels().add(unknown.getChildFactory().apply(unknown));
		}
		unknown.readAllModels(data, offsetHint);
		
		int nodes = (mapSizeX.getValue() + 1) * (mapSizeZ.getValue() + 1);
		
		waterColour.getChildModels().clear();
		for(int i = 0; i < nodes; i++) {
			waterColour.getChildModels().add(waterColour.getChildFactory().apply(waterColour));
		}
		waterColour.readAllModels(data, offsetHint);
		
		waterType.getChildModels().clear();
		for(int i = 0; i < tiles; i++) {
			waterType.getChildModels().add(waterType.getChildFactory().apply(waterType));
		}
		waterType.readAllModels(data, offsetHint);
		
		terrainHeight.getChildModels().clear();
		for(int i = 0; i < nodes; i++) {
			terrainHeight.getChildModels().add(terrainHeight.getChildFactory().apply(terrainHeight));
		}
		terrainHeight.readAllModels(data, offsetHint);
		
		waterHeight.getChildModels().clear();
		for(int i = 0; i < nodes; i++) {
			waterHeight.getChildModels().add(waterHeight.getChildFactory().apply(waterHeight));
		}
		waterHeight.readAllModels(data, offsetHint);
	}

	public void writeAllModels(List<Byte> data, int offsetHint) {
		waterHeight.writeAllModels(data, offsetHint);
		terrainHeight.writeAllModels(data, offsetHint);
		waterType.writeAllModels(data, offsetHint);
		waterColour.writeAllModels(data, offsetHint);
		unknown.writeAllModels(data, offsetHint);
		terrainData.writeAllModels(data, offsetHint);
		terrainGroup.writeAllModels(data, offsetHint);
		mapSizeZ.writeAllModels(data, offsetHint);
		mapSizeX.writeAllModels(data, offsetHint);
	}

}
