package editor.tool.elevation;

import java.util.List;

import command.CommandExecutor;
import command.SetValueCommand;
import datahandler.DataModel;
import editor.EditorContext;
import editor.brush.BrushModel;
import editor.brush.BrushModelHolder;
import editor.brush.BrushMouseStateModel;
import editor.brush.view.BrushView;
import editor.tool.EditorTool;
import javafx.scene.Node;
import mapmodel.map.MapSizeModel;
import utility.observable.Observer;

public class BaseElevationTool implements EditorTool {
	
	private EditorContext editorContext;
	private List<DataModel<Float>> dataModel;
	
	private ElevationRangeModel rangeModel;
	private ElevationHeightModel heightModel;
	private ElevationOverlayCreator overlayCreator;
	
	private BrushModelHolder brushModelHolder;
	
	private ElevationRangeViewer elevationRangeViewer;
	private BrushView brushView;

	private Observer<Object> paintWaterObserver;
	private Observer<Object> stopPaintWaterObserver;

	
	public BaseElevationTool(EditorContext editorContext, List<DataModel<Float>> dataModel) {
		this.editorContext = editorContext;
		this.dataModel = dataModel;
		rangeModel = new ElevationRangeModel();
		heightModel = new ElevationHeightModel();
		overlayCreator = new ElevationOverlayCreator(editorContext, dataModel, rangeModel);
		
		brushModelHolder = new BrushModelHolder();
		
		brushView = BrushView.verticesBrushView(editorContext.getMainView(), brushModelHolder);
		editorContext.getMainView().getStackPane().getChildren().add(brushView.getNode());
		
		elevationRangeViewer = new ElevationRangeViewer(rangeModel, heightModel);
		
		paintWaterObserver = value -> handlePaint();
		brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DOWN, paintWaterObserver);
		brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DRAGGED, paintWaterObserver);
		stopPaintWaterObserver = value -> handleStopPaint();
		brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_UP, stopPaintWaterObserver);
	}
	
	@Override
	public Node getLeftGraphics() {
		return elevationRangeViewer.getNode();
	}
	
	private void handlePaint() {
		float height = heightModel.getHeight();
		
		MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
		
		CommandExecutor commandExecutor = editorContext.getCommandExecutor();
		int mapWidth = mapSizeModel.getMapSizeX().getValue();
		int mapHeight = mapSizeModel.getMapSizeZ().getValue();
		BrushModel brushModel = brushModelHolder.getBrushModel();
		for(int z = brushModel.getPosZ(); z < brushModel.getPosZ() + brushModel.getHeight(); z++) {
			for(int x = brushModel.getPosX(); x < brushModel.getPosX() + brushModel.getWidth(); x++) {
				if(x >= 0 && z >= 0 && x <= mapWidth && z <= mapHeight) {
					int index = (mapHeight + 1) * x + z;
					commandExecutor.addPart(new SetValueCommand<>(dataModel.get(index), height));
				}
			}
		}
	}
	
	private void handleStopPaint() {
		editorContext.getCommandExecutor().done();
	}
	
	@Override
	public void destroy() {
		brushView.destroy();
		editorContext.getMainView().getStackPane().getChildren().remove(brushView.getNode());
		
		overlayCreator.destroy();
	}

}
