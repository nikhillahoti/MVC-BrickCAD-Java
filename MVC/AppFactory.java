package mvc;

public interface AppFactory {
	
	 Model makeModel();
	 View makeView(String type);
	 Command makeCommand(String type);
	 String[] getViews();
	 String[] getCommands();
	 String getTitle();
	 String getHelp();
	 String about();
	
}
