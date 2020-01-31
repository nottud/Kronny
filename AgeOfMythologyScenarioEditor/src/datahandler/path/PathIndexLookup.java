package datahandler.path;

public class PathIndexLookup implements PathElement {
	
	private int index;

	public PathIndexLookup(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

}
