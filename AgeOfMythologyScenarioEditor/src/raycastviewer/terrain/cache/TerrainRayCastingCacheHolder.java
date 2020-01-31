package raycastviewer.terrain.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import terrain.TerrainEntry;
import utility.image.ImageLoaderEntryType;

public class TerrainRayCastingCacheHolder {
	
	private ImageLoaderEntryType type;
	private Map<TerrainEntry, TerrainRayCastingCache> terrainTypeToCache;
	
	public TerrainRayCastingCacheHolder(ImageLoaderEntryType type) {
		this.type = type;
		terrainTypeToCache = new LinkedHashMap<>();
	}
	
	public TerrainRayCastingCache getCacheForImage(TerrainEntry terrainType) {
		TerrainRayCastingCache cache = terrainTypeToCache.get(terrainType);
		if(cache == null) {
			cache = new TerrainRayCastingCache(terrainType.loadOrGetImage().getImage(type));
			terrainTypeToCache.put(terrainType, cache);
		}
		return cache;
	}

}
