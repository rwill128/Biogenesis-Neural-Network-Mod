package menu;

import javax.swing.JMenuItem;

import actions.ActionFactory;


public class MenuWorld extends LocalizedMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MenuWorld() {
		super("T_WORLD");
		
		JMenuItem menuItem;
		ActionFactory actionFactory = ActionFactory.getInstance();
		
		add(new JMenuItem(actionFactory.getStatisticsAction()));
		add(new JMenuItem(actionFactory.getLabAction()));
		menuItem = new JMenuItem(actionFactory.getIncreaseCO2Action());
		menuItem.setIcon(null);
		add(menuItem);
		menuItem = new JMenuItem(actionFactory.getDecreaseCO2Action());
		menuItem.setIcon(null);
		add(menuItem);
		add(new JMenuItem(actionFactory.getKillAllAction()));
		add(new JMenuItem(actionFactory.getDisperseAllAction()));
		add(new JMenuItem(actionFactory.getParametersAction()));
	}
}
