package editor.brush;

public class BrushModelHolder {
	
	private BrushModel brushModel;
	private BrushMouseStateModel brushMouseStateModel;
	
	public BrushModelHolder() {
		brushModel = new BrushModel();
		brushMouseStateModel = new BrushMouseStateModel(brushModel);
	}
	
	public BrushModel getBrushModel() {
		return brushModel;
	}
	
	public BrushMouseStateModel getBrushMouseStateModel() {
		return brushMouseStateModel;
	}

}
