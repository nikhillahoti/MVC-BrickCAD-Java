package mvc;

import javax.swing.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utilities {

   public static JMenu makeMenu(String name, String[] items, ActionListener handler) {

	      JMenu result;
	      int j = name.indexOf('&');
	      if ( j != -1) {
	         char c = name.charAt(j + 1);
	         String s = name.substring(0, j) + name.substring(j + 1);
	         result = new JMenu(s);
	         result.setMnemonic(c);
	      } else {
	         result = new JMenu(name);
	      }

	      for(int i = 0; i < items.length; i++) {
	         if (items[i] == null) {
	            result.addSeparator();
	         } else {
	            j = items[i].indexOf('&');
	            JMenuItem item;
	            if ( j != -1) {
	               char c = items[i].charAt(j + 1);
	               String s = items[i].substring(0, j) +
	                  items[i].substring(j + 1);
	               item = new JMenuItem(s, items[i].charAt(j + 1));
	               item.setAccelerator(
	                  KeyStroke.getKeyStroke(c, InputEvent.CTRL_MASK));
	            } else { // no accelerator or shortcut key
	               item = new JMenuItem(items[i]);
	            }
	            item.addActionListener(handler);
	            result.add(item);
	         }
	         //result.addMenuListener(this);
	      }
	   return result;
   }

   public static String askUser(String query) {
   	   return JOptionPane.showInputDialog(query);
   }


   public static boolean confirm(String query) {
	   int result = JOptionPane.showConfirmDialog(null,
	             query, "choose one", JOptionPane.YES_NO_OPTION);
	   return result == 0; // or 1?
   }

   public static void error(String gripe) {
		JOptionPane.showMessageDialog(null,
			gripe,
			"OOPS!",
			JOptionPane.ERROR_MESSAGE);
   }

   public static void error(Exception gripe) {
	    gripe.printStackTrace();
		JOptionPane.showMessageDialog(null,
				gripe.toString(),
				"OOPS!",
				JOptionPane.ERROR_MESSAGE);
	   }

   public static void informUser(String info) {
	   JOptionPane.showMessageDialog(null, info,
	             "information", JOptionPane.INFORMATION_MESSAGE);
   }

   public static void saveChanges(Model model) {	   
	   if (model.hasUnsavedChanges() && Utilities.confirm("current model has unsaved changes, continue?"))
		  Utilities.save(model);
   }

   public static void save(Model model) {
	   JFileChooser fileChooser = new JFileChooser();
	   int response = fileChooser.showSaveDialog(null);
	   if(response == fileChooser.APPROVE_OPTION) {
		   try {
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile().toString()));
				os.writeObject(model);
				model.setUnsavedChanges(false);
				informUser("Saved Successfully");
			} catch (Exception err) {
				Utilities.error(err.getMessage());
			}   
	   }
		
   }
   
   public static void saveAs(Model model) {
	   JFileChooser fileChooser = new JFileChooser();
	   int response = fileChooser.showSaveDialog(null);
	   if(response == fileChooser.APPROVE_OPTION) {
		   try {
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile().toString()));
				os.writeObject(model);
				model.setUnsavedChanges(false);
			} catch (Exception err) {
				Utilities.error(err.getMessage());
			}
	   }
		
   }
   
   public static Model open() {
	   Model model = null;
	   try {
		   JFileChooser file = new JFileChooser();
		   int response = file.showOpenDialog(null);
		   if(response  == file.APPROVE_OPTION) {
			   ObjectInputStream os = new ObjectInputStream(new FileInputStream(file.getSelectedFile().getAbsolutePath()));
			   model = (Model) os.readObject();   
		   }		   
	   }
	   catch (Exception e) {
			e.printStackTrace();
	   }
	   return model; 
   }
}