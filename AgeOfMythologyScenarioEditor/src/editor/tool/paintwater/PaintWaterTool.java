
package editor.tool.paintwater;

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
import editor.tool.EditorToolType;
import editor.waterchooser.WaterSelectionModel;
import editor.waterchooser.WaterViewer;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import mapmodel.map.MapSizeModel;
import utility.observable.Observer;
import water.WaterEntry;

public class PaintWaterTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private EditorContext editorContext;
   
   private BrushModelHolder brushModelHolder;
   
   private WaterOverlayCreator overlayViewCreator;
   private BrushView brushView;
   
   private WaterSelectionModel waterSelectionModel;
   private WaterViewer waterViewer;
   
   private Observer<Object> paintWaterObserver;
   private Observer<Object> stopPaintWaterObserver;
   
   public PaintWaterTool(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      brushModelHolder = new BrushModelHolder();
      
      overlayViewCreator = new WaterOverlayCreator(editorContext);
      
      brushView = BrushView.tilesBrushView(editorContext.getMainView(), brushModelHolder);
      editorContext.getMainView().getStackPane().getChildren().add(brushView.getNode());
      
      waterSelectionModel = new WaterSelectionModel();
      waterViewer = new WaterViewer(waterSelectionModel);
      
      paintWaterObserver = value -> handleTerrainPaint();
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DOWN, paintWaterObserver);
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DRAGGED, paintWaterObserver);
      stopPaintWaterObserver = value -> handleStopPaint();
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_UP, stopPaintWaterObserver);
   }
   
   @Override
   public Node getLeftGraphics() {
      return waterViewer.getTableView();
   }
   
   private void handleTerrainPaint() {
      WaterEntry waterEntry = waterSelectionModel.getSelectedWaterEntry();
      if (waterEntry == null) {
         return;
      }
      
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      List<DataModel<Byte>> waterTypeModels = mapSizeModel.getWaterType().getChildModels();
      List<DataModel<Color>> waterColourModels = mapSizeModel.getWaterColour().getChildModels();
      
      CommandExecutor commandExecutor = editorContext.getCommandExecutor();
      int mapWidth = mapSizeModel.getMapSizeX().getValue();
      int mapHeight = mapSizeModel.getMapSizeZ().getValue();
      BrushModel brushModel = brushModelHolder.getBrushModel();
      for (int z = brushModel.getPosZ(); z < brushModel.getPosZ() + brushModel.getHeight(); z++) {
         for (int x = brushModel.getPosX(); x < brushModel.getPosX() + brushModel.getWidth(); x++) {
            if (x >= 0 && z >= 0 && x < mapWidth && z < mapHeight) {
               int index = mapHeight * x + z;
               commandExecutor.addPart(new SetValueCommand<>(waterTypeModels.get(index), (byte) waterEntry.getIndex()));
               
               int colourIndex = (mapHeight + 1) * x + z;
               Color colour = waterEntry.getColour();
               commandExecutor.addPart(new SetValueCommand<>(waterColourModels.get(colourIndex), colour));
               commandExecutor.addPart(new SetValueCommand<>(waterColourModels.get(colourIndex + 1), colour));
               commandExecutor.addPart(new SetValueCommand<>(waterColourModels.get(colourIndex + mapHeight + 1), colour));
               commandExecutor.addPart(new SetValueCommand<>(waterColourModels.get(colourIndex + mapHeight + 2), colour));
            }
         }
      }
      
      overlayViewCreator.getOverlayView().requestRedraw();
   }
   
   private void handleStopPaint() {
      editorContext.getCommandExecutor().done();
   }
   
   @Override
   public void destroy() {
      brushView.destroy();
      editorContext.getMainView().getStackPane().getChildren().remove(brushView.getNode());
      
      overlayViewCreator.destroy();
   }
   
}
