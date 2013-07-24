package menu;


import javax.swing.JMenuItem;

import actions.ActionFactory;

public class MenuNet extends LocalizedMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MenuNet() {
		super("T_NETWORK");
		
		JMenuItem menuItem;
		ActionFactory actionFactory = ActionFactory.getInstance();
		
		add(new JMenuItem(actionFactory.getNetConfigAction()));
		menuItem = new JMenuItem(actionFactory.getManageConnectionsAction());
		menuItem.setIcon(null);
		add(menuItem);
	}
}
