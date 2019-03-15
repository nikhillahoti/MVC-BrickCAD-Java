package mvc;

public abstract class Command {

    public Command() {
    	undoable = true;
    }

    private Boolean undoable;
    protected Model model;
    private Memento memento;

    public void execute(){
    	if(undoable)
    		this.memento = model.makeMemento();
    		//this.model.changed();
    }
    
    public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public void undo() {
    	//doubtful
        if(undoable && memento!=null) {
        	model.accept(memento);
        	model.changed();
        }
    }

	public Boolean isUndoable() {
		return this.undoable;
	}

	public void setUndoable(Boolean undoable) {
		this.undoable = undoable;
	}

}