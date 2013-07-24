package menu;

import javax.swing.JMenuBar;

public class MainMenu extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MainMenu() {
		add(new MenuGame());
		add(new MenuWorld());
		add(new MenuNet());
		add(new MenuHelp());
	}

}
