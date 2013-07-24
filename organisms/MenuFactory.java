package organisms;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import actions.ActionFactory;

public class MenuFactory {
	private JPopupMenu alivePopupMenu;
	private JPopupMenu deadPopupMenu;
	
	private static MenuFactory menuFactory = null;
	
	public static MenuFactory getInstance() {
		if (menuFactory == null)
			menuFactory = new MenuFactory();
		return menuFactory;
	}
	
	public MenuFactory() {
		ActionFactory actionFactory = ActionFactory.getInstance();
		JMenuItem menuItem;
	    alivePopupMenu = new JPopupMenu();
	    menuItem = new JMenuItem(actionFactory.getTrackAction());
	    menuItem.setIcon(null);
	    alivePopupMenu.add(menuItem);
	    menuItem = new JMenuItem(actionFactory.getFeedAction());
	    menuItem.setIcon(null);
	    alivePopupMenu.add(menuItem);
	    menuItem = new JMenuItem(actionFactory.getWeakenAction());
	    menuItem.setIcon(null);
	    alivePopupMenu.add(menuItem);
	    alivePopupMenu.add(new JMenuItem(actionFactory.getReproduceAction()));
	    alivePopupMenu.add(new JMenuItem(actionFactory.getRejuvenateAction()));
	    menuItem = new JMenuItem(actionFactory.getKillAction());
	    menuItem.setIcon(null);
	    alivePopupMenu.add(menuItem);
	    menuItem = new JMenuItem(actionFactory.getCopyAction());
	    menuItem.setIcon(null);
	    alivePopupMenu.add(menuItem);
	    alivePopupMenu.add(new JMenuItem(actionFactory.getExportAction()));
	    menuItem = new JMenuItem(actionFactory.getSaveImageAction());
	    menuItem.setIcon(null);
	    alivePopupMenu.add(menuItem);
	    
	    deadPopupMenu = new JPopupMenu();
	    menuItem = new JMenuItem(actionFactory.getReviveAction());
	    menuItem.setIcon(null);
	    deadPopupMenu.add(menuItem);
	    menuItem = new JMenuItem(actionFactory.getDisperseAction());
	    menuItem.setIcon(null);
	    deadPopupMenu.add(menuItem);
	}
	
	public JPopupMenu getAlivePopupMenu() {
		return alivePopupMenu;
	}
	
	public JPopupMenu getDeadPopupMenu() {
		return deadPopupMenu;
	}
}
