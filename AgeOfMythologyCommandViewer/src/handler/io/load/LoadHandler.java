package handler.io.load;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import context.Context;
import graphics.alert.MessageDisplay;
import handler.Handler;
import handler.HandlerType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.util.Pair;

public class LoadHandler implements Handler<Pair<Context, Window>> {

	public static final HandlerType<Pair<Context, Window>> HANDLER_TYPE = new HandlerType<>(LoadHandler::new);

	private FileChooser fileChooser;
	private LoadFromXml loadFromXml;
	private MessageDisplay messageDisplay;

	public LoadHandler() {
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
		loadFromXml = new LoadFromXml();
		messageDisplay = new MessageDisplay();
	}

	@Override
	public void handle(Pair<Context, Window> value) {
		File chosenFile = fileChooser.showOpenDialog(value.getValue());
		if (chosenFile != null) {
			try {
				loadFromXml.load(value.getKey().getCommandModel(), chosenFile);
			} catch (JAXBException | SAXException e) {
				messageDisplay.showError(value.getValue(), e, "Failed to load from XML.");
			}
		}
	}
}
