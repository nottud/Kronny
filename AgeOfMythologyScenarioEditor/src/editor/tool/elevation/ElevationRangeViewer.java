package editor.tool.elevation;

import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import utility.graphics.validationinput.ValidationInputParserFloat;
import utility.graphics.validationinput.ValidationInputTextField;

public class ElevationRangeViewer {
	
	private ElevationRangeModel rangeModel;
	private ElevationHeightModel heightModel;
	
	private ValidationInputTextField<Float> topRangeTextField;
	private ValidationInputTextField<Float> bottomRangeTextField;
	private Slider opacitySlider;
	private ValidationInputTextField<Float> heightTextField;
	
	private VBox vBox;


	public ElevationRangeViewer(ElevationRangeModel rangeModel, ElevationHeightModel heightModel) {
		this.rangeModel = rangeModel;
		this.heightModel = heightModel;
		
		topRangeTextField = new ValidationInputTextField<>(rangeModel.getTopRange(), new ValidationInputParserFloat());
		topRangeTextField.getObservableManager().addObserver(topRangeTextField.getValueChangedObserverType(), rangeModel::setTopRange);
		
		bottomRangeTextField = new ValidationInputTextField<>(rangeModel.getBottomRange(), new ValidationInputParserFloat());
		bottomRangeTextField.getObservableManager().addObserver(bottomRangeTextField.getValueChangedObserverType(), rangeModel::setBottomRange);
		
		opacitySlider = new Slider(0.0, 1.0, rangeModel.getOpacity());
		opacitySlider.valueProperty().addListener((source, oldValue, newValue) -> rangeModel.setOpacity(newValue.doubleValue()));
		
		heightTextField = new ValidationInputTextField<>(heightModel.getHeight(), new ValidationInputParserFloat());
		heightTextField.getObservableManager().addObserver(heightTextField.getValueChangedObserverType(), heightModel::setHeight);
		
		vBox = new VBox(topRangeTextField.getNode(), bottomRangeTextField.getNode(), opacitySlider, heightTextField.getNode());
	}
	
	public VBox getNode() {
		return vBox;
	}

}
