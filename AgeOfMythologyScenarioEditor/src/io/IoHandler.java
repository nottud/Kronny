
package io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import datahandler.location.LocationNotFoundException;
import editor.EditorContext;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class IoHandler {
   
   private EditorContext editorContext;
   
   private FileChooser fileChooser;
   private Extractor extractor;
   private Compressor compressor;
   
   public IoHandler(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      fileChooser = new FileChooser();
      fileChooser.getExtensionFilters().add(new ExtensionFilter("Scenario files", "*.scx"));
      
      extractor = new Extractor();
      compressor = new Compressor();
   }
   
   public void load() {
      File file = editorContext.getFileModel().getFile();
      if (file == null) {
         loadAs();
      } else {
         loadWithFile(file);
      }
   }
   
   public void loadAs() {
      File file = fileChooser.showOpenDialog(editorContext.getStage());
      if (file != null) {
         editorContext.getFileModel().setFile(file);
         loadWithFile(file);
      }
   }
   
   private void loadWithFile(File file) {
      editorContext.getCommandExecutor().reset();
      try {
         editorContext.getFileModel().setData(extractor.extract(file));
      } catch (IOException e) {
         editorContext.getMessageDisplay().showError(e, "Reading in file failed.");
      }
   }
   
   public void save() {
      File file = editorContext.getFileModel().getFile();
      if (file != null) {
         saveWithFile(file);
      } else {
         saveAs();
      }
   }
   
   public void saveAs() {
      File file = fileChooser.showSaveDialog(editorContext.getStage());
      if (file != null) {
         editorContext.getFileModel().setFile(file);
         saveWithFile(file);
      }
   }
   
   private void saveWithFile(File file) {
      List<Byte> data = editorContext.getFileModel().getData();
      
      if (data != null) {
         try {
            editorContext.getRootModel().writeAllModels(data, 0);
            compressor.write(file, data);
         } catch (LocationNotFoundException ex) {
            editorContext.getMessageDisplay().showError(ex, "Failed to update scenario file data with editor model. Write failed.");
         } catch (IOException ex) {
            editorContext.getMessageDisplay().showError(ex, "Error writing data to file.");
         }
      }
   }
   
}
