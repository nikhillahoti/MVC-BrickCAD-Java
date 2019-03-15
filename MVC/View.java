package mvc;

import javax.swing.JPanel;
import java.util.Observer;

public abstract class View extends JPanel implements Observer  {
	protected Model model;
	
	public void setModel(Model model) {
		this.model = model;
		if(model != null)
			model.addObserver(this);
	}
	
	public Model getModel() {
		return model;
	}
}