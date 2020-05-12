
package editor.tool.multiplayeroverride;

import editor.EditorContext;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import javafx.scene.Node;

public class MultiplayerOverrideTool implements EditorTool {
   
   public static final EditorToolType TOOL_TYPE = new EditorToolType();
   
   private MultiplayerOverrideToolGlobalData globalData;
   private MultiplayerOverrideToolPlayerData playerData;
   
   public MultiplayerOverrideTool(EditorContext editorContext) {
      globalData = new MultiplayerOverrideToolGlobalData(editorContext);
      playerData = new MultiplayerOverrideToolPlayerData(editorContext);
   }
   
   @Override
   public Node getLeftGraphics() {
      return globalData.getNode();
   }
   
   @Override
   public Node getBottomGraphics() {
      return playerData.getNode();
   }
   
   @Override
   public void destroy() {
      globalData.destroy();
      playerData.destroy();
   }
   
}
