package io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import editor.EditorContext;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ConversionHandler {
	
	private FileChooser scenarioFileChooser;
	private FileChooser extractedDataChooser;
	
	private Extractor extractor;
	private Compressor compressor;
	
	private Reader reader;
	private Writer writer;
	
	private EditorContext editorContext;

	public ConversionHandler(EditorContext editorContext) {
		this.editorContext = editorContext;
		
		extractor = new Extractor();
		compressor = new Compressor();
		
		reader = new Reader();
		writer = new Writer();
		
		scenarioFileChooser = new FileChooser();
		scenarioFileChooser.getExtensionFilters().add(new ExtensionFilter("Scenario files", "*.scx"));
		
		extractedDataChooser = new FileChooser();
		extractedDataChooser.getExtensionFilters().add(new ExtensionFilter("Extracted scenario file", "*.dat"));
	}
	
	public void extractScenario() {
		File inputFile = scenarioFileChooser.showOpenDialog(editorContext.getStage());
		if(inputFile == null) {
			return;
		}
		
		List<Byte> data;
		try {
			data = extractor.extract(inputFile);
		} catch (IOException e) {
			editorContext.getMessageDisplay().showError(e, "Failed to extract file.");
			return;
		}
		
		extractedDataChooser.setInitialDirectory(inputFile.getParentFile());
		extractedDataChooser.setInitialFileName(getNameWithoutFileExtension(inputFile));
		File outputFile = extractedDataChooser.showSaveDialog(editorContext.getStage());
		if(outputFile == null) {
			return;
		}
		
		try {
			writer.write(outputFile, data);
		} catch (IOException e) {
			editorContext.getMessageDisplay().showError(e, "Failed to export extracted file.");
		}
	}
	
	public void compressScenario() {
		File inputFile = extractedDataChooser.showOpenDialog(editorContext.getStage());
		if(inputFile == null) {
			return;
		}
		
		List<Byte> data;
		try {
			data = reader.read(inputFile);
		} catch (IOException e) {
			editorContext.getMessageDisplay().showError(e, "Failed to read file.");
			return;
		}
		
		scenarioFileChooser.setInitialDirectory(inputFile.getParentFile());
		scenarioFileChooser.setInitialFileName(getNameWithoutFileExtension(inputFile));
		File outputFile = scenarioFileChooser.showSaveDialog(editorContext.getStage());
		if(outputFile == null) {
			return;
		}
		
		try {
			compressor.write(outputFile, data);
		} catch (IOException e) {
			editorContext.getMessageDisplay().showError(e, "Failed to compress file.");
		}
	}
	
	private String getNameWithoutFileExtension(File file) {
		return file.getName().split("\\.")[0];
	}

}
