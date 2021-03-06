package context;

import handler.HandlerManager;
import model.command.CommandModel;

public class Context {
	
	private HandlerManager handlerManager;
	private CommandModel commandModel;
	
	public Context() {
		handlerManager = new HandlerManager();
		commandModel = new CommandModel();
	}
	
	public HandlerManager getHandlerManager() {
		return handlerManager;
	}
	
	public CommandModel getCommandModel() {
		return commandModel;
	}

}
