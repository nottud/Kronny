package water;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class WaterType {
	
	private static final WaterType INSTANCE = new WaterType();

	public static WaterType getInstance() {
		return INSTANCE;
	}
	
	private int index = 0;
	
	private List<WaterEntry> waters;
	
	private WaterType() {
		waters = new ArrayList<>();
		
		addEntry("Greek River", "GRiver64.bmp", Color.rgb(0, 50, 170), 3.0);
		addEntry("Norse River", "NRiver64.bmp", Color.rgb(0, 50, 170), 3.0);
		addEntry("Tundra Pool", "NRiver64.bmp", Color.rgb(0, 50, 170), 3.0);
		addEntry("Yellow River", "ERiver64.bmp", Color.rgb(166, 129, 2), 4.0);
		addEntry("Yellow River Shallow", "EShallow64.bmp", Color.rgb(166, 129, 2), 1.24);
		addEntry("Egyptian Nile", "ERiver64.bmp", Color.rgb(0, 175, 235), 3.0);
		addEntry("Egyptian Nile Shallow", "EShallow64.bmp", Color.rgb(117, 168, 130), 1.24);
		addEntry("Savannah Water Hole", "ERiver64.bmp", Color.rgb(0, 175, 235), 3.0);
		addEntry("Styx River", "ERiver64.bmp", Color.rgb(2, 2, 2), 3.0);
		addEntry("Mediterranean Sea", "mediteranean sea 64.bmp", Color.rgb(0, 50, 170), 4.0);
		addEntry("Aegean Sea", "aegean sea 64.bmp", Color.rgb(0, 125, 225), 4.0);
		addEntry("Red Sea", "red sea 64.bmp", Color.rgb(25, 80, 170), 4.0);
		addEntry("North Atlantic Ocean", "north atlantic ocean 64.bmp", Color.rgb(0, 75, 255), 4.0);
		addEntry("Norwegian Sea", "norweigen sea 64.bmp", Color.rgb(0, 100, 220), 4.0);
		addEntry("Tundra", "tundra sea 64.bmp", Color.rgb(0, 100, 220), 4.0);
		addEntry("Old Atlantis Outter Sea", "aegean sea 64.bmp", Color.rgb(0, 50, 170), 4.0);
		addEntry("Old Atlantis Inner Sea", "aegean sea 64.bmp", Color.rgb(0, 125, 225), 4.0);
		addEntry("Yellow Sea", "tundra sea 64.bmp", Color.rgb(166, 129, 2), 4.0);
		addEntry("South Sea", "red sea 64.bmp", Color.rgb(25, 80, 170), 4.0);
		
		index = 255;
		addEntry("Remove Water", "icon building farm 64.bmp", Color.BLACK, 0.0);
	}
	
	private void addEntry(String name, String file, Color colour, double depth) {
		WaterEntry waterEntry = new WaterEntry(index, name, file, colour, depth);
		waters.add(waterEntry);
		index++;
	}
	
	public List<WaterEntry> getWaters() {
		return waters;
	}
	
}
