
package launcher;

import java.util.List;

import datahandler.location.LocationNotFoundException;
import editor.EditorContext;
import editor.EditorPane;
import editor.FileModel;
import editor.StageTitleProvider;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
   
   @Override
   public void start(Stage stage) throws Exception {
      EditorContext editorContext = new EditorContext(stage);
      
      new StageTitleProvider(editorContext);
      editorContext.getFileModel().getObservableManager().addObserver(FileModel.DATA_CHANGED,
            value -> readInData(editorContext, value));
      
      EditorPane editorPane = new EditorPane(editorContext);
      
      Scene scene = new Scene(editorPane.getNode(), 1024, 768);
      stage.setScene(scene);
      stage.show();
   }
   
   private void readInData(EditorContext editorContext, List<Byte> bytesList) {
      try {
         editorContext.getRootModel().readAllModels(bytesList, 0);
      } catch (LocationNotFoundException ex) {
         editorContext.getMessageDisplay().showError(ex,
               "Failed to read in scenario data to editor model. Ensure scenario is saved with latest version of the game and there are no custom "
                     + "terrains, water types, etc.");
      }
   }
   
   public static void main(String[] args) {
      Application.launch(args);
   }
}
