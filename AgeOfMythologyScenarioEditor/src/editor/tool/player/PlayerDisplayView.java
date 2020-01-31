package editor.tool.player;

import datahandler.editor.DataModelEditorHolder;
import datahandler.editor.IntegerModelEditor;
import datahandler.editor.StringModelEditor;
import editor.EditorContext;
import editor.tool.EditorTool;
import editor.tool.EditorToolType;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import mapmodel.ListModel;
import mapmodel.RootModel;
import mapmodel.player.AllPlayersModel;
import mapmodel.player.PlayerModel;
import utility.observable.Observer;

public class PlayerDisplayView implements EditorTool {
	
	public static final EditorToolType TOOL_TYPE = new EditorToolType();

	private EditorContext editorContext;
	
	private DataModelEditorHolder editorHolder;

	private GridPane gridPane;
	private ScrollPane scrollPane;
	
	private Observer<Void> modelReadObserver;

	public PlayerDisplayView(EditorContext editorContext) {
		this.editorContext = editorContext;
		
		editorHolder = new DataModelEditorHolder();
		
		gridPane = new GridPane();
		scrollPane = new ScrollPane(gridPane);
		
		modelReadObserver = value -> createGraphics();
		editorContext.getRootModel().getObservableManager().addObserver(RootModel.MODEL_READ, modelReadObserver);
		createGraphics();
	}

	private void createGraphics() {
		editorHolder.destroyAllEditors();
		gridPane.getChildren().clear();
		AllPlayersModel allPlayersModel = editorContext.getRootModel().getAllPlayersModel();
		ListModel<PlayerModel> listModel = allPlayersModel.getPlayerModels();
		for (int i = 0; i < listModel.getChildModels().size(); i++) {
			createRow(listModel.getChildModels().get(i), i);
		}
	}

	private void createRow(PlayerModel playerModel, int index) {
		gridPane.addRow(index,
				editorHolder.add(new IntegerModelEditor(editorContext.getCommandExecutor(), playerModel.getUnknown1())).getEditor(),
				editorHolder.add(new IntegerModelEditor(editorContext.getCommandExecutor(), playerModel.getUnknown2())).getEditor(),
				editorHolder.add(new IntegerModelEditor(editorContext.getCommandExecutor(), playerModel.getPlayerId())).getEditor(),
				editorHolder.add(new StringModelEditor(editorContext.getCommandExecutor(), playerModel.getPlayerName())).getEditor(),
				editorHolder.add(new IntegerModelEditor(editorContext.getCommandExecutor(), playerModel.getUnknown3())).getEditor(),
				editorHolder.add(new IntegerModelEditor(editorContext.getCommandExecutor(), playerModel.getUnknown4())).getEditor(),
				editorHolder.add(new IntegerModelEditor(editorContext.getCommandExecutor(), playerModel.getUnknown5())).getEditor(),
				editorHolder.add(new IntegerModelEditor(editorContext.getCommandExecutor(), playerModel.getUnknown6())).getEditor());
	}
	
	@Override
	public Node getBottomGraphics() {
		return scrollPane;
	}

	@Override
	public void destroy() {
		editorContext.getRootModel().getObservableManager().removeObserver(RootModel.MODEL_READ, modelReadObserver);
		editorHolder.destroyAllEditors();
	}

}
