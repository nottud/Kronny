
package editor.tool.elevation;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import utility.graphics.BlockListener;
import utility.graphics.validationinput.ValidationInputParserFloat;
import utility.graphics.validationinput.ValidationInputTextField;
import utility.observable.Observer;

public class ElevationRangeViewer {
   
   private ElevationHeightModel heightModel;
   
   private ValidationInputTextField<Float> bottomRangeTextField;
   private ValidationInputTextField<Float> topRangeTextField;
   private Slider opacitySlider;
   
   private BlockListener blockListener;
   private ValidationInputTextField<Float> heightTextField;
   private Observer<Float> heightObserver;
   
   private VBox vBox;
   
   public ElevationRangeViewer(ElevationRangeModel rangeModel, ElevationHeightModel heightModel) {
      this.heightModel = heightModel;
      
      bottomRangeTextField = new ValidationInputTextField<>(rangeModel.getBottomRange(), new ValidationInputParserFloat());
      bottomRangeTextField.getObservableManager().addObserver(bottomRangeTextField.getValueChangedObserverType(), rangeModel::setBottomRange);
      
      topRangeTextField = new ValidationInputTextField<>(rangeModel.getTopRange(), new ValidationInputParserFloat());
      topRangeTextField.getObservableManager().addObserver(topRangeTextField.getValueChangedObserverType(), rangeModel::setTopRange);
      
      opacitySlider = new Slider(0.0, 1.0, rangeModel.getOpacity());
      opacitySlider.valueProperty().addListener((source, oldValue, newValue) -> rangeModel.setOpacity(newValue.doubleValue()));
      
      blockListener = new BlockListener();
      heightTextField = new ValidationInputTextField<>(heightModel.getHeight(), new ValidationInputParserFloat());
      heightTextField.getObservableManager().addObserver(heightTextField.getValueChangedObserverType(),
            value -> blockListener.attemptBlockAndDo(() -> heightModel.setHeight(value)));
      heightObserver = value -> blockListener.attemptBlockAndDo(() -> heightTextField.setValue(value));
      heightModel.getObservableManager().addObserver(ElevationHeightModel.HEIGHT_CHANGED, heightObserver);
      
      vBox = new VBox(
            new Label("Lowest display value"), bottomRangeTextField.getNode(),
            new Label("Highest display value"), topRangeTextField.getNode(),
            new Label("View opacity"), opacitySlider,
            new Label("Height"), heightTextField.getNode());
   }
   
   public VBox getNode() {
      return vBox;
   }
   
   public void destroy() {
      heightModel.getObservableManager().removeObserver(ElevationHeightModel.HEIGHT_CHANGED, heightObserver);
   }
   
}
