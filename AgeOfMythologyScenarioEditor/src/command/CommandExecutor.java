package command;

import java.util.Iterator;
import java.util.LinkedList;

import editor.EditorContext;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class CommandExecutor implements Observable {
	
	public static final ObserverType<Void> CAN_UNDO = new ObserverType<>();
	public static final ObserverType<Void> CANNOT_UNDO = new ObserverType<>();
	public static final ObserverType<Void> CAN_REDO = new ObserverType<>();
	public static final ObserverType<Void> CANNOT_REDO = new ObserverType<>();
	
	private EditorContext editorContext;
	
	private LinkedList<Command> activeCommands;
	
	private LinkedList<LinkedList<Command>> undoStack;
	private LinkedList<LinkedList<Command>> redoStack;
	
	private ObservableManager observableManager;
	
	public CommandExecutor(EditorContext editorContext) {
		this.editorContext = editorContext;
		
		activeCommands = new LinkedList<>();
		
		undoStack = new LinkedList<>();
		redoStack = new LinkedList<>();
		
		observableManager = new ObservableManagerImpl();
	}
	
	public void addPart(Command command) {
		if(!redoStack.isEmpty()) {
			redoStack.clear();
			observableManager.notifyObservers(CANNOT_REDO, null);
		}
		command.run(editorContext);
		activeCommands.push(command);
		if(undoStack.isEmpty() && activeCommands.size() == 1) {
			observableManager.notifyObservers(CAN_UNDO, null);
		}
	}
	
	public void done() {
		if(activeCommands.isEmpty()) {
			return;
		}
		undoStack.push(activeCommands);
		activeCommands = new LinkedList<>();
	}
	
	public boolean canUndo() {
		return !undoStack.isEmpty();
	}
	
	public void undo() {
		done();
		LinkedList<Command> commands = undoStack.pop();
		Iterator<Command> iterator = commands.iterator();
		while(iterator.hasNext()) {
			iterator.next().undo(editorContext);
		}
		if(undoStack.isEmpty()) {
			observableManager.notifyObservers(CANNOT_UNDO, null);
		}
		redoStack.push(commands);
		if(redoStack.size() == 1) {
			observableManager.notifyObservers(CAN_REDO, null);
		}
	}
	
	public boolean canRedo() {
		return !redoStack.isEmpty();
	}
	
	public void redo() {
		done();
		LinkedList<Command> commands = redoStack.pop();
		Iterator<Command> iterator = commands.descendingIterator();
		while(iterator.hasNext()) {
			iterator.next().run(editorContext);
		}
		if(redoStack.isEmpty()) {
			observableManager.notifyObservers(CANNOT_REDO, null);
		}
		undoStack.push(commands);
		if(undoStack.size() == 1) {
			observableManager.notifyObservers(CAN_UNDO, null);
		}
	}
	
	public void reset() {
		activeCommands = new LinkedList<>();
		
		if(!undoStack.isEmpty()) {
			undoStack.clear();
			observableManager.notifyObservers(CANNOT_UNDO, null);
		}
		if(!redoStack.isEmpty()) {
			redoStack.clear();
			observableManager.notifyObservers(CANNOT_REDO, null);
		}
	}
	
	@Override
	public ObservableManager getObservableManager() {
		return observableManager;
	}

}
