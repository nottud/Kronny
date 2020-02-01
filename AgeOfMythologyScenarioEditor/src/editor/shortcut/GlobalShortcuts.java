
package editor.shortcut;

import editor.EditorContext;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;

public class GlobalShortcuts {
   
   private EditorContext editorContext;
   
   public GlobalShortcuts(EditorContext editorContext, Node topLevelNode) {
      this.editorContext = editorContext;
      
      topLevelNode.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressedEventFilter);
      topLevelNode.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressedEventHandler);
   }
   
   private void handleKeyPressedEventFilter(KeyEvent keyEvent) {
      switch (keyEvent.getCode()) {
         case ESCAPE:
            editorContext.getEditorToolManager().closeActiveTool();
            break;
         default:
            break;
      }
   }
   
   private void handleKeyPressedEventHandler(KeyEvent keyEvent) {
      switch (keyEvent.getCode()) {
         case Z:
            if (keyEvent.isControlDown()) {
               editorContext.getCommandExecutor().undo();
            }
            break;
         case Y:
            if (keyEvent.isControlDown()) {
               editorContext.getCommandExecutor().redo();
            }
            break;
         default:
            break;
      }
   }
   
}
