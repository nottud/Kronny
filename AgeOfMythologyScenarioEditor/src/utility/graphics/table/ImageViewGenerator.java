package utility.graphics.table;

import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ImageViewGenerator {
	
	public ImageView createImageView(TableColumn<?, ImageView> tableColumn, Image image) {
		ImageView imageView = new ImageView(image);
		setUpImageView(tableColumn, imageView);
		return imageView;
	}
	
	public ImageView createImageView(TableColumn<?, ImageView> tableColumn, Color colour) {
		ImageView imageView = new ImageView(new Rectangle(1, 1, colour).snapshot(null, null));
		setUpImageView(tableColumn, imageView);
		return imageView;
	}
	
	private void setUpImageView(TableColumn<?, ImageView> tableColumn, ImageView imageView) {
		imageView.setPreserveRatio(true);
		imageView.setFitWidth(tableColumn.getWidth());
		imageView.setFitHeight(tableColumn.getWidth());
		tableColumn.widthProperty().addListener((source, oldValue, newValue) -> {
			imageView.setFitWidth(newValue.doubleValue());
			imageView.setFitHeight(newValue.doubleValue());
		});
	}

}
