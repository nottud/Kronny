
package editor.brush.handler;

import java.util.List;
import java.util.function.UnaryOperator;

import command.CommandExecutor;
import command.SetValueCommand;
import datahandler.DataModel;
import editor.EditorContext;
import editor.brush.BrushModel;
import editor.brush.BrushModelHolder;
import editor.brush.BrushMouseStateModel;
import mapmodel.map.MapSizeModel;
import utility.observable.Observer;

public class GeneralBrushHandler<T> {
   
   private EditorContext editorContext;
   private BrushModelHolder brushModelHolder;
   private List<DataModel<T>> dataModel;
   private UnaryOperator<T> updateOperation;
   private boolean vertices;
   
   private Observer<Object> paintObserver;
   private Observer<Object> stopPaintObserver;
   
   public GeneralBrushHandler(EditorContext editorContext, BrushModelHolder brushModelHolder, List<DataModel<T>> dataModel,
         UnaryOperator<T> updateOperation, boolean vertices) {
      this.editorContext = editorContext;
      this.brushModelHolder = brushModelHolder;
      this.dataModel = dataModel;
      this.updateOperation = updateOperation;
      this.vertices = vertices;
      
      paintObserver = value -> handlePaint();
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DOWN, paintObserver);
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_DRAGGED, paintObserver);
      stopPaintObserver = value -> handleStopPaint();
      brushModelHolder.getBrushMouseStateModel().getObservableManager().addObserver(BrushMouseStateModel.BRUSH_UP, stopPaintObserver);
   }
   
   private void handlePaint() {
      CommandExecutor commandExecutor = editorContext.getCommandExecutor();
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      int mapWidth = mapSizeModel.getMapSizeX().getValue();
      int mapHeight = mapSizeModel.getMapSizeZ().getValue();
      if (vertices) {
         mapWidth++;
         mapHeight++;
      }
      BrushModel brushModel = brushModelHolder.getBrushModel();
      for (int z = brushModel.getPosZ(); z < brushModel.getPosZ() + brushModel.getHeight(); z++) {
         for (int x = brushModel.getPosX(); x < brushModel.getPosX() + brushModel.getWidth(); x++) {
            if (x >= 0 && z >= 0 && x < mapWidth && z < mapHeight) {
               int index = mapHeight * x + z;
               T value = updateOperation.apply(dataModel.get(index).getValue());
               commandExecutor.addPart(new SetValueCommand<>(dataModel.get(index), value));
            }
         }
      }
   }
   
   private void handleStopPaint() {
      editorContext.getCommandExecutor().done();
   }
   
   public void destroy() {
      brushModelHolder.getBrushMouseStateModel().getObservableManager().removeObserver(BrushMouseStateModel.BRUSH_DOWN, paintObserver);
      brushModelHolder.getBrushMouseStateModel().getObservableManager().removeObserver(BrushMouseStateModel.BRUSH_DRAGGED, paintObserver);
      brushModelHolder.getBrushMouseStateModel().getObservableManager().removeObserver(BrushMouseStateModel.BRUSH_UP, stopPaintObserver);
   }
   
}
