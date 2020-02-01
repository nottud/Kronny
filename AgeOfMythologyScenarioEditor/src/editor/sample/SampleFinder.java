
package editor.sample;

import java.util.OptionalInt;

import camera.CameraConverter;
import editor.EditorContext;
import mapmodel.map.MapSizeModel;

public class SampleFinder {
   
   private EditorContext editorContext;
   
   public SampleFinder(EditorContext editorContext) {
      this.editorContext = editorContext;
   }
   
   public OptionalInt findValue(double screenX, double screenY, boolean vertices) {
      MapSizeModel mapSizeModel = editorContext.getRootModel().getMapSizeModel();
      int mapWidth = mapSizeModel.getMapSizeX().getValue();
      int mapHeight = mapSizeModel.getMapSizeZ().getValue();
      double offset = 0.0;
      
      if (vertices) {
         mapWidth = mapWidth + 1;
         mapHeight = mapHeight + 1;
         offset = 1.0;
      }
      
      CameraConverter cameraConverter = editorContext.getMainView().getCameraConverter();
      int x = (int) Math.floor((cameraConverter.toWorldX(screenX) - offset) / 2.0);
      int z = (int) Math.floor((cameraConverter.toWorldZ(screenY) - offset) / 2.0);
      
      if (x >= 0 && z >= 0 && x < mapWidth && z < mapHeight) {
         int index = mapHeight * x + z;
         
         return OptionalInt.of(index);
      } else {
         return OptionalInt.empty();
      }
   }
   
}
