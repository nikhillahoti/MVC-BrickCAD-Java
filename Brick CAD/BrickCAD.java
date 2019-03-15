
package bc;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import javax.swing.BorderFactory;

import javax.swing.JLabel;
import javax.swing.JTextField;

import mvc.*;

class DimensionView extends View { 

	JTextField heightText = new JTextField(10);
    JTextField lengthText = new JTextField(10);
    JTextField widthText = new JTextField(10);
    CommandProcessor commandProc = null;
	
	public DimensionView() {
		super();
		commandProc = CommandProcessor.makeCommandProcessor();
		setUI();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		Brick brick = (Brick) model;
		heightText.setText("" + brick.getHeight());
		lengthText.setText("" + brick.getLength());
		widthText.setText("" + brick.getWidth());
	}

	public void setUI() {
		this.setLayout(new GridLayout(3,2));
	        
	    this.add(new JLabel("Set Height"));
	    this.add(heightText);
	    this.add(new JLabel("Set Length"));
	    this.add(lengthText);
	    this.add(new JLabel("Set Width"));
	    this.add(widthText);
	    
	    heightText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = heightText.getText();
				if(checkValue(str)) {
					Command cmmd = new SetHeight(Double.parseDouble(str));
					cmmd.setUndoable(true);
		    		cmmd.setModel(model);
		    		commandProc.execute(cmmd);
				}				
			}
		});

	    widthText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = widthText.getText();
				if(checkValue(str)) {
					Command cmmd = new SetWidth(Double.parseDouble(str));
					cmmd.setUndoable(true);
		    		cmmd.setModel(model);
		    		commandProc.execute(cmmd);
				}		
			}
		});
	    
	    lengthText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = lengthText.getText();
				if(checkValue(str)) {
					Command cmmd = new SetLength(Double.parseDouble(str));
					cmmd.setUndoable(true);
		    		cmmd.setModel(model);
		    		commandProc.execute(cmmd);
				}
				
			}
		});
	}
	
	protected boolean checkValue(String str) {
		if(str == null || str.isEmpty() || Double.parseDouble(str.toString()) < 0) return false;
		return true;
	}
	
	protected void paintComponent(Graphics g) {
		Brick brick = (Brick) super.model;
		heightText.setText("" + brick.getHeight());
		lengthText.setText("" + brick.getLength());
		widthText.setText("" + brick.getWidth());
	}
}

class TopView extends View {
	private static final int X_Coor = 50;
	private static final int Y_Coor = 50;
	
	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
	      super.paintComponent(g);
	      g.setColor(Color.RED);
	      Brick brick = (Brick) getModel();
	      g.fillRect(X_Coor, Y_Coor, brick.getWidth().intValue(), brick.getLength().intValue());
	}
}

class FrontView extends View {
	private static final int X_Coor = 50;
	private static final int Y_Coor = 50;
	
	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
	      super.paintComponent(g);
	      g.setColor(Color.RED);
	      Brick brick = (Brick) getModel();
	      g.fillRect(X_Coor, Y_Coor, brick.getLength().intValue(), brick.getHeight().intValue());
	}
}

class SideView extends View {
	private static final int X_Coor = 50;
	private static final int Y_Coor = 50;
	
	@Override
	public void update(Observable o, Object arg) {		
		repaint();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	    g.setColor(Color.RED);
	    Brick brick = (Brick) getModel();
	    g.fillRect(X_Coor, Y_Coor, brick.getWidth().intValue(), brick.getHeight().intValue());
	}
}

class SetWidth extends Command {
	private Double newWidth;
	
	public SetWidth(Double newWidth) {
		this.newWidth = newWidth;
		this.setUndoable(false);
	}
	
	@Override
	public void execute() {
		super.execute(); // this creates a memento
		// brickModel = safe cast of inherited model to Brick
		Brick brickModel = (Brick) model;
		brickModel.setWidth(newWidth);
	}
}

class SetLength extends Command {
	private Double newLength;
	
	public SetLength(Double newLength) {
		this.newLength = newLength;
		this.setUndoable(false);
	}
	
	@Override
	public void execute() {
		super.execute(); // this creates a memento
		// brickModel = safe cast of inherited model to Brick
		Brick brickModel = (Brick) model;
		brickModel.setLength(newLength);
	}
}

class SetHeight extends Command {

	private Double newHeight;
	
	public SetHeight(Double newHeight) {
		this.newHeight = newHeight;
		this.setUndoable(false);
	}
	
	@Override
	public void execute() {
		super.execute(); // this creates a memento
		// brickModel = safe cast of inherited model to Brick
		Brick brickModel = (Brick) model;
		brickModel.setHeight(newHeight);
	}
}

class Brick extends Model {

	private static final long serialVersionUID = 1L;
	private Double height, width, length; // state
	
	public Brick(Double height, Double length, Double width) {
		this.height = height;
		this.length = length;
		this.width = width;
	}

	public void setWidth(Double newWidth) {
		width = newWidth;
		changed();
	}
	
	public Double getWidth() {
		return width;
	}
	
	public void setLength(Double newLength) {
		length = newLength;
		changed();
	}
	
	public Double getLength() {
		return length;
	}

	public void setHeight(Double newHeight) {
		height = newHeight;
		changed();
	}
	
	public Double getHeight() {
		return height;
	}

	private class BrickMemento implements Memento {
		public double height, width, length;
		
		public BrickMemento(double height, double width, double length) {
			this.height = height;
			this.width = width;
			this.length = length;
		}
	}

	public Memento makeMemento() { return new BrickMemento(height, width, length); }

	public void accept(Memento m) {
		// safe cast m as BrickMemento
		BrickMemento brickMemento = (BrickMemento) m;
		
		// restore height, etc.
		this.height = brickMemento.height;
		this.width = brickMemento.width;
		this.length = brickMemento.length;
		
		// Notify
		changed();
	}
}

class BrickFactory implements AppFactory {
	
	@Override
	public Model makeModel() {
		Model brick = new Brick(100.0, 50.0 , 80.0);
		brick.setUnsavedChanges(false);
		return brick;
	}

	@Override
	public View makeView(String cmmd) {
		View view = null;
		switch(cmmd) {
			case "Left Side View":
			case "Right Side View":
				view = new SideView();
				break;
		
			case "Dimension View":
				view = new DimensionView();
				view.setBorder(BorderFactory.createTitledBorder("Attributes"));
				break;
			
			case "Front View":
				view = new FrontView();
				break;
				
			case "Top View":
				view = new TopView();
				break;
		}
		view.setPreferredSize(new Dimension(200, 200));
		return view;
	}

	@Override
	public Command makeCommand(String type) {
		
		Command cmmd = null;
		String newVal;
		switch (type) {
			case "Set Width":
				newVal = Utilities.askUser("Enter the width");
				if( validInput(newVal)) 
					cmmd = new SetWidth(Double.parseDouble(newVal));	
				else 
					Utilities.informUser("Invalid Input!");
				break;
	
			case "Set Height":
				newVal = Utilities.askUser("Enter the heigth");
				if( validInput(newVal))
					cmmd = new SetHeight(Double.parseDouble(newVal));
				else 
					Utilities.informUser("Invalid Input!");
				break;
	
			case "Set Length":
				newVal = Utilities.askUser("Enter the Length");
				if( validInput(newVal))
					cmmd = new SetLength(Double.parseDouble(newVal));
				else 
					Utilities.informUser("Invalid Input!");
				break;
		}
		return cmmd;
	}
	
	private boolean validInput(String str) {
		if(str == null || str.isEmpty() || Double.parseDouble(str) < 1) return false;
		return true;
	}

	@Override
	public String[] getViews() {
		return new String [] {"Left Side View", "Right Side View", "Top View", "Front View", "Dimension View"};
	}

	@Override
	public String[] getCommands() {
		return new String [] {"Set Height", "Set Width", "Set Length", "Undo", "Redo"};
	}

	@Override
	public String getTitle() {
		return "Brick CAD";
	}

	@Override
	public String getHelp() {
		return "Use Edit to change Dimensions, View to open up New Views and File menu for various other options.";
	}

	@Override
	public String about() {
		return "Brick CAD Project!";
	}
}

public class BrickCAD {
	public static void main(String args[]) {
		MVCApp.run(new BrickFactory());
	}
}