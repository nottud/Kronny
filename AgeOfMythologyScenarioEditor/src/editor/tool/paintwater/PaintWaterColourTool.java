
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
import editor.colourchooser.ColourSelectionModel;
import editor.colourchooser.ColourSelectionView;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import mapmodel.map.MapSizeModel;
import utility.observable.Observer;

public class PaintWaterColourTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private EditorContext editorContext;
   
   private BrushModelHolder brushModelHolder;
   
   private WaterColourOverlayCreator overlayViewCreator;
   private BrushView brushView;
   
   private ColourSelectionModel colourSelectionModel;
   private ColourSelectionView colourSelectionView;
   
   private Observer<Object> paintWaterObserver;
   private Observer<Object> stopPaintWaterObserver;
   
   public PaintWaterColourTool(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      brushModelHolder = new BrushModelHolder();
      
      overlayViewCreator = new WaterColourOverlayCreator(editorContext);
      
      brushView = BrushView.verticesBrushView(editorContext.getMainView(), brushModelHolder);
      editorContext.getMainView().getStackPane().getChildren().add(brushView.getNode());
      
      colourSelectionModel = new ColourSelectionModel();
      colourSelectionView = new ColourSelectionView(colourSelectionModel);
      
      paintWaterObserver = value -> handlePaint();
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DOWN, paintWaterObserver);
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DRAGGED, paintWaterObserver);
      stopPaintWaterObserver = value -> handleStopPaint();
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_UP, stopPaintWaterObserver);
   }
   
   @Override
   public Node getLeftGraphics() {
      return colourSelectionView.getNode();
   }
   
   private void handlePaint() {
      Color colour = colourSelectionModel.getColour();
      if (colour == null) {
         return;
      }
      
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      List<DataModel<Color>> waterColourModels = mapSizeModel.getWaterColour().getChildModels();
      
      CommandExecutor commandExecutor = editorContext.getCommandExecutor();
      int mapWidth = mapSizeModel.getMapSizeX().getValue();
      int mapHeight = mapSizeModel.getMapSizeZ().getValue();
      BrushModel brushModel = brushModelHolder.getBrushModel();
      for (int z = brushModel.getPosZ(); z < brushModel.getPosZ() + brushModel.getHeight(); z++) {
         for (int x = brushModel.getPosX(); x < brushModel.getPosX() + brushModel.getWidth(); x++) {
            if (x >= 0 && z >= 0 && x <= mapWidth && z <= mapHeight) {
               int index = (mapHeight + 1) * x + z;
               commandExecutor.addPart(new SetValueCommand<>(waterColourModels.get(index), colour));
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
      
      colourSelectionView.destroy();
   }
   
}
