package editor.tool.paintwater;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import datahandler.DataModel;
import editor.EditorContext;
import editor.overlay.OverlayView;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import mapmodel.map.MapSizeModel;
import utility.observable.Observer;

public class WaterColourOverlayCreator {
	
	private EditorContext editorContext;
	private Map<DataModel<Color>, Observer<Color>> observers;
	private OverlayView overlayView;
	
	public WaterColourOverlayCreator(EditorContext editorContext) {
		this.editorContext = editorContext;
		observers = new LinkedHashMap<>();
		overlayView = OverlayView.verticesTileOverlayView(editorContext, editorContext.getMainView().getCameraConverter(), 1);
		editorContext.getMainView().getStackPane().getChildren().add(overlayView.getNode());
		
		MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
		int index = 0;
		int width = mapSizeModel.getMapSizeX().getValue();
		int height = mapSizeModel.getMapSizeZ().getValue();
		if(width <= 0 || height <= 0) {
			return;
		}
		List<DataModel<Color>> waterColour = mapSizeModel.getWaterColour().getChildModels();
		for(int x = 0; x <= width; x++) {
			for(int y = 0; y <= height; y++) {
				final int posX = x;
				final int posY = y;
				DataModel<Color> dataModel = waterColour.get(index);
				observers.put(dataModel, dataModel.addValueObserverAndImmediatelyNotify(foundColour -> updatePreview(posX, posY, foundColour)));
				index++;
			}
		}
		overlayView.requestRedraw();
	}
	
	private void updatePreview(int x, int y, Color colour) {
		PixelWriter pixelWriter = overlayView.getImage().getPixelWriter();
		pixelWriter.setColor(x, y, new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), 0.5));
		overlayView.requestRedraw();
	}
	
	public OverlayView getOverlayView() {
		return overlayView;
	}
	
	public void destroy() {
		overlayView.destroy();
		editorContext.getMainView().getStackPane().getChildren().remove(overlayView.getNode());
		
		for(Entry<DataModel<Color>, Observer<Color>> entry : observers.entrySet()) {
			entry.getKey().getObservableManager().removeObserver(entry.getKey().getValueChangedObserverType(), entry.getValue());
		}
		observers.clear();
	}

}
