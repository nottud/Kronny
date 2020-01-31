package editor;

import command.CommandExecutor;
import editor.messagedisplay.MessageDisplay;
import editor.tool.EditorToolManager;
import javafx.stage.Stage;
import mapmodel.RootModel;

public class EditorContext {
	
	private Stage stage;
	private MessageDisplay messageDisplay;
	private FileModel fileModel;
	private RootModel rootModel;
	private CommandExecutor commandExecutor;
	private GraphicsRedrawHander redrawTerrainHander;
	private EditorToolManager editorToolManager;
	private MainView mainView;
	
	public EditorContext(Stage stage) {
		this.stage = stage;
		messageDisplay = new MessageDisplay(stage);
		fileModel = new FileModel();
		rootModel = new RootModel();
		commandExecutor = new CommandExecutor(this);
		redrawTerrainHander = new GraphicsRedrawHander();
		editorToolManager = new EditorToolManager(this);
		mainView = new MainView();
	}
		
	public Stage getStage() {
		return stage;	
	}
	
	public MessageDisplay getMessageDisplay() {
		return messageDisplay;
	}
	
	public FileModel getFileModel() {
		return fileModel;
	}
	
	public RootModel getRootModel() {
		return rootModel;
	}
	
	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
	
	public GraphicsRedrawHander getRedrawTerrainHander() {
		return redrawTerrainHander;
	}
	
	public EditorToolManager getEditorToolManager() {
		return editorToolManager;
	}
	
	public MainView getMainView() {
		return mainView;
	}

}
