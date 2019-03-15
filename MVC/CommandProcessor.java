package mvc;

import java.util.*;

public class CommandProcessor {

    Stack<Command> undoStack = new Stack<Command>();
    Stack<Command> redoStack = new Stack<Command>();

    private  static CommandProcessor cmmdProc = null;
    
    private CommandProcessor() {
    	
    }
    
    public static CommandProcessor makeCommandProcessor() {
    	if (cmmdProc ==  null) {
    		cmmdProc = new CommandProcessor();
    	}
    	return cmmdProc;
    }
    
    public void execute(Command cmmd) {
    	if (cmmd != null) {
    		cmmd.execute();
        	if(cmmd.isUndoable()){
        		undoStack.push(cmmd);
        		redoStack.clear();
        	}	
    	}
    	else {
    		System.out.println(" Command Processor is not Intitialized");
    	}
    }

    public void undo() {
    	if(!undoStack.isEmpty()) {
    		Command command = undoStack.pop();
    		command.undo();
            redoStack.push(command);
    	}
    	else {
    		System.out.println("Nothing to Undo");
    	}
    }

    public void redo() {
    	if(!redoStack.isEmpty()) {
    		Command command = redoStack.pop();
        	undoStack.push(command);
        	command.execute();	
    	}
    	else {
    		System.out.println("Nothing to Redo");
    	}
    }
}