package editor.colourchooser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import utility.observable.Observer;

public class ColourSelectionView {
	
	private static final double GAP = 2.0;
	private static final double INCREMENT = 0.2;
	private static final int TILE_SIZE = 30;

	private static final List<Color> COLOURS = new ArrayList<>();
	
	static {
		for(double b = 0; b <= 1.0; b = b + INCREMENT) {
			for(double g = 0; g <= 1.0; g = g + INCREMENT) {
				for(double r = 0; r <= 1.0; r = r + INCREMENT) {
					COLOURS.add(new Color(r, g, b, 1.0));
				}
			}
		}
	}
	
	private ColourSelectionModel model;
	
	private Collection<Observer<Color>> observers;

	private BorderPane borderPane;
	
	private ColorPicker colourPicker;
	
	private FlowPane flowPane;
	private ScrollPane scrollPane;
	
	
	public ColourSelectionView(ColourSelectionModel model) {
		this.model = model;
		observers = new LinkedHashSet<>();
		
		colourPicker = new ColorPicker(model.getColour());
		colourPicker.setMaxWidth(Double.MAX_VALUE);
		colourPicker.setOnAction(event -> model.setColour(colourPicker.getValue()));
		model.getObservableManager().addObserver(ColourSelectionModel.COLOUR_CHANGED, newColour -> colourPicker.setValue(newColour));
		
		flowPane = new FlowPane();
		flowPane.setHgap(GAP);
		flowPane.setVgap(GAP);
		for(Color foundColour : COLOURS) {
			ToggleButton toggleButton = new ToggleButton();
			toggleButton.setBackground(new Background(new BackgroundFill(foundColour, null, null)));
			toggleButton.setPrefSize(TILE_SIZE, TILE_SIZE);
			toggleButton.setOnAction(event -> model.setColour(foundColour));
			model.getObservableManager().addObserver(ColourSelectionModel.COLOUR_CHANGED, newColour -> toggleButton.setSelected(foundColour.equals(newColour)));
			flowPane.getChildren().add(toggleButton);
		}
		
		scrollPane = new ScrollPane(flowPane);
		scrollPane.setFitToWidth(true);
		
		borderPane = new BorderPane();
		borderPane.setTop(colourPicker);
		borderPane.setCenter(scrollPane);
	}
	
	public BorderPane getNode() {
		return borderPane;
	}
	
	public void destroy() {
		for(Observer<Color> observer : observers) {
			model.getObservableManager().removeObserver(ColourSelectionModel.COLOUR_CHANGED, observer);
		}
	}
	
}
