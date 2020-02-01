
package editor.tool.paintterrain;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;

import command.CommandExecutor;
import command.RedrawTerrainCommand;
import command.SetValueCommand;
import datahandler.DataModel;
import editor.EditorContext;
import editor.brush.BrushModel;
import editor.brush.BrushModelHolder;
import editor.brush.BrushMouseStateModel;
import editor.brush.view.BrushView;
import editor.overlay.OverlayView;
import editor.sample.SampleFinder;
import editor.terrainchooser.TerrainSelectionModel;
import editor.terrainchooser.TerrainViewer;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import mapmodel.map.MapSizeModel;
import terrain.TerrainEntry;
import terrain.TerrainType;
import utility.observable.Observer;

public class TerrainTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private EditorContext editorContext;
   
   private BrushModelHolder brushModelHolder;
   private TerrainPreviewColourCache terrainPreviewColourCache;
   private Set<int[]> paintCoverage;
   
   private TerrainSelectionModel terrainSelectionModel;
   private TerrainViewer terrainViewer;
   
   private Observer<Object> paintTerrainObserver;
   private Observer<Object> stopPaintTerrainObserver;
   
   private OverlayView overlayView;
   private BrushView brushView;
   
   private SampleFinder sampleFinder;
   private EventHandler<MouseEvent> mouseClickedHandler;
   
   public TerrainTool(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      brushModelHolder = editorContext.getToolModels().getBrushModelHolder();
      terrainPreviewColourCache = new TerrainPreviewColourCache();
      paintCoverage = new LinkedHashSet<>();
      
      terrainSelectionModel = editorContext.getToolModels().getTerrainModels().getTerrainSelectionModel();
      terrainViewer = new TerrainViewer(terrainSelectionModel);
      
      paintTerrainObserver = value -> handleTerrainPaint();
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DOWN, paintTerrainObserver);
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DRAGGED, paintTerrainObserver);
      stopPaintTerrainObserver = value -> handleStopPaint();
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_UP, stopPaintTerrainObserver);
      
      overlayView = OverlayView.tileOverlayView(editorContext, editorContext.getMainView().getCameraConverter(), 2);
      editorContext.getMainView().getStackPane().getChildren().add(overlayView.getNode());
      
      brushView = BrushView.tilesBrushView(editorContext.getMainView(), brushModelHolder);
      editorContext.getMainView().getStackPane().getChildren().add(brushView.getNode());
      
      sampleFinder = new SampleFinder(editorContext);
      mouseClickedHandler = this::handleMouseClicked;
      editorContext.getMainView().getStackPane().addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickedHandler);
   }
   
   private void handleMouseClicked(MouseEvent mouseEvent) {
      if (MouseButton.SECONDARY.equals(mouseEvent.getButton())) {
         OptionalInt index = sampleFinder.findValue(mouseEvent.getX(), mouseEvent.getY(), false);
         if (!index.isPresent()) {
            return;
         }
         
         MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
         List<DataModel<Byte>> groupModels = mapSizeModel.getTerrainGroup().getChildModels();
         List<DataModel<Byte>> terrainModels = mapSizeModel.getTerrainData().getChildModels();
         
         TerrainEntry terrainEntry = TerrainType.getInstance()
               .getTerrainGroups().get(Byte.toUnsignedInt(groupModels.get(index.getAsInt()).getValue()))
               .getTerrainEntries().get(Byte.toUnsignedInt(terrainModels.get(index.getAsInt()).getValue()));
         terrainSelectionModel.setSelectedTerrainType(terrainEntry);
      }
   }
   
   @Override
   public Node getLeftGraphics() {
      return terrainViewer.getTableView();
   }
   
   private void handleTerrainPaint() {
      TerrainEntry terrainEntry = terrainSelectionModel.getSelectedTerrainEntry();
      if (terrainEntry == null) {
         return;
      }
      
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      
      int mapWidth = mapSizeModel.getMapSizeX().getValue();
      int mapHeight = mapSizeModel.getMapSizeZ().getValue();
      BrushModel brushModel = brushModelHolder.getBrushModel();
      PixelWriter pixelWriter = overlayView.getImage().getPixelWriter();
      Color previewColour = terrainPreviewColourCache.getColour(terrainEntry);
      for (int z = brushModel.getPosZ(); z < brushModel.getPosZ() + brushModel.getHeight(); z++) {
         for (int x = brushModel.getPosX(); x < brushModel.getPosX() + brushModel.getWidth(); x++) {
            if (x >= 0 && z >= 0 && x < mapWidth && z < mapHeight) {
               paintCoverage.add(new int[] {x, z});
               int dx = x * 2;
               int dz = z * 2;
               pixelWriter.setColor(dx, dz, previewColour);
               pixelWriter.setColor(dx + 1, dz, previewColour);
               pixelWriter.setColor(dx, dz + 1, previewColour);
               pixelWriter.setColor(dx + 1, dz + 1, previewColour);
            }
         }
      }
      
      overlayView.requestRedraw();
   }
   
   private void handleStopPaint() {
      TerrainEntry terrainEntry = terrainSelectionModel.getSelectedTerrainEntry();
      if (terrainEntry == null) {
         return;
      }
      
      Byte groupValue = (byte) terrainEntry.getGroupIndex();
      Byte indexValue = (byte) terrainEntry.getIndex();
      
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      
      List<DataModel<Byte>> groupModels = mapSizeModel.getTerrainGroup().getChildModels();
      List<DataModel<Byte>> terrainModels = mapSizeModel.getTerrainData().getChildModels();
      
      CommandExecutor commandExecutor = editorContext.getCommandExecutor();
      int mapHeight = mapSizeModel.getMapSizeZ().getValue();
      for (int[] location : paintCoverage) {
         int index = mapHeight * location[0] + location[1];
         commandExecutor.addPart(new SetValueCommand<>(groupModels.get(index), groupValue));
         commandExecutor.addPart(new SetValueCommand<>(terrainModels.get(index), indexValue));
      }
      paintCoverage.clear();
      
      commandExecutor.addPart(new RedrawTerrainCommand());
      editorContext.getCommandExecutor().done();
      
      Platform.runLater(this::clearOverlay);
   }
   
   private void clearOverlay() {
      overlayView.clear();
      overlayView.requestRedraw();
   }
   
   @Override
   public void destroy() {
      brushView.destroy();
      editorContext.getMainView().getStackPane().getChildren().remove(brushView.getNode());
      
      overlayView.destroy();
      editorContext.getMainView().getStackPane().getChildren().remove(overlayView.getNode());
      
      brushModelHolder.getBrushMouseStateModel().getObservableManager().removeObserver(BrushMouseStateModel.BRUSH_DOWN, paintTerrainObserver);
      brushModelHolder.getBrushMouseStateModel().getObservableManager().removeObserver(BrushMouseStateModel.BRUSH_DRAGGED, paintTerrainObserver);
      brushModelHolder.getBrushMouseStateModel().getObservableManager().removeObserver(BrushMouseStateModel.BRUSH_UP, stopPaintTerrainObserver);
      
      editorContext.getMainView().getStackPane().removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickedHandler);
   }
   
}
