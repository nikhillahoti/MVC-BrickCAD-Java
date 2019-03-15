package mvc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.io.*;


public class MVCApp extends JFrame implements ActionListener {

	private JDesktopPane desktop;
	private AppFactory factory;
	private Model model;
	private CommandProcessor commandProcessor;

	public MVCApp(AppFactory factory) {

		this.factory = factory;
		this.model = factory.makeModel();
		this.commandProcessor = CommandProcessor.makeCommandProcessor();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		desktop = new JDesktopPane(); //a specialized layered pane
		
		// Adding behaviour to save state before exit
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Utilities.saveChanges(model);
			}
		});

		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset,screenSize.width  - inset*2,screenSize.height - inset*2);

		//create first "window" here (I forgot to do this)

		setContentPane(desktop);
		setJMenuBar(createMenuBar());

		//Make dragging a little faster but perhaps uglier.
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
	}

	protected JMenuBar createMenuBar() {
		// create a menu bar containing File, Edit, View, and Help menus
		JMenuBar jmb = new JMenuBar();
		
		String [] strFile = {"New", "Open", "Save", "Save As", "Help", "About", "Quit"};
		
		jmb.add(Utilities.makeMenu("File", strFile, this));
		jmb.add(Utilities.makeMenu("Edit", factory.getCommands(), new EditHandler()));
		jmb.add(Utilities.makeMenu("View", factory.getViews(), new ViewHandler()));
		
		return jmb;
	}

	public void actionPerformed(ActionEvent e){
    	String cmmd = e.getActionCommand();
    	if (cmmd == "Save") {
    		Utilities.save(model);
    	} else if (cmmd == "Save As") {
    		Utilities.saveAs(model);
    	} else if (cmmd == "Open") {
    		Model modell = Utilities.open();
    		if(modell != null) this.model = modell; 
    	} else if (cmmd == "New") {
    		Utilities.saveChanges(model);
    		model = factory.makeModel();    		
    	} else if (cmmd == "Quit") {
    		Utilities.saveChanges(model);
    		System.exit(1);
    	} else if (cmmd == "Help") {
    		Utilities.informUser(factory.getHelp());
    	} else if (cmmd == "About") {
    		Utilities.informUser(factory.about());
    	} else {
    		Utilities.error("Unrecognized command: " + cmmd);
    	}
    }

	// sort of works
	class ViewHandler implements ActionListener {
		public void actionPerformed(ActionEvent e){
			String cmmd = e.getActionCommand();
			View panel = factory.makeView(cmmd);
			panel.setModel(model);
			ViewFrame vf = new ViewFrame(panel);
			if(e.getActionCommand().contains("Top"))
				vf.setTitle("Top View");
			else 
				if(e.getActionCommand().contains("Front"))
					vf.setTitle("Front View");
				else
					if(e.getActionCommand().contains("Left"))
						vf.setTitle("Left Side View");
					else
						if(e.getActionCommand().contains("Right"))
							vf.setTitle("Right Side View");
						else
							vf.setTitle("Dimension View");
			vf.setVisible(true);
			desktop.add(vf);
			try {
				vf.setSelected(true);
			} catch (java.beans.PropertyVetoException ex) {}
		}
	}

    class EditHandler implements ActionListener {
    	public void actionPerformed(ActionEvent e){
        	// make a command and ask command processor to execute it
    		
    		switch(e.getActionCommand()) {
	    		case "Undo":
	    			commandProcessor.undo();
	    			break;
	    		
	    		case "Redo":
	    			commandProcessor.redo();
	    			break;
	    			
	    		default:
	    			Command command = factory.makeCommand(e.getActionCommand());
	    			command.setUndoable(true);
		    		command.setModel(model);
		    		commandProcessor.execute(command);
    		}
    	}
    }

    public static void run(AppFactory factory) {
    	try {
    		MVCApp app = new MVCApp(factory);
    		app.setSize(800,600);
			app.setTitle(factory.getTitle());
			app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			app.setVisible(true);
    	} catch(Exception e) {
    		Utilities.error("" + e);
    	}

    }

}