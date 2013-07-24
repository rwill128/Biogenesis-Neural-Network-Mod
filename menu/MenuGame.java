package menu;

import javax.swing.JMenuItem;

import actions.ActionFactory;


public class MenuGame extends LocalizedMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MenuGame() {
		super("T_GAME");
		
		ActionFactory actionFactory = ActionFactory.getInstance();
		JMenuItem menuItem;
		
		menuItem = new JMenuItem(actionFactory.getNewGameAction());
		menuItem.setIcon(null);
		add(menuItem);
		StartStopMenuItem _menuStartStopGame =
				new StartStopMenuItem(actionFactory.getStartStopAction());
		_menuStartStopGame.setIcon(null);
		add(_menuStartStopGame);
		add(new JMenuItem(actionFactory.getOpenGameAction()));
		menuItem = new JMenuItem(actionFactory.getSaveGameAction());
		menuItem.setIcon(null);
		add(menuItem);
		add(new JMenuItem(actionFactory.getSaveGameAsAction()));
		add(new JMenuItem(actionFactory.getQuitAction()));
	}
}
