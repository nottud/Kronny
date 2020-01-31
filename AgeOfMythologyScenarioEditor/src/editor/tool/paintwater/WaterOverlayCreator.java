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

public class WaterOverlayCreator {
	
	private static final byte NO_WATER = (byte) 0xff;
	
	private EditorContext editorContext;
	private Map<DataModel<Color>, Observer<Color>> colourObservers;
	private Map<DataModel<Byte>, Observer<Byte>> typeObservers;
	private OverlayView overlayView;
	
	public WaterOverlayCreator(EditorContext editorContext) {
		this.editorContext = editorContext;
		colourObservers = new LinkedHashMap<>();
		typeObservers = new LinkedHashMap<>();
		overlayView = OverlayView.verticesTileOverlayView(editorContext, editorContext.getMainView().getCameraConverter(), 2);
		editorContext.getMainView().getStackPane().getChildren().add(overlayView.getNode());
		
		MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
		int width = mapSizeModel.getMapSizeX().getValue();
		int height = mapSizeModel.getMapSizeZ().getValue();
		if(width <= 0 || height <= 0) {
			return;
		}
		List<DataModel<Color>> waterColour = mapSizeModel.getWaterColour().getChildModels();
		List<DataModel<Byte>> waterType = mapSizeModel.getWaterType().getChildModels();
		
		int index = 0;
		for(int x = 0; x <= width; x++) {
			for(int y = 0; y <= height; y++) {
				final int posX = x;
				final int posY = y;
				DataModel<Color> waterColourModel = waterColour.get(index);
				colourObservers.put(waterColourModel, waterColourModel.addValueObserverAndImmediatelyNotify(foundColour -> updatePreview(posX, posY, width, height, waterType, foundColour)));
				index++;
			}
		}
		
		index = 0;
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				final int posX = x;
				final int posY = y;
				DataModel<Byte> waterTypeModel = waterType.get(index);
				typeObservers.put(waterTypeModel, waterTypeModel.addValueObserverAndImmediatelyNotify(foundType -> updateType(posX, posY, width, height, waterType, waterColour)));
				index++;
			}
		}
		overlayView.requestRedraw();
	}
	
	private void updatePreview(int x, int y, int width, int height, List<DataModel<Byte>> waterType, Color colour) {
		PixelWriter pixelWriter = overlayView.getImage().getPixelWriter();
		Color colourToDraw = new Color(colour.getRed(), colour.getGreen(), colour.getBlue(), 0.5);
		int doubleX = x * 2;
		int doubleY = y * 2;
		if(shouldDraw(doubleX, doubleY, width, height, waterType)){
			pixelWriter.setColor(doubleX, doubleY, colourToDraw);
		} else {
			pixelWriter.setColor(doubleX, doubleY, Color.TRANSPARENT);
		}
		if(shouldDraw(doubleX + 1, doubleY, width, height, waterType)){
			pixelWriter.setColor(doubleX + 1, doubleY, colourToDraw);
		} else {
			pixelWriter.setColor(doubleX + 1, doubleY, Color.TRANSPARENT);
		}
		if(shouldDraw(doubleX, doubleY + 1, width, height, waterType)){
			pixelWriter.setColor(doubleX, doubleY + 1, colourToDraw);
		} else {
			pixelWriter.setColor(doubleX, doubleY + 1, Color.TRANSPARENT);
		}
		if(shouldDraw(doubleX + 1, doubleY + 1, width, height, waterType)){
			pixelWriter.setColor(doubleX + 1, doubleY + 1, colourToDraw);
		} else {
			pixelWriter.setColor(doubleX + 1, doubleY + 1, Color.TRANSPARENT);
		}
		overlayView.requestRedraw();
	}
	
	private void updateType(int x, int y, int width, int height, List<DataModel<Byte>> waterType, List<DataModel<Color>> waterColour) {
		updatePreview(x, y, width, height, waterType, getColour(x, y, height, waterColour));
		updatePreview(x + 1, y, width, height, waterType, getColour(x + 1, y, height, waterColour));
		updatePreview(x, y + 1, width, height, waterType, getColour(x, y + 1, height, waterColour));
		updatePreview(x + 1, y + 1, width, height, waterType, getColour(x + 1, y + 1, height, waterColour));
	}
	
	private boolean shouldDraw(int imageX, int imageY, int width, int height, List<DataModel<Byte>> waterType) {
		int checkX = (imageX - 1) / 2;
		int checkY = (imageY - 1) / 2;
		if(imageX <= 0 || imageY <= 0 || checkX >= width || checkY >= height) {
			return false;
		} else {
			return waterType.get(checkX * height + checkY).getValue() != NO_WATER;
		}
	}
	
	private Color getColour(int x, int y, int height, List<DataModel<Color>> waterColour) {
		return waterColour.get(x * (height + 1) + y).getValue();
	}
	
	public OverlayView getOverlayView() {
		return overlayView;
	}
	
	public void destroy() {
		overlayView.destroy();
		editorContext.getMainView().getStackPane().getChildren().remove(overlayView.getNode());
		
		for(Entry<DataModel<Color>, Observer<Color>> entry : colourObservers.entrySet()) {
			entry.getKey().getObservableManager().removeObserver(entry.getKey().getValueChangedObserverType(), entry.getValue());
		}
		colourObservers.clear();
		
		for(Entry<DataModel<Byte>, Observer<Byte>> entry : typeObservers.entrySet()) {
			entry.getKey().getObservableManager().removeObserver(entry.getKey().getValueChangedObserverType(), entry.getValue());
		}
		typeObservers.clear();
	}

}
