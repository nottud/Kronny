package raycastviewer.terrain;

import java.util.function.Supplier;

import camera.CameraConverter;
import editor.GraphicsRedrawHander;
import mapmodel.RootModel;
import mapmodel.map.MapSizeModel;

public class ShowMapOnLoad {
	
	private static final double DEFAULT_ZOOM = 1.0 / 8.0;
	
	private RootModel rootModel;
	private CameraConverter cameraConverter;
	private GraphicsRedrawHander graphicsRedrawHander;
	private Supplier<Double> displayWidthProvider;
	private Supplier<Double> displayHeightProvider;

	public ShowMapOnLoad(RootModel rootModel, CameraConverter cameraConverter, GraphicsRedrawHander graphicsRedrawHander, Supplier<Double> displayWidthProvider, Supplier<Double> displayHeightProvider) {
		this.rootModel = rootModel;
		this.cameraConverter = cameraConverter;
		this.graphicsRedrawHander = graphicsRedrawHander;
		this.displayWidthProvider = displayWidthProvider;
		this.displayHeightProvider = displayHeightProvider;
		rootModel.getObservableManager().addObserver(RootModel.MODEL_READ, value -> handleModelLoaded());
	}
	
	private void handleModelLoaded() {
		MapSizeModel mapSizeModel = rootModel.getMapSizeModel();
		cameraConverter.setZoom(DEFAULT_ZOOM);
		cameraConverter.setWorldXAtOrigin(mapSizeModel.getMapSizeX().getValue() - cameraConverter.vectorToWorldX(displayWidthProvider.get() / 2));
		cameraConverter.setWorldZAtOrigin(mapSizeModel.getMapSizeZ().getValue() - cameraConverter.vectorToWorldZ(displayHeightProvider.get() / 2));
		graphicsRedrawHander.requestRedraw();
	}

}
