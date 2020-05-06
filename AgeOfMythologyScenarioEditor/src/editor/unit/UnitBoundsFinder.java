
package editor.unit;

import camera.CameraConverter;
import javafx.geometry.BoundingBox;
import mapmodel.unit.UnitModel;

public class UnitBoundsFinder {
   
   public static final double UNIT_RADIUS = 1.0;
   public static final double UNIT_DIAMETRE = UNIT_RADIUS * 2;
   
   private CameraConverter cameraConverter;
   
   public UnitBoundsFinder(CameraConverter cameraConverter) {
      this.cameraConverter = cameraConverter;
   }
   
   public BoundingBox screenBounds(UnitModel unitModel) {
      double worldX = unitModel.getPosX().getValue() - UNIT_RADIUS;
      double worldZ = unitModel.getPosZ().getValue() + UNIT_RADIUS;
      
      double screenX = cameraConverter.toScreenX(worldX);
      double screenY = cameraConverter.toScreenY(worldZ);
      
      return new BoundingBox(screenX, screenY, cameraConverter.vectorToScreenX(UNIT_DIAMETRE), cameraConverter.vectorToScreenY(UNIT_DIAMETRE));
   }
   
}
