
package command;

import java.util.Iterator;
import java.util.LinkedList;

import editor.EditorContext;
import utility.observable.Observable;
import utility.observable.ObservableManager;
import utility.observable.ObservableManagerImpl;
import utility.observable.ObserverType;

public class CommandExecutor implements Observable {
   
   public static final ObserverType<Boolean> UNDO_AVAILABILITY_CHANGED = new ObserverType<>();
   public static final ObserverType<Boolean> REDO_AVAILABILITY_CHANGED = new ObserverType<>();
   
   private EditorContext editorContext;
   
   private LinkedList<Command> activeCommands;
   
   private LinkedList<LinkedList<Command>> undoStack;
   private LinkedList<LinkedList<Command>> redoStack;
   
   private boolean canUndo;
   private boolean canRedo;
   
   private ObservableManager observableManager;
   
   /**
    * Constructor.
    * @param editorContext The {@link EditorViewContext} which will be passed through to the {@link Command}s when they execute and undo.
    */
   public CommandExecutor(EditorContext editorContext) {
      this.editorContext = editorContext;
      
      activeCommands = new LinkedList<>();
      
      undoStack = new LinkedList<>();
      redoStack = new LinkedList<>();
      
      observableManager = new ObservableManagerImpl();
   }
   
   /**
    * Adds single {@link Command} from a series of {@link Command}s making up the next batch of changes. This {@link Command} is immediately executed.
    * @param command {@link Command} to add.
    */
   public void addPart(Command command) {
      if (!redoStack.isEmpty()) {
         redoStack.clear();
         updateRedoStateAndNotify(false);
      }
      command.run(editorContext);
      activeCommands.push(command);
      if (undoStack.isEmpty() && activeCommands.size() == 1) {
         updateUndoStateAndNotify(true);
      }
   }
   
   /**
    * Groups all of the previous {@link Command}s executed into a single {@link Command} group for the purposes of {@link #undo()} and
    * {@link #redo()}. This should be called when the data changes are considered complete and therefore should be grouped. Failing to call
    * {@link #done()} will result in the previous set of {@link Command}s being grouped with the next set of {@link Command}s and {@link #undo()} and
    * {@link #redo()} applying to both sets.
    */
   public void done() {
      if (activeCommands.isEmpty()) {
         return;
      }
      undoStack.push(activeCommands);
      activeCommands = new LinkedList<>();
   }
   
   /**
    * Determines if an {@link #undo()} can be performed. Calling {@link #undo()} blindly checking is permitted but will do nothing if {@link #undo()}
    * is not available.
    * @return True if calling {@link #undo()} will actually result in an undo being performed.
    */
   public boolean canUndo() {
      return canUndo;
   }
   
   /**
    * Undos the last group of {@link Command}s calling {@link Command#undo(EditorViewContext)} on each of the {@link Command}s in reverse order to how
    * they were run. If undo cannot be performed nothing will happen.
    */
   public void undo() {
      if (!canUndo()) {
         return;
      }
      done();
      LinkedList<Command> commands = undoStack.pop();
      Iterator<Command> iterator = commands.iterator();
      while (iterator.hasNext()) {
         iterator.next().undo(editorContext);
      }
      if (undoStack.isEmpty()) {
         updateUndoStateAndNotify(false);
      }
      redoStack.push(commands);
      if (redoStack.size() == 1) {
         updateRedoStateAndNotify(true);
      }
   }
   
   /**
    * Determines if an {@link #redo()} can be performed. Calling {@link #redo()} blindly checking is permitted but will do nothing if {@link #redo()}
    * is not available.
    * @return True if calling {@link #redo()} will actually result in a redo being performed.
    */
   public boolean canRedo() {
      return canRedo;
   }
   
   /**
    * Redos the last group of {@link Command}s that were undone. The {@link Command#run(EditorViewContext)} will be called on each {@link Command} in
    * the same order they were originally called. If redo cannot be performed then nothing will happen.
    */
   public void redo() {
      if (!canRedo()) {
         return;
      }
      done();
      LinkedList<Command> commands = redoStack.pop();
      Iterator<Command> iterator = commands.descendingIterator();
      while (iterator.hasNext()) {
         iterator.next().run(editorContext);
      }
      if (redoStack.isEmpty()) {
         updateRedoStateAndNotify(false);
      }
      undoStack.push(commands);
      if (undoStack.size() == 1) {
         updateUndoStateAndNotify(true);
      }
   }
   
   /**
    * Clears the undo and redo stacks and makes performing each of the operations unavailable. This should only ever be called when performing a major
    * operation such as replacing the file being shown where {@link #undo()} may not make conceptual sense or be practical.
    */
   public void reset() {
      activeCommands = new LinkedList<>();
      
      if (!undoStack.isEmpty()) {
         undoStack.clear();
         updateUndoStateAndNotify(false);
      }
      if (!redoStack.isEmpty()) {
         redoStack.clear();
         updateRedoStateAndNotify(false);
      }
   }
   
   /**
    * Updates the state of if {@link #undo()} can occur and notifies the new availability.
    * @param newState True if {@link #undo()} can occur.
    */
   private void updateUndoStateAndNotify(boolean newState) {
      canUndo = newState;
      observableManager.notifyObservers(UNDO_AVAILABILITY_CHANGED, newState);
   }
   
   /**
    * Updates the state of if {@link #redo()} can occur and notifies the new availability.
    * @param newState True if {@link #redo()} can occur.
    */
   private void updateRedoStateAndNotify(boolean newState) {
      canRedo = newState;
      observableManager.notifyObservers(REDO_AVAILABILITY_CHANGED, newState);
   }
   
   @Override
   public ObservableManager getObservableManager() {
      return observableManager;
   }
   
}
