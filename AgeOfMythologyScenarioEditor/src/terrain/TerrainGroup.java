package terrain;

import java.util.List;

public class TerrainGroup {
	
	private int index;
	private String groupName;
	private List<TerrainEntry> terrainEntries;

	public TerrainGroup(int index, String groupName, List<TerrainEntry> terrainEntries) {
		this.index = index;
		this.groupName = groupName;
		this.terrainEntries = terrainEntries;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public List<TerrainEntry> getTerrainEntry() {
		return terrainEntries;
	}

}
