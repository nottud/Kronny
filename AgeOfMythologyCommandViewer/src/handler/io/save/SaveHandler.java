package handler.io.save;

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

public class SaveHandler implements Handler<Pair<Context, Window>> {

	public static final HandlerType<Pair<Context, Window>> HANDLER_TYPE = new HandlerType<>(SaveHandler::new);

	private FileChooser fileChooser;
	private SaveToXml saveToXml;
	private MessageDisplay messageDisplay;

	public SaveHandler() {
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("XML Files", "*.xml"));
		saveToXml = new SaveToXml();
		messageDisplay = new MessageDisplay();
	}

	@Override
	public void handle(Pair<Context, Window> value) {
		File chosenFile = fileChooser.showSaveDialog(value.getValue());
		if(chosenFile != null) {
			try {
				saveToXml.save(value.getKey().getCommandModel(), chosenFile);
			} catch (JAXBException | SAXException e) {
				messageDisplay.showError(value.getValue(), e, "Failed to export to XML.");
			}
		}
	}

}
