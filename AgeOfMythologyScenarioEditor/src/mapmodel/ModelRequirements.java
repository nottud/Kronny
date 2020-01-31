package mapmodel;

import java.util.List;

public interface ModelRequirements {
	
	public void readAllModels(List<Byte> data, int offsetHint);
	
	public void writeAllModels(List<Byte> data, int offsetHint);

}