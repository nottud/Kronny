package handler.parse.java;

import java.io.File;
import java.io.IOException;
import java.util.List;

import context.Context;
import graphics.alert.MessageDisplay;
import handler.Handler;
import handler.HandlerType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import javafx.util.Pair;
import model.command.AomMethod;
import model.command.CommandModel;

public class FileParserHandler implements Handler<Pair<Context, Window>> {

	public static final HandlerType<Pair<Context, Window>> HANDLER_TYPE = new HandlerType<>(FileParserHandler::new);

	private FileChooser fileChooser;
	private FileParser fileParser;
	private MessageDisplay messageDisplay;

	public FileParserHandler() {
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Command Java File", "*.java"));
		fileParser = new FileParser();
		messageDisplay = new MessageDisplay();
	}

	@Override
	public void handle(Pair<Context, Window> value) {
		List<File> chosenFiles = fileChooser.showOpenMultipleDialog(value.getValue());
		if(chosenFiles == null) {
			return;
		}
		CommandModel commandModel = value.getKey().getCommandModel();
		try {
			for (File file : chosenFiles) {
				for(AomMethod method : fileParser.parse(file)) {
					commandModel.add(method);
				}
			}
		} catch (IOException e) {
			messageDisplay.showError(value.getValue(), e, "Failed to read methods from file.");
		}
	}

}
