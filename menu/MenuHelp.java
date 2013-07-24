package menu;

import javax.swing.JMenuItem;

import actions.ActionFactory;


public class MenuHelp extends LocalizedMenu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MenuHelp() {
		super("T_HELP");
		
		ActionFactory actionFactory = ActionFactory.getInstance();
		add(new JMenuItem(actionFactory.getManualAction()));
		add(new JMenuItem(actionFactory.getCheckLastVersionAction()));
		add(new JMenuItem(actionFactory.getAboutAction()));
	}

}
