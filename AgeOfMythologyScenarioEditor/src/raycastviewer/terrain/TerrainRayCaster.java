package raycastviewer.terrain;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javafx.scene.paint.Color;
import mapmodel.map.MapSizeModel;
import raycastviewer.RayCaster;
import raycastviewer.terrain.cache.TerrainRayCastingCache;
import raycastviewer.terrain.cache.TerrainRayCastingCacheHolder;
import terrain.TerrainEntry;
import terrain.TerrainGroup;
import terrain.TerrainType;
import utility.graphics.colour.ColourToArgb;
import utility.image.ImageLoaderEntryType;
import utility.math.Doubles;

public class TerrainRayCaster implements RayCaster {
	
	private static final int OUTSIDE_COLOUR = ColourToArgb.convert(Color.GREY);
	private static final double WORLD_TO_IMAGE = 16.0;
	private static final List<TerrainGroup> TERRAINS = TerrainType.getInstance().getTerrainGroups();
	
	private MapSizeModel mapSizeModel;
	
	private Map<ImageLoaderEntryType, TerrainRayCastingCacheHolder> cacheHolder;
	private TerrainRayCastingCacheHolder currentCacheHolder;
	
	public TerrainRayCaster(MapSizeModel mapSizeModel) {
		this.mapSizeModel = mapSizeModel;
		cacheHolder = new EnumMap<>(ImageLoaderEntryType.class); 
		cacheHolder.put(ImageLoaderEntryType.LARGE, new TerrainRayCastingCacheHolder(ImageLoaderEntryType.LARGE));
		cacheHolder.put(ImageLoaderEntryType.MEDIUM, new TerrainRayCastingCacheHolder(ImageLoaderEntryType.MEDIUM));
		cacheHolder.put(ImageLoaderEntryType.SMALL, new TerrainRayCastingCacheHolder(ImageLoaderEntryType.SMALL));
		cacheHolder.put(ImageLoaderEntryType.TINY, new TerrainRayCastingCacheHolder(ImageLoaderEntryType.TINY));
		
		currentCacheHolder = cacheHolder.get(ImageLoaderEntryType.LARGE);
	}
	
	public void setImageLoaderEntryType(ImageLoaderEntryType type) {
		currentCacheHolder = cacheHolder.get(type);
	}

	@Override
	public int cast(double worldX, double worldZ) {
		int tileX = (int) Math.floor(worldX / 2);
		if(tileX < 0 || tileX >= mapSizeModel.getMapSizeX().getValue()) {
			return OUTSIDE_COLOUR;
		}
		
		int tileZ = (int) Math.floor(worldZ / 2);
		if(tileZ < 0 || tileZ >= mapSizeModel.getMapSizeZ().getValue()) {
			return OUTSIDE_COLOUR;
		}
		
		int index = tileX * mapSizeModel.getMapSizeZ().getValue() + tileZ;
		Byte groupType = mapSizeModel.getTerrainGroup().getChildModels().get(index).getValue();
		Byte terrainType = mapSizeModel.getTerrainData().getChildModels().get(index).getValue();
		TerrainEntry terrainEntry = TERRAINS.get(groupType).getTerrainEntry().get(terrainType);
		TerrainRayCastingCache terrainRayCastingCache = currentCacheHolder.getCacheForImage(terrainEntry);
		return terrainRayCastingCache.getColour(
				(int) Doubles.positiveModulo(worldX * WORLD_TO_IMAGE, terrainRayCastingCache.getWidth()), 
				(int) Doubles.positiveModulo(-worldZ * WORLD_TO_IMAGE, terrainRayCastingCache.getHeight()));
	}

}
