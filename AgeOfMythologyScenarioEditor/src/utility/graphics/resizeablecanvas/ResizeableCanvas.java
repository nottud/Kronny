package utility.graphics.resizeablecanvas;

import javafx.scene.canvas.Canvas;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

/**
 * Defines a {@link Canvas} that can be freely resized. The resize
 * implementation is identical to {@link Region}. The min and max height methods
 * need overriding rather than simply setting the values otherwise it does not
 * work.
 */
public class ResizeableCanvas extends Canvas implements Observable {

	public static final ObserverType<Void> RESIZED = new ObserverType<>();

	private ObservableManager observableManager;

	/**
	 * Constructor passing through the default width and height.
	 * 
	 * @param initialWidth
	 *            Initial width the canvas should take.
	 * @param initialHeight
	 *            Initial height the canvas should take.
	 */
	public ResizeableCanvas(double initialWidth, double initialHeight) {
		super(initialWidth, initialHeight);
		observableManager = new ObservableManagerImpl();
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public double minWidth(double width) {
		return 0;
	}

	@Override
	public double minHeight(double height) {
		return 0;
	}

	@Override
	public double maxWidth(double height) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public double maxHeight(double width) {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public void resize(double width, double height) {
		setWidth(width);
		setHeight(height);
		observableManager.notifyObservers(RESIZED, null);
	}

	@Override
	public ObservableManager getObservableManager() {
		return observableManager;
	}
}
