
package editor;

import java.io.File;

public class StageTitleProvider {
   
   private static final String TITLE_PREFIX = "Kronny - ";
   
   private EditorContext editorContext;
   
   public StageTitleProvider(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      FileModel fileModel = editorContext.getFileModel();
      setTitle(fileModel.getFile());
      fileModel.getObservableManager().addObserver(FileModel.FILE_CHANGED, this::setTitle);
   }
   
   private void setTitle(File file) {
      if (file == null) {
         editorContext.getStage().setTitle(TITLE_PREFIX + "No scenario loaded");
      } else {
         editorContext.getStage().setTitle(TITLE_PREFIX + file.getName());
      }
   }
   
}
