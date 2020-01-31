package utility.image;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ImageLoader {
	
	public static Image loadImage(Class<?> rootClass, String file) {
		try {
			// JavaFx image reading not working for some reason - using awt and converting
			return SwingFXUtils.toFXImage(ImageIO.read(rootClass.getResourceAsStream(file)), null);
		} catch (IOException | IllegalArgumentException e) {
			return new Rectangle(32, 32, Color.RED).snapshot(null, null);
		}
	}

	public static ImageLoaderEntry loadImageGroup(Class<?> rootClass, String file) {
		try {
			// JavaFx image reading not working for some reason - using awt and converting
			BufferedImage largeImage = ImageIO.read(rootClass.getResourceAsStream(file));
			
			AffineTransformOp mediumTransform = new AffineTransformOp(AffineTransform.getScaleInstance(0.5, 0.5), AffineTransformOp.TYPE_BICUBIC);
			AffineTransformOp mediumTransformInverse = new AffineTransformOp(AffineTransform.getScaleInstance(2.0, 2.0), AffineTransformOp.TYPE_BICUBIC);
			BufferedImage mediumImage = mediumTransformInverse.filter(mediumTransform.filter(largeImage, null), null);
			
			AffineTransformOp smallTransform = new AffineTransformOp(AffineTransform.getScaleInstance(0.25, 0.25), AffineTransformOp.TYPE_BICUBIC);
			AffineTransformOp smallTransformInverse = new AffineTransformOp(AffineTransform.getScaleInstance(4.0, 4.0), AffineTransformOp.TYPE_BICUBIC);
			BufferedImage smallImage = smallTransformInverse.filter(smallTransform.filter(largeImage, null), null);
			
			AffineTransformOp tinyTransform = new AffineTransformOp(AffineTransform.getScaleInstance(0.125, 0.125), AffineTransformOp.TYPE_BICUBIC);
			AffineTransformOp tinyTransformInverse = new AffineTransformOp(AffineTransform.getScaleInstance(8.0, 8.0), AffineTransformOp.TYPE_BICUBIC);
			BufferedImage tinyImage = tinyTransformInverse.filter(tinyTransform.filter(largeImage, null), null);
			
			return new ImageLoaderEntry(
					SwingFXUtils.toFXImage(largeImage, null),
					SwingFXUtils.toFXImage(mediumImage, null),
					SwingFXUtils.toFXImage(smallImage, null),
					SwingFXUtils.toFXImage(tinyImage, null));
		} catch (IOException | IllegalArgumentException e) {
			return new ImageLoaderEntry(
					new Rectangle(32, 32, Color.RED).snapshot(null, null),
					new Rectangle(16, 16, Color.RED).snapshot(null, null),
					new Rectangle(8, 8, Color.RED).snapshot(null, null),
					new Rectangle(4, 4, Color.RED).snapshot(null, null));
		}
	}

}
